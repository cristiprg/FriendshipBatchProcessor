package util.histogram;

import org.json.simple.JSONObject;
import org.mortbay.util.ajax.JSON;

import java.util.Arrays;
import java.util.Date;
import java.util.TreeSet;

/**
 * Created by cristiprg on 30-3-16.
 * Wrapper for the time distribution data we want to compute.
 *  1. Histogram - distribution per hour of the day
 *  2. Histogram - distribution per day of the week (future feature)
 *  3. Chart - cumulative distribution over the whole period
 */
public class GraphTimeDistribution {
    private Histogram perHour;
    private Histogram perWeekDay;
    private CumulativeDistribution cumulativeDistribution;

    public GraphTimeDistribution(){
        perHour = new Histogram(24);
        perWeekDay = new Histogram(7);
    }

    public void initializeCumulative(long minTimestamp, long maxTimestamp){
        cumulativeDistribution = new CumulativeDistribution(minTimestamp, maxTimestamp, 32);
    }

    public void addTimestampCumulative(long timestamp){
        cumulativeDistribution.addTimestamp(timestamp);
    }

    public void addHourPoint(int hour){
        perHour.addDataPoint(hour);
    }

    public void addWeekDayPoint(int weekDay){
        perWeekDay.addDataPoint(weekDay-1);
    }

    /**
     * @return JSON in the following format:
     * {
     *     'per_hour':  [1, 2, ..., 24],
     *     'per_day':   [1, 2, ..., 366],
     *     'per_month': [1, 2, ..., 12]
     * }
     */
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();

        obj.put("per_hour", perHour.getJSONArray());
        obj.put("per_week_day", perWeekDay.getJSONArray());
        obj.put("cumulative", cumulativeDistribution.getJSON());
        return obj;
    }

    public String toJSONString(){
        return getJSON().toJSONString();
    }
}




























