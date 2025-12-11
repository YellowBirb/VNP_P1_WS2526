public class DiscreteEventSimulator {

    public static void main(String[] args) {

        SystemGraph system = new SystemGraph();

        system.init("");

        for (int i = 0; i < 10000; i++) {
            system.step();
        }
    }
}