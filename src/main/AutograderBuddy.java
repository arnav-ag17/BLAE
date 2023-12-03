package main;

import browser.NgordnetQueryHandler;


public class AutograderBuddy {

    public static NgordnetQueryHandler getHyponymHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        SynsetMap sm = new SynsetMap(synsetFile);
        Graph graph = new Graph(sm, hyponymFile);

        return new HyponymsHandler(graph, wordFile, countFile);
    }
}
