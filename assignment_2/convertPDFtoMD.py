#! /usr/bin/env python
import fnmatch
import os
os.system('convert -density 150 PacMan_assignment.pdf -quality 90 ./images/image.png')

image_list = []

for file in os.listdir('./images'):
    if fnmatch.fnmatch(file, '*.png'):
        image_list.append(file)

image_list.sort()

f = open('README.md', 'w')

for image in image_list:
    f.write('![](images/' + image + ')\n')

f.close()
print("Converted Othello_assignment.pdf to README.md")
