package main;

import edu.princeton.cs.algs4.In;

import java.util.Map;
import java.util.*;

public class SynsetMap {
    private Map<Integer, String[]> idToWords;
    private Map<Integer,String> idToDef;

    public SynsetMap(String data) {
        In in = new In(data);
        idToWords = new HashMap<>();
        idToDef = new HashMap<>();

        while (in.hasNextLine() && !in.isEmpty()) {
            String[] token = in.readLine().split(",");
            int id = Integer.parseInt(token[0]);
            String[] words = token[1].split("\\s+");
            String def = token[2];
            idToWords.put(id, words);
            idToDef.put(id, def);
        }
    }

    public List<Integer> getId(String word) {
        // Initialize a list to hold the IDs that correspond to the given word.
        List <Integer> ids = new ArrayList<>();
        // Iterate over each entry in the idToWords map.
        for (Map.Entry<Integer, String[]> entry : idToWords.entrySet()) {
            // Check if the word is present in the array of words for this ID.
            for (String synonym : entry.getValue()) {
                if (synonym.equals(word)) {
                    // If the word is found, add the ID to the list of IDs.
                    ids.add(entry.getKey());
                    // Since we've found the word, we can break out of the inner loop.
                    break;
                }
            }
        }
        // Return the list of IDs that contain the given word.
        return ids;
    }

    public List<String> getWords(int id) {
        return Arrays.asList(idToWords.get(id));
    }

    public String getDef(int id) {
        return idToDef.get(id);
    }
}
