import org.apache.flink.graph.Vertex;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {

    public static void main(String[] args) {
        Vertex<Long, String> v = new Vertex<>(1L, "foo");
       // Vertex<Long, NullValue> v2 = new Vertex<>();

        System.out.println(v.getValue());

        GraphDatabaseService graphDB = new GraphDatabaseFactory()
                .newEmbeddedDatabase("/home/cristiprg/neo4jDB");

        try(Transaction tx = graphDB.beginTx()) {

            Label label = DynamicLabel.label("Friendship");
            try (ResourceIterator<Node> friendships =
                         graphDB.findNodes(label)) {

                while (friendships.hasNext()) {
                    System.out.println(friendships.next().getProperty("timestamp"));
                }
            }

            tx.success();
        }
    }
}
