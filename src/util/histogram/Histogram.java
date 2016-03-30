package util.histogram;

import org.json.simple.JSONArray;

/**
 * Created by cristiprg on 30-3-16.
 */
public class Histogram {
    private final int[] freq;

    /**
     * Creates a new instance of histogram.
     *
     * @param N number of elements (classes) in the histogram.
     */
    public Histogram(int N) {
        freq = new int[N];
    }

    public void addDataPoint(int i) {
        ++freq[i];
    }

    public int[] getHistogram() {
        return freq;
    }

    public JSONArray getJSONArray(){
        JSONArray list = new JSONArray();
        for(int data : getHistogram())
            list.add(data);

        return list;
    }
}