package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.TreeItem;

public class AppState implements Serializable {

    private static AppState instance = new AppState();
    private transient TreeItem<String> treeRoot;
    private HashMap<String, Snippet> snippetList;
    private TreeItemData serializableTree;

    public AppState() {
        treeRoot = new TreeItem<>("");
        snippetList = new HashMap<String, Snippet>();
    }

    public static AppState getInstance() {
        return instance;
    }

    public static void setInstance(AppState inputInstance) {
        instance = inputInstance;
    }

    public TreeItem<String> getTreeRoot() {
        return treeRoot;
    }

    public HashMap<String, Snippet> getSnippetList() {
        return snippetList;
    }

    public void setTreeRoot(TreeItem<String> root) {
        this.treeRoot = root;
        String basePath = System.getProperty("user.dir");
        basePath = basePath + "/src/AppState.ser";
        saveStateToFile(basePath);
    }

    public void setLoadingTreeRoot(TreeItem<String> root) {
        this.treeRoot = root;
    }

    public void updateSnippetList(String a, Snippet b) {
        System.out.println("UPDATING THE SNIPPET LIST WITH A NEW SNIPPET NOW!!");
        snippetList.put(a, b);
        String basePath = System.getProperty("user.dir");
        basePath = basePath + "/src/AppState.ser";
        saveStateToFile(basePath);
        System.out.println("After save, snippet list: ");
        printAllSnippets();
    }

    public TreeItemData getSerializableData() {
        return serializableTree;
    }

    public void setSerializableData(TreeItemData data) {
        serializableTree = data;
    }

    private static TreeItemData convertToTreeItemData(TreeItem<String> treeItem) {
        TreeItemData data = new TreeItemData(treeItem.getValue());
        for (TreeItem<String> child : treeItem.getChildren()) {
            data.getChildren().add(convertToTreeItemData(child));
        }
        return data;
    }

    private static TreeItem<String> convertToTreeItem(TreeItemData data) {
        TreeItem<String> treeItem = new TreeItem<>(data.getValue());
        for (TreeItemData childData : data.getChildren()) {
            treeItem.getChildren().add(convertToTreeItem(childData));
        }
        return treeItem;
    }

    public void saveStateToFile(String filename) {
        System.out.println("Saving AppState to file: " + filename);
        AppState state = AppState.getInstance();
        TreeItemData rootData = convertToTreeItemData(state.getTreeRoot());
        state.setSerializableData(rootData);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(state);
            System.out.println("Saved AppState:");
            printTree(state.getTreeRoot(), 0);
            System.out.println("After saving state, here is current list of snippets:");
            printAllSnippets(); 
            System.out.println("End of current list of snippets");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadStateFromFile(String filename) {
        File file = new File(filename);
        if (file.exists() && !file.isDirectory()) {
            System.out.println("Loading AppState from file: " + filename);
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                AppState state = (AppState) in.readObject();
                TreeItem<String> rootItem = convertToTreeItem(state.getSerializableData());
                state.setLoadingTreeRoot(rootItem);
                System.out.println("Loaded AppState:");
                printTree(rootItem, 0);
                AppState.setInstance(state);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No previous AppState found. Initializing new state.");
            AppState.setInstance(new AppState());
        }
    }

    private static void printTree(TreeItem<String> item, int depth) {
        System.out.println(" ".repeat(depth * 2) + item.getValue());
        for (TreeItem<String> child : item.getChildren()) {
            printTree(child, depth + 1);
        }
    }

    public void printAllSnippets() {
        System.out.println("Printing all snippets:");
        for (Map.Entry<String, Snippet> entry : snippetList.entrySet()) {
            String key = entry.getKey();
            Snippet snippet = entry.getValue();
            System.out.println("Key: " + key + ", Snippet: " + snippet);
        }
    }

}