#!/bin/bash
javac *.java
jar cfm Questioner.jar questionerManifest.txt *.class
