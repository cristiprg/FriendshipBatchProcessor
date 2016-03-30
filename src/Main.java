import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {

    public static void main(String[] args) {

        GraphDatabaseService graphDB = null;
        try{
            // keep in mind that the neo4j-service has to be stopped before running this thing.
            graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("/home/cristiprg/neo4jDB");
            GraphStatistics.insertTimeDistribution(graphDB);
        }finally {
            // without explicit shutdown, the service doesn't start anymore and shows no error (at least on Ubuntu)
            if (graphDB != null)
                graphDB.shutdown();
        }
    }
}
