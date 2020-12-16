#! /bin/bash

javac -encoding utf8 $(find . -name "*.java") -d out
cp -a res/. out/
java -cp out ch.epfl.cs107.play.Play