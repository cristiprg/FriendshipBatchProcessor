package util.histogram;

import  org.json.simple.JSONArray;
import  org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cristiprg on 30-3-16.
 */
public class CumulativeDistribution {
    private long minTimestamp = Long.MAX_VALUE;
    private long maxTimestamp = Long.MIN_VALUE;
    private long nrSteps = 0;
    private long stepSize = 0;
    private int[] buckets;

    public CumulativeDistribution(long minTimestamp, long maxTimestamp, int nrSteps) {
        this.minTimestamp = minTimestamp;
        this.maxTimestamp = maxTimestamp;
        this.nrSteps = nrSteps;
        this.stepSize = (maxTimestamp - minTimestamp) / nrSteps;
        buckets = new int[nrSteps];
    }

    public void addTimestamp(long timestamp){
        int index =(int) ((timestamp - minTimestamp) / stepSize);
        if (index == buckets.length) --index;
        ++buckets[index];
    }

    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("values", getBucketValuesJSONArray());
        obj.put("ticks", getBucketIndexesJSONArray());
        return obj;
    }

    private JSONArray getBucketValuesJSONArray(){
        int currentValue = 0;
        JSONArray list = new JSONArray();
        for(int data : buckets)
            list.add(currentValue += data);

        return list;
    }

    /**
     * The indexes returned are in fact dates in format 'YYYY-MM-DD'... as opposed to integers
     */
    private JSONArray getBucketIndexesJSONArray(){
        JSONArray list = new JSONArray();
        for (int i = 0; i < buckets.length; ++i){
            long timeStamp = minTimestamp + i * stepSize;
            Date time = new Date(timeStamp);
            DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            String stringTime = df.format(time);
            list.add(stringTime);
        }
        return list;
    }
}
