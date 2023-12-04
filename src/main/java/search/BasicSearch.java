package search;

import java.util.HashMap;

import gui.AppState;
import gui.Snippet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.Arrays;

public class BasicSearch {

    protected static HashMap<String, ArrayList<String>> searchArrayList = new HashMap<String, ArrayList<String>>();

    public static HashMap<String, Snippet> getData() {
        return AppState.getInstance().getSnippetList();
    }

    // takes new set of keywords, distributes to searchArrayList
    public static void distributeSnippet(Snippet snip) {
        String keywords = snip.getKeywords();
        String language = snip.getLanguage();

        String fullKeywords = language + " " + keywords;
        newKeyWords(fullKeywords);
    }

    public static void newKeyWords(String keywords) {
        String[] words = keywords.split(" ");
        for (String word : words) {
            if (!searchArrayList.containsKey(word)) {
                searchArrayList.put(word, new ArrayList<>());
            }
            searchArrayList.get(word).add(keywords);
        }
    }

    public static ArrayList<String> search(String[] keywords) {
        if (keywords == null || keywords.length < 1)
            return null;

        // all of the keywords sets that match the language
        ArrayList<String> matches = searchArrayList.get(keywords[0]);
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
