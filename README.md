Here are the commands to run this software:

javac --module-path PATH_TO_SDK/lib --add-modules javafx.controls,javafx.fxml -d . src/main/java/gui/*.java src/main/java/search/*.java

java --module-path PATH_TO_SDK/lib --add-modules javafx.controls,javafx.fxml -cp . gui.GUI

For example, if the JavaFX SDK is installed in the current directory, you would replace PATH_TO_SDK/lib with the absolute path to the current directory + the current directory + /lib.