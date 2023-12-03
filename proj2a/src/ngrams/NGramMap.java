package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    private Map<String, TimeSeries> data;
    private TimeSeries years;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // Parse the words file and store data in a map
        In words = new In(wordsFilename);
        Map<String, TimeSeries> worData = new HashMap<>();
        String currWord = "";
        TimeSeries ts = new TimeSeries();
        // Parse words file and create TimeSeries for each word
        while (words.hasNextLine()) {
            String word = words.readString();
            if (currWord.isEmpty()) {
                currWord = word;
            }
            if (!currWord.equals(word)) {
                worData.put(currWord, ts);
                ts = new TimeSeries();
                currWord = word;
            }
            int year = words.readInt();
            double frequency = words.readDouble();
            words.readInt();
            ts.put(year, frequency);
            words.readLine();
        }
        worData.put(currWord, ts);
        data = worData;

        In counter = new In(countsFilename);
        TimeSeries yearData = new TimeSeries();
        // Parse counts file and create TimeSeries for total word counts per year
        while (counter.hasNextLine()) {
            String[] info = counter.readLine().split(",");
            yearData.put(Integer.parseInt(info[0]), Double.parseDouble(info[1]));
        }
        years = yearData;
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // Get the TimeSeries data for the specified word and create a new TimeSeries within the given range
        TimeSeries ts = data.get(word);
        if (ts == null) {
            return new TimeSeries();
        }
        TimeSeries history = new TimeSeries(data.get(word), startYear, endYear);
        return history; // A TimeSeries representing the word's frequency history within the specified years.
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        // Get the TimeSeries data for the specified word
        TimeSeries ts = data.get(word);
        if (ts == null) {
            return new TimeSeries();
        }
        return ts; // A TimeSeries representing the word's frequency history across all available years.
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return years;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // Get the TimeSeries data for the specified word and the total word counts within the given range
        TimeSeries worData = new TimeSeries(data.get(word), startYear, endYear);
        if (worData == null) {
            return new TimeSeries();
        }
        TimeSeries totals = new TimeSeries(years, startYear, endYear);
        // Calculate and return the relative frequency of the word within the specified years
        return worData.dividedBy(totals);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        // Get the TimeSeries data for the specified word and the total word counts for all years
        TimeSeries worData = data.get(word);
        if (worData == null) {
            return new TimeSeries();
        }
        // Calculate and return the relative frequency of the word compared to all words per year
        return worData.dividedBy(years);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        // Calculate the summed relative frequency of all words within the specified years
        TimeSeries sum = summedWeightHistory(words);
        sum = new TimeSeries(sum, startYear, endYear);
        return sum;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries sum = new TimeSeries();
        // Iterate over the words and sum their relative frequencies, ignoring words not found
        for (String word: words) {
            if (data.get(word) != null) {
                sum = sum.plus(data.get(word).dividedBy(totalCountHistory()));
            }
        }
        return sum;
    }
}
