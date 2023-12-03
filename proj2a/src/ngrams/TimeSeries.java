package ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        // Iterate over the years in the given range and copy data from the input TimeSeries
        for (int i = startYear; i <= endYear; i++) {
            if (ts != null) {
                if (ts.containsKey(i)) {
                    put(i, ts.get(i));
                }
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        return new ArrayList<>(this.keySet());
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        return new ArrayList<>(this.values());
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries result = new TimeSeries(); //Create an empty TimeSeries
        // Create a set of all unique years from both TimeSeries
        Set<Integer> years = new HashSet<>(); // Set only accepts unique values
        years.addAll(this.keySet());
        years.addAll(ts.keySet());

        // Iterate over the unique years and calculate the sum of values from both TimeSeries
        for (int year: years) {
            double sum = 0.0;
            if (this.containsKey(year)) {
                sum += this.get(year);
            }
            if (ts.containsKey(year)) {
                sum += ts.get(year);
            }
            result.put(year, sum);
        }
        return result;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries result = new TimeSeries();
        for (int year: this.years()) {
            // Check if the corresponding year exists in the input TimeSeries
            if (!ts.containsKey(year)) {
                throw new IllegalArgumentException("Year not found"); //Throw an IllegalArgumentException Error
            }
            double quotient = this.get(year) / ts.get(year);
            result.put(year, quotient);
        }
        return result;
    }
}
