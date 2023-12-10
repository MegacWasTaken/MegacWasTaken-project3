Here are the commands to run this software. They must be run from the base directory where the project is installed, ending in MegacWasTaken-project3.

javac --module-path "SDK_PATH" --add-modules javafx.controls,javafx.fxml -d . "src/main/java/gui/AppState.java" "src/main/java/gui/FolderWithPath.java" "src/main/java/gui/GUI.java" "src/main/java/gui/HomeScreenController.java" "src/main/java/gui/NewController.java" "src/main/java/gui/Snippet.java" "src/main/java/gui/TreeItemData.java" "src/main/java/search/BasicSearch.java"

java --module-path "SDK_PATH" --add-modules javafx.controls,javafx.fxml -cp . gui.GUI

For example, if the JavaFX SDK is installed in the current directory, you would replace PATH_TO_SDK/lib with the absolute path to the current directory + the current directory + /lib.