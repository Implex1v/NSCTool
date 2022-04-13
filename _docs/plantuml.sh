#!/bin/bash
set -exu
cd "$(dirname "$0")"

if [[ ! -f plantuml.jar ]]; then
    wget https://github.com/plantuml/plantuml/releases/download/v1.2022.4/plantuml-1.2022.4.jar -O ./plantuml.jar
fi

echo pwd
DIRS=$(find . -type d | paste -s -d" " -)
java -jar plantuml.jar "**/*.puml"

