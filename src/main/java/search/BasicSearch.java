package search;

import java.util.HashMap;
import java.util.Map;

import gui.AppState;
import gui.Snippet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BasicSearch {

    public static HashMap<String, ArrayList<String>> searchArrayList = new HashMap<String, ArrayList<String>>();

    public static HashMap<String, Snippet> getData() {
        return AppState.getInstance().getSnippetList();
    }

    public static void updateStoredHashMap(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(searchArrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // takes new set of keywords, distributes to searchArrayList
    public static void distributeSnippet(Snippet snip) {
        String keywords = snip.getKeywords();
        String language = snip.getLanguage();

        String fullKeywords = language + " " + keywords;
        newKeyWords(fullKeywords);
        String basePath = System.getProperty("user.dir");
        basePath = basePath + "/src/Database.ser";
        updateStoredHashMap(basePath);
    }

    public static void removeKeywords(String keywords) {
        System.out.println("State of current keywords list:");
        // print whole snippet list
        // for (Map.Entry<String, ArrayList<String>> entry : searchArrayList.entrySet())
        // {
        // for(String a : entry.getValue()){
        // System.out.println(entry.get(a));
        // }
        // }
        System.out.println("Deleting keywords: " + keywords);
        for (HashMap.Entry<String, ArrayList<String>> entry : searchArrayList.entrySet()) {
            System.out.println("");
            System.out.println("Searching current keywords list for word: " + entry.getKey() + "\n Keywords list is: "
                    + entry.getValue() + "\n");
            ArrayList<String> list = entry.getValue();
            System.out.println("Removing \"" + " " + keywords + "\"from " + entry.getValue());
            System.out.println("");
            for (String entryString : entry.getValue()) {
                System.out.println("Entry contained: \"" + entryString + "\"");
            }
            // remove keywords list from ArrayList<String> entry
            entry.getValue().remove(" " + keywords);
            System.out.println("After removing, new list is " + list);
        }

    }

    public static void newKeyWords(String keywords) {
        String[] words = keywords.split(" ");
        for (String word : words) {
            word = word.trim();
            if (!searchArrayList.containsKey(word)) {
                System.out.println("putting new word : \"" + word + "\"");
                searchArrayList.put(word, new ArrayList<>());
            } else if (!(searchArrayList.get(word).contains(keywords))) {
                searchArrayList.get(word).add(keywords);
            }
        }
    }

    public static ArrayList<String> search(String[] keywords) {
        if (keywords == null || keywords.length < 1)
            return null;

        // all of the keywords sets that match the language
        ArrayList<String> matches = searchArrayList.get(keywords[0]);
        for (String a : matches) {
            System.out.println("FOUND MATCH for keyword in searchArrayList : " + keywords[0] + ", : \n\t " + a);
        }
        if (keywords.length == 1)
            return matches;

        // Cross-comparing
        for (int i = 1; i < keywords.length; i++) {
            ArrayList<String> newMatches = searchArrayList.get(keywords[i]);
            for (int j = 0; j < matches.size(); j++) {
                boolean matchIndexStays = containsString(newMatches, matches.get(j));
                // Remove in matches array if not found in keywords list for next keyword
                if (!matchIndexStays) {
                    matches.remove(j);
                }
            }
        }
        return matches;
    }

    public static boolean containsString(ArrayList<String> array, String target) {
        for (String element : array) {
            if (element.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
