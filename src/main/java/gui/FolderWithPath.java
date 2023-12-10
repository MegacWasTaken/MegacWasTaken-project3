package gui;

import javafx.scene.control.TreeItem;


public class FolderWithPath extends TreeItem<String> {
    private String path;
    public String folderName;

    public FolderWithPath(String folderName, String path) {
        super(folderName);
        this.path = path;
    }

    public String getName() {
        return folderName;
    }

    public String getPath() {
        return path;
    }
}