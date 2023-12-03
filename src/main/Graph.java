package main;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class Graph {
    private Map<Integer, Set<Integer>> adjList;
    private SynsetMap sm;

    public Graph(SynsetMap synsetMap, String hyponymsFile) {
        adjList = new HashMap<>();
        sm = synsetMap;

        // Process the file containing hyponym relationships
        In in = new In(hyponymsFile);
        while (in.hasNextLine()) {
            String[] tokens = in.readLine().split(",");
            int hypernymId = Integer.parseInt(tokens[0]);
            List<Integer> hyponymIds = new ArrayList<>();
            for (int i = 1; i < tokens.length; i++) {
                int hyponymId = Integer.parseInt(tokens[i]);
                hyponymIds.add(hyponymId);
            }
            addNode(hypernymId);
            for (int hyponymId : hyponymIds) {
                addNode(hyponymId);
                addEdge(hypernymId, hyponymId);
            }
        }
        transitiveClosure();
    }

    public void transitiveClosure() {
        for (int node: adjList.keySet()) {
            List<Integer> descendants = findDescendants(node);
            for (int descendant : descendants) {
                addEdge(node, descendant);
            }
        }
    }

    private void addNode(int node) { // Use integer representation
        // add node to the graph
        if (!adjList.containsKey(node)) {
            adjList.put(node, new HashSet<>());
        }
    }

    private void addEdge(int u, int v) { // Use integer representation
        // add edge to the graph
        adjList.get(u).add(v);
    }

    private List<Integer> findDescendants(int hypernym) { // Use integer representation
        Set<Integer> descendants = new HashSet<>();
        Queue<Integer> fringe = new LinkedList<>();
        if (!adjList.containsKey(hypernym)) {
            return new ArrayList<>(descendants);
        }
        fringe.add(hypernym);
        while (!fringe.isEmpty()) {
            int curr = fringe.remove();
            for (int adjNode : adjList.getOrDefault(curr, Collections.emptySet())) {
                if (descendants.add(adjNode)) {
                    fringe.add(adjNode);
                }
            }
        }
        descendants.remove((Integer) hypernym); // Use Integer to remove object from list
        return new ArrayList<>(descendants);
    }

    public List<String> getHyponyms(String hypernym) {
        // Compile a list of synset IDs associated with the hypernym word
        List<Integer> hyperId = sm.getId(hypernym);
        List<String> hyponyms = new ArrayList<>();

        // Iterate over each synset ID to gather hyponyms
        for (int id : hyperId) {
            // Add all words from the current synset
            hyponyms.addAll(sm.getWords(id));

            // Check if there are direct hyponyms for the current synset
            if (adjList.containsKey(id)) {
                Set<Integer> directHyponyms = adjList.get(id);
                // For each hyponym ID, add all associated words to the list
                for (int hyponymId : directHyponyms) {
                    hyponyms.addAll(sm.getWords(hyponymId));
                }
            }
        }
        // Return the complete list of words including the hypernym and its hyponyms
        return hyponyms;
    }
}
