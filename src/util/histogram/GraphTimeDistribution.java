package util.histogram;

import org.json.simple.JSONObject;

import java.util.Arrays;

/**
 * Created by cristiprg on 30-3-16.
 * Wrapper for the three time distributions we want to compute.
 */
public class GraphTimeDistribution {
    private Histogram perHour;
    private Histogram perDay;
    private Histogram perMonth;

    public GraphTimeDistribution(){
        perHour = new Histogram(24);
        perDay = new Histogram(366+1);
        perMonth = new Histogram(12);
    }

    public void addHourPoint(int hour){
        perHour.addDataPoint(hour);
    }

    public void addDayPoint(int day){
        perDay.addDataPoint(day);
    }

    public void addMonthPoint(int month){
        perMonth.addDataPoint(month);
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
        obj.put("per_day", perDay.getJSONArray());
        obj.put("per_month", perMonth.getJSONArray());
        return obj;
    }

    public String toJSONString(){
        return getJSON().toJSONString();
    }
}




























