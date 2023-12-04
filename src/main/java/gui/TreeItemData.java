package gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeItemData implements Serializable {
    private String value;
    private List<TreeItemData> children;

    public TreeItemData(String value) {
        this.value = value;
        children = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<TreeItemData> getChildren(){
        return children;
    }

}