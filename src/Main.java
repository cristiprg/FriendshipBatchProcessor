import org.apache.flink.graph.Vertex;

public class Main {

    public static void main(String[] args) {
        Vertex<Long, String> v = new Vertex<>(1L, "foo");
       // Vertex<Long, NullValue> v2 = new Vertex<>();

        System.out.println(v.getValue());
    }
}
