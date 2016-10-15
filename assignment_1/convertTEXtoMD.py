#! /usr/bin/env python
import os
os.system('pandoc -f latex -t markdown_github -o README.md Othello_assignment.tex --bibliography=references.bib')
print("Converted Othello_assignment.tex to README.md")
