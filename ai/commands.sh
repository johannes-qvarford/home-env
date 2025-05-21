#!/bin/bash

for model in "$1/"*.Modelfile; do
    model_name=$(basename "$model" .Modelfile)
    echo "Creating model: $model_name"
    ollama create $model_name -f "$1/$model_name.Modelfile"
done