import org.json.simple.JSONObject;
import org.neo4j.graphdb.*;
import util.histogram.CumulativeDistribution;
import util.histogram.GraphTimeDistribution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by cristiprg on 30-3-16.
 */
public class GraphStatistics {

    /**
     * Computes the time distribution of friendships timestamps by: hour, day, month;
     * And then, it stores this as JSON property of STATS node.
     * @param graphDB
     */
    public static void insertTimeDistribution(GraphDatabaseService graphDB) {

        GraphTimeDistribution distribution = new GraphTimeDistribution();
        try(Transaction tx = graphDB.beginTx()) {

            long minTimestamp = Long.MAX_VALUE;
            long maxTimestamp = Long.MIN_VALUE;


            // 1. compute histograms
            Label label = DynamicLabel.label("Friendship");
            try (ResourceIterator<Node> friendships = graphDB.findNodes(label)) {

                while (friendships.hasNext()) {
                    // read the timestamp from the friendship node
                    String stringDate = (String) friendships.next().getProperty("timestamp");

                    // discard non-existing data
                    if (stringDate.equals("0")) continue;

                    // convert timestamp to calendar type
                    Calendar cal = Calendar.getInstance();
                    long timestamp = Long.valueOf(stringDate) * 1000L;
                    cal.setTime(new Date(timestamp));

                    // add stuff to the histograms
                    distribution.addHourPoint(cal.get(Calendar.HOUR_OF_DAY));


                    minTimestamp = Math.min(minTimestamp, timestamp);
                    maxTimestamp = Math.max(maxTimestamp, timestamp);
                }
            }

            // 2nd iteration ... best trade-off
            distribution.initializeCumulative(minTimestamp, maxTimestamp);
            try (ResourceIterator<Node> friendships = graphDB.findNodes(label)) {
                while (friendships.hasNext()){
                    // read the timestamp from the friendship node
                    String stringDate = (String) friendships.next().getProperty("timestamp");

                    // discard non-existing data
                    if (stringDate.equals("0")) continue;

                    long timestamp = Long.valueOf(stringDate) * 1000L;

                    distribution.addTimestampCumulative(timestamp);
                }
            }

            // test for now what we've done so far
//            try {
//
//                FileWriter file = new FileWriter("test.json");
//                file.write(distribution.getJSON().toJSONString());
//                file.flush();
//                file.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            // 2. create the stats JSON and insert into the STATS node
            JSONObject statsObj = new JSONObject();
            statsObj.put("time_distribution", distribution.getJSON());

            Node statsNode = graphDB.createNode();
            statsNode.addLabel(DynamicLabel.label("facebookStats"));
            statsNode.setProperty("stats", statsObj.toJSONString());

            tx.success();
        }


    }
}
