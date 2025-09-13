@echo off
echo Creating JAR file...
echo Main-Class: NumberGuessingGame > manifest.txt
jar cfm NumberGuessingGame.jar manifest.txt NumberGuessingGame.class
del manifest.txt
echo JAR file created successfully!
pause