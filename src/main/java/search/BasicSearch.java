package search;

import java.util.HashMap;
import java.util.Map.Entry;
import gui.AppState;
import gui.Snippet;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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
        // System.out.println("now distributing: " + keywords);
        String language = snip.getLanguage();

        String fullKeywords = language + " " + keywords;
        newKeyWords(fullKeywords);
        String basePath = System.getProperty("user.dir");
        basePath = basePath + "/src/Database.ser";
        updateStoredHashMap(basePath);
    }

    public static void removeKeywords(String keywords) {
        // System.out.println("State of current keywords list:");
        // print whole snippet list
        // for (Map.Entry<String, ArrayList<String>> entry : searchArrayList.entrySet())
        // {
        // for(String a : entry.getValue()){
        // System.out.println(entry.get(a));
        // }
        // }
        // System.out.println("Deleting keywords: " + keywords);
        for (HashMap.Entry<String, ArrayList<String>> entry : searchArrayList.entrySet()) {
            //System.out.println("Now removing now:" + keywords);
            entry.getValue().remove(" " + keywords);
        }
        String basePath = System.getProperty("user.dir");
        basePath = basePath + "/src/Database.ser";
        updateStoredHashMap(basePath);
    }

    public static void newKeyWords(String keywords) {
        String[] words = keywords.split(" ");
        for (String word : words) {
            // "\n now adding word in new keywords: " + word + "\n");
            word = word.trim();
            if (!searchArrayList.containsKey(word)) {
                // System.out.println("putting new word : \"" + word + "\"");
                searchArrayList.put(word, new ArrayList<>());
                searchArrayList.get(word).add(keywords);
            } else if (!(searchArrayList.get(word).contains(keywords))) {
                searchArrayList.get(word).add(keywords);
            }
        }
        // System.out.println("Finished distributing keywords: " + keywords + ".\n The
        // new database follows: ");
        for (Entry<String, ArrayList<String>> entry : BasicSearch.searchArrayList.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            // System.out.println("\t \t Key: " + key + "\n\t\t Value: " + value);
        }
    }

    public static ArrayList<String> search(String[] keywords) {
        if (keywords == null || keywords.length < 1)
            return null;

        // all of the keywords sets that match the language
        ArrayList<String> matches = searchArrayList.get(keywords[0]);
        for (String a : matches) {
            // System.out.println("In hashmap, keywords list that contain " + keywords[0] +
            // ": " + a);
        }
        if (matches.size() < 1)
            // System.out.println("No matches found for query: " + keywords[0]);
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
