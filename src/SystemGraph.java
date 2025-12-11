import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SystemGraph {

    private int time;
    private List<ProcessNode> nodes;
    private List<WaitingMessage> communicationLatencyQueue;
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
        for (WaitingMessage msg : communicationLatencyQueue) {
            msg.setWaitTime(msg.getWaitTime()-1);
            if (msg.getWaitTime() <= 0) {
                nodes.get(msg.getMessage().getReceiver()).receive(msg.getMessage());
                communicationLatencyQueue.remove(msg);
            }
        }

        for (ProcessNode p : nodes) {
            p.step();
        }
    }

    public <T> void send(Message<T> message, int latency) {
        int receiver = message.getReceiver();
        communicationLatencyQueue.add(new WaitingMessage(message, latency));
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<ProcessNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ProcessNode> nodes) {
        this.nodes = nodes;
    }

    public List<WaitingMessage> getCommunicationLatencyQueue() {
        return communicationLatencyQueue;
    }

    public void setCommunicationLatencyQueue(List<WaitingMessage> communicationLatencyQueue) {
        this.communicationLatencyQueue = communicationLatencyQueue;
    }

    public int[] getStartRequestTimes() {
        return startRequestTimes;
    }

    public void setStartRequestTimes(int[] startRequestTimes) {
        this.startRequestTimes = startRequestTimes;
    }
}
