public class DiscreteEventSimulator {

    public static void main(String[] args) {

        SystemGraph system = new SystemGraph();

        system.init("./src/test-graph.txt", "./src/test-data.txt");

        // System.out.println(system);

        for (int i = 0; i < 10000; i++) {
            // System.out.println("test" + i);
            system.step();
            // System.out.println("-");
        }
    }
}