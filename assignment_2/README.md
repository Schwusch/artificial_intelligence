Introduction
============

In this assignment the objective was to implement an AI controller for Ms. Pacman that is capable of learning from data extracted from previous (human) gameplays. The controller had to be a decision tree trained by means of a ID3 algorithm(Russel 2010). We chose to extend the attribute selection algorithm to C4.5(Russel 2010), since it is such a small extension of ID3.

-   What input parameters have you used as the input of the decision tree and why?

-   Why have you discouraged the others?

-   Did you have to discretize any parameters? Why? How have you accomplished this?

-   How did you code the learning algorithm and the attribute selection function?

-   What experiences have you acquired?

Ms. Pac-Man
===========

Ms. Pac-Man is a arcade video game released in the late 1980s. The game is not to be confused with the original Pac-Man game though the gameplay is very similar. It is one of the most popular arcade game of all time and also one of the most successful, selling over 115,000 arcade cabinets. (Wikipedia 2016)

Gameplay
--------

The objective is to earn points by eating pills and at the same time avoid the four ghosts that are circling the playing field. It is also possible to eat a larger pill in order to make the ghosts edible and scoring points by eating them. If a ghost, that is not edible, collides with Ms. Pac-Man a life is lost. The game ends when all three lives are lost or when all the levels are completed. To complete a level Ms. Pac-Man must eat all the pills on the playing field.

Input parameters
================

The Ms. Pac-Man implementation that we were given contained some parameters to choose from. For example there are four ghosts on the playing field. Each of the ghosts has attributes like:

-   Distance (from the ghosts to Ms. Pac-Man)

-   Direction (in which direction are the ghosts moving in)

-   Edible (are the ghosts edible)

Other parameters that is not concerning the ghosts but still can be used are:

-   Number of pills left

-   Number of big pills left

-   Current game time

-   Current score

-   Number of lives left

-   Ms. Pac-Mans position

-   Last chosen direction

Chosen parameters
-----------------

Our general approach when choosing parameters for the learning algorithm was to look at a human when learning to play Ms. Pac-Man. A human player might look at the state of the ghosts, are they edible, close and moving towards Ms. Pac-Man? Based on those criteria the player might have enough information to evade the ghosts and survive a little longer. Our philosophy was to use as little information as possible to solve the task, to avoid overfitting.

Our biggest issue was to determine, with the recorded data, whether Ms. Pac-Man was facing a ghost at a given time. This seemed impossible and multiple attempts were made to use the last chosen direction and direction of a ghost to make sense of the data. The issue was, when using the last made move as an attribute in any way, the decision tree became extremely shallow. It only required a ghosts direction as an attribute in the root node to determine the next move. This is because the subsets of the data, when finding subsets with an attribute taking a value combined with the class value, all data tuples in the subset has the same class and can be considered as a leaf node when passing that subset to a new node.

Discarded parameters
--------------------

The available parameters are in abundance and parameters such as total time since game started may not be relevant for the survival of Ms. Pac-Man. Similar parameters which does not have any effect to a human players behaviour are ruled out since our guess is that it would not contribute to the heuristic decision tree either.

Discretized parameters
----------------------

Some parameters are continuous, such as distance, and needs to be discretized before used in the decision tree. The reason for this is that the decision tree would have an unreasonable amount of child nodes if there were a child node for each value of a continuous attribute. It needs an unreasonable amount of data to support that many values. That is also why discretizing needs to be reasonable, with not to many possible discrete values.

The Ms. Pac-Man framework provides a method and enumeration in the `DataTuple` class for this purpose, with seven possible discrete values. We believed this also was to granular and made a similar method and enumeration, limiting the possible values to `HIGH` and `LOW`. The inflection point between these values was chosen to be a normalized distance value of 0.2. This value was simply concluded with guesses and empirical tests to find a moderate value that was good enough.

The learning algorithm
======================

The learning algorithms main purpose is to create a decision tree that is able to return a `MOVE` when given a game state. Our tree is created when calling a `Node` constructor with the complete dataset and a list of attributes to learn from. This object will be the decision tree root node. It has a `getDecision()` method which will return a `MOVE` object that the learning algorithm views as the most appropriate according to the learning data.

ID3 and C4.5
------------

The main part of the learning algorithm is the C4.5 attribute choosing algorithm which, at each node, decides what attribute has the highest benefit when analyzing the set of data given to the node. The data given to the root node is supposedly very impure, since it is recorded from human playthroughs, which probably contains mistakes which lead to the death of Ms. Pac-Man. This means that the information entropy is relatively large and is hard to classify.

The ID3 algorithm has to find an attribute that leads to a dataset that has the least entropy and highest information gain possible among all attributes. This way the decision tree is guaranteed to be the best tree possible.

Our class `C45` has only one method visible to outside classes (Listing \[attribute\]), and one private method (Listing \[infod\]).

|C45.selectAttribute()|

    public static String selectAttribute(
                LinkedList<DataTuple> dataList,
                LinkedList<String> attributes) {
        |\dots|
    }
        

|C45.calculateInfoD()|

    private static double calculateInfoD(LinkedList<DataTuple> dataTuples) {
        HashMap<Constants.MOVE, Double> tuplePerClass = new HashMap<>(); |\label{tupleperclass}|
        |\dots|
        // Increase every find by one in the map
        for (DataTuple tuple : dataTuples) {
            tuplePerClass.put(tuple.DirectionChosen,
                tuplePerClass.get(tuple.DirectionChosen) + 1);
        }
        double dataSize = (double) dataTuples.size();
        double infoD = 0;
        for (Constants.MOVE move : Constants.MOVE.values()) { |\label{addingstart}|
            double count = tuplePerClass.get(move);
            if (count > 0) {
                infoD += -(count / dataSize) *
                        (Math.log(count / dataSize) / Math.log(2)); |\label{addingend}|
            }
        }
        return infoD;
    }

Both are static and holds no state between method calls. The `selectAttribute()` method simply returns the name of the most rewarding attribute in a string and the `calculateInfoD()` method returns a `double` value derived of equation \[infoD\]. \[\label{infoD}
    Info(D)=-\sum_{i=1}^{m}p_i log_2(p_i)\]

The variable \(p_i\) is simply the number of tuples belonging to a certain class value e.g. `UP` divided with the total number of tuples in the dataset. This is achieved in the code by counting all class value occurrences in a hashmap (Listing \[infod\], line \[tupleperclass\]) and adding \(p_i log_2(p_i)\) for every value in the class (Listing \[infod\], line \[addingstart\] - \[addingend\]) to the `infoD` variable.

The `selectAttribute()` method loops through all attributes passed to that method and performs a number of operations to find the one with most benefit. It calculates a gain for each attribute according to equation \[GainA\]: \[\label{GainA}
    Gain(A)=Info(D)-Info_A (D)\] \(Info_A (D)\) is explained by equation \[InfoAD\]:

\[\label{InfoAD}
    Info_A (D)=\sum_{j=1}^{v}\frac{|D_j|}{|D|}\times Info(D_j)\]

\(Gain(A)\) is then used for calulating the gain ratio according to equation \[GainRatio\]:

\[\label{GainRatio}
    GainRatio(A)=\frac{Gain(A)}{SplitInfo_A (D)}\]

\(SplitInfo_A (D)\) is explained by equation \[SplitInfo\]:

\[\label{SplitInfo}
    SplitInfo_A (D)=-\sum_{j=1}^{v}\frac{|D_j|}{|D|} \times log_2\bigg( \frac{|D_j|}{|D|}\bigg)\]

In code, these are implemented as follows in listing \[excerptSelect\]:

    double infoD = calculateInfoD(dataList); |\label{infoDCalc}|
    |\dots|
    for (String attr : attributes) { |\label{forEveryAttr}|
        |\dots|
        for (String value : Utils.getAttributeVariations(attr)) {
            subset = Utils.createSubset(dataList, field, value); |\label{infoADCalcStart}|
            infoAD += ((double) subset.size() / (double) dataList.size()) *
                                                        calculateInfoD(subset); |\label{infoADCalcEnd}|
            if(subset.size() > 0) {                                             |\label{splitInfoADStart}|
                splitInfoAD += -((double) subset.size() / (double) dataList.size()) *
                        (Math.log((double) subset.size() / (double) dataList.size()) /
                                                                (double) Math.log(2));
            }                                                   |\label{splitInfoADEnd}|
        }
        double gainA = infoD - infoAD; |\label{gainACalc}|
        double gainRatio = Double.MAX_VALUE;          |\label{gainRatioStart}|
        if(splitInfoAD > 0)
            gainRatio = gainA / splitInfoAD;    |\label{gainRatioEnd}|
        |\dots|
    }
    |\dots|

First \(Info(D)\) on the whole dataset is calculated (Listing \[excerptSelect\], line \[infoDCalc\]), then for every attribute (Listing \[excerptSelect\], line \[forEveryAttr\]) \(Info_A (D)\) (Listing \[excerptSelect\], line \[infoADCalcStart\] - \[infoADCalcEnd\]) and \(SplitInfo_A (D)\) (Listing \[excerptSelect\], line \[splitInfoADStart\] - \[splitInfoADEnd\]) is calculated. \(SplitInfo_A (D)\) can only be calculated if the subset (\(|D_j|\)) is larger than zero, because \(log_2 (0)\) is undefined. \(Gain(A)\) is then calculated (Listing \[excerptSelect\], line \[gainACalc\]) and \(GainRatio(A)\) is calculated if \(SplitInfo_A (D)\) is greater than zero, otherwise it is set to the biggest possible value (Listing \[excerptSelect\], line \[gainRatioStart\] - \[gainRatioEnd\]). This is of course because something cannot be divided by zero, but we assume \(\lim_{SplitInfo_A (D) \to 0}\) which means \(\lim_{GainRatio(A) \to \infty}\). The algorithm then returns the attribute with the highest gain ratio.

The Node class
--------------

Här ska vi förklara nodklassen

Accuracy of the classifier
==========================

Inte så långt kapitel om hur vi mätte noggranheten på klassifieraren

Conclusions
===========

Erfarenheter etc

Russel, P., S. Norvig. 2010. *Artificial Intelligence, a Modern Approach*. Prentice Hall.

Wikipedia. 2016. “Ms. Pac-Man.” <https://en.wikipedia.org/wiki/Ms._Pac-Man>.


