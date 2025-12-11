import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SystemGraph<T> {

    private int time;
    private List<ProcessNode<T>> nodes;
    private List<WaitingMessage<T>> communicationLatencyQueue;
    private int[] startRequestTimes;

    public SystemGraph() {
        this.time = 0;
        this.nodes = new ArrayList<>();
        this.communicationLatencyQueue = new ArrayList<>();
        this.startRequestTimes = new int[0];
    }

    public void init(String filePath) {
        this.time = 0;

        // parse text files and generate Nodes from them

        this.startRequestTimes = new int[nodes.size()];
        for (int i = 0; i < startRequestTimes.length; i++) {
            startRequestTimes[i] = new Random().nextInt(5000) + 1;
        }
    }

    public void step() {
        for (WaitingMessage<T> msg : communicationLatencyQueue) {
            msg.setWaitTime(msg.getWaitTime()-1);
            if (msg.getWaitTime() <= 0) {
                nodes.get(msg.getMessage().getReceiver()).receive(msg.getMessage());
                communicationLatencyQueue.remove(msg);
            }
        }

        for (ProcessNode<T> p : nodes) {
            p.step();
        }
    }

    public void send(Message<T> message, int latency) {
        int receiver = message.getReceiver();
        communicationLatencyQueue.add(new WaitingMessage<>(message, latency));
    }



    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<ProcessNode<T>> getNodes() {
        return nodes;
    }

    public void setNodes(List<ProcessNode<T>> nodes) {
        this.nodes = nodes;
    }

    public List<WaitingMessage<T>> getCommunicationLatencyQueue() {
        return communicationLatencyQueue;
    }

    public void setCommunicationLatencyQueue(List<WaitingMessage<T>> communicationLatencyQueue) {
        this.communicationLatencyQueue = communicationLatencyQueue;
    }

    public int[] getStartRequestTimes() {
        return startRequestTimes;
    }

    public void setStartRequestTimes(int[] startRequestTimes) {
        this.startRequestTimes = startRequestTimes;
    }
}
