package gui;

import java.util.HashMap;

import javafx.scene.control.TreeItem;

public class AppState {
    private static AppState instance = new AppState();
    private TreeItem<String> treeRoot;
    private HashMap<String, Snippet> snippetList;

    private AppState() {
        treeRoot = new TreeItem<>("");
        snippetList = new HashMap<String, Snippet>();
    }

    public static AppState getInstance() {
        return instance;
    }

    public TreeItem<String> getTreeRoot() {
        return treeRoot;
    }

    public HashMap<String, Snippet> getSnippetList() {
        return snippetList;
    }

    public void setTreeRoot(TreeItem<String> root) {
        this.treeRoot = root;
    }

    public void setSnippetList(HashMap<String, Snippet> updated) {
        snippetList = updated;
    }
}
