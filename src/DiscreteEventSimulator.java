import java.util.Arrays;
import java.util.Random;

public class DiscreteEventSimulator {

    public static void main(String[] args) {

        SystemGraph system = new SystemGraph();

        system.init("./src/test-graph.txt", "./src/test-data.txt");

        boolean nurEineRequest = false;

        if (nurEineRequest) {
            int[] arr = new int[system.getNodes().size()];
            Arrays.fill(arr, Integer.MAX_VALUE);
            arr[new Random().nextInt(system.getNodes().size())] = 10;
            system.setStartRequestTimes(arr);
        }

        // System.out.println(system);

        for (int i = 0; i < 10000; i++) {
            system.step();
        }
    }
}