#!/bin/bash
DIRS=$(find . -type d | paste -s -d" " -)
plantuml -r $DIRS

