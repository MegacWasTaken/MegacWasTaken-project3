package main.java.search;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class BasicSearch {

    private HashMap<String, ArrayList<String>> currentData = new HashMap<>();

    public HashMap<String, ArrayList<String>> getData() {
        return currentData;
    }

    // keywords passed as string, return list of
    public void newKeyWords(String keywords) {
        String[] words = keywords.split(" ");
        for (String word : words) {
            if (!currentData.containsKey(word)) {
                currentData.put(word, new ArrayList<>());
            }
            currentData.get(word).add(keywords);
        }
    }

    public ArrayList<String> search(String[] keywords) {
        if (keywords == null || keywords.length < 1)
            return null;

        ArrayList<String> matches = currentData.get(keywords[0]);
        if (keywords.length == 1)
            return matches;

        // Cross-comparing
        for (int i = 1; i < keywords.length; i++) {
            ArrayList<String> newMatches = currentData.get(keywords[i]);
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

    public boolean containsString(ArrayList<String> array, String target) {
        for (String element : array) {
            if (element.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        BasicSearch newSearch = new BasicSearch();
        newSearch.getData().put("hello", new ArrayList<String>(Arrays.asList("hello", "hello program")));
        newSearch.getData().put("program", new ArrayList<String>(Arrays.asList("hello program", "word program")));
        newSearch.getData().put("word", new ArrayList<String>(Arrays.asList("word program")));

        // full keywords in database:
        // hello
        // hello program
        // word program
        ArrayList<String> helloResults = newSearch.search(new String[] { "hello" });
        System.out.println("Expecting:[hello, hello program]");
        System.out.println("Returned: " + helloResults);

        ArrayList<String> noResults = newSearch.search(new String[] { "hello progra" });
        System.out.println("Expecting:null");
        System.out.println("Returned: " + noResults);
    }

}
