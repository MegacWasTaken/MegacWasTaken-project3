package main.java.search;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BasicSearch {

    public static HashMap<String, String[]> currentData = new HashMap<>();

    // when new addition in main database, add here
    public static void newKeyWords(String[] keywords){

    }

    public static String[] search(String[] keywords) {
        if(keywords == null || keywords.length < 1) return null;

        String[] matchesArray = currentData.get(keywords[0]);
        if(keywords.length == 1) return matchesArray;
        List<String> matches = new ArrayList<>(Arrays.asList(matchesArray));

        // Cross-comparing
        for(int i = 1; i < keywords.length; i++){
            String[] newMatches = currentData.get(keywords[i]);
            for(int j = 0; j < matchesArray.length; j++){
                boolean matchIndexStays = containsString(newMatches, matches.get(j));
                // Remove in matches array if not found in keywords list for next keyword
                if(!matchIndexStays){
                    matches.remove(j);
                }
            }
        }
        matchesArray = matches.toArray(new String[0]);
        return matchesArray;
    }

    public static boolean containsString(String[] array, String target){
        for(String element: array){
            if(element.equals(target)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        currentData.put("hello", new String[] { "hello", "hello program" });
        currentData.put("program", new String[] { "hello program", "word program" });
        currentData.put("word", new String[] { "word program" });

        // full keywords in database:
            // hello
            // hello program
            // word program
        String[] helloResults = search(new String[]{"hello"});
        System.out.println("Expecting:[hello, hello program]");
        System.out.println("Returned: " + Arrays.toString(helloResults));

        String[] noResults = search(new String[]{"hello progra"});
        System.out.println("Expecting:null");
        System.out.println("Returned: " + Arrays.toString(noResults));
    }

}
