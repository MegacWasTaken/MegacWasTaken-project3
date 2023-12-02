package gui;

import javafx.scene.control.TreeItem;

public class AppState {
    private static AppState instance = new AppState();
    private TreeItem<String> treeRoot;

    private AppState() {
        treeRoot = new TreeItem<>("");
    }

    public static AppState getInstance() {
        return instance;
    }

    public TreeItem<String> getTreeRoot() {
        return treeRoot;
    }

    public void setTreeRoot(TreeItem<String> root) {
        this.treeRoot = root;
    }
}