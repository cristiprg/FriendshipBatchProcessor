import org.neo4j.graphdb.*;
import util.histogram.GraphTimeDistribution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

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

            // 1. compute histograms
            Label label = DynamicLabel.label("Friendship");
            try (ResourceIterator<Node> friendships = graphDB.findNodes(label)) {

                while (friendships.hasNext()) {
                    // read the timestamp from the friendship node
                    String stringDate = (String)friendships.next().getProperty("timestamp");

                    // discard non-existing data
                    if (stringDate.equals("0")) continue;

                    // convert timestamp to calendar type
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(Long.valueOf(stringDate) * 1000L));

                    // add stuff to the histograms
                    distribution.addHourPoint(cal.get(Calendar.HOUR_OF_DAY));
                    distribution.addDayPoint(cal.get(Calendar.DAY_OF_YEAR));
                    distribution.addMonthPoint(cal.get(Calendar.MONTH));
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

            // 2. insert STATS node
            Node statsNode = graphDB.createNode();
            statsNode.addLabel(DynamicLabel.label("facebookStats"));
            statsNode.setProperty("stats", distribution.toJSONString());

            tx.success();
        }


    }
}
