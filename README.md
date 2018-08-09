**Build Instructions:**
1. Clone repository using "git clone https://github.com/testeaxeax/ld42.git"
2. Set your working directory to the projects root by using "cd ./ld42"
3.1 Use "./gradlew desktop:dist" to build the desktop version
3.2 Use "./gradlew html:dist" to build the HTML version
4. The JAR file is now in "./desktop/build/libs/" and the HTML root directory is "./html/build/dist/"

**Import into Eclipse:**
1. Set your working directory to the projects root by using "cd ./ld42"
2. Create the Eclipse project files using "./gradlew eclipse"
3. Open Eclipse
4. Click on "Menu" and on "Import..."
5. Extend the "General" folder and select "Existing Projects into Workspace"
6. Click on "Next"
7. Select the root directory ("ld42")
8. Click on "Finish"
