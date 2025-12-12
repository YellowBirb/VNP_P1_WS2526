import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SystemGraph {

    private int time;
    private List<ProcessNode<String>> nodes;
    private List<WaitingMessage<String>> communicationLatencyQueue;
    private int[] startRequestTimes;
    private List<String> contentIDs;

    public SystemGraph() {
        this.time = 0;
        this.nodes = new ArrayList<>();
        this.communicationLatencyQueue = new ArrayList<>();
        this.startRequestTimes = new int[0];
        this.contentIDs = new ArrayList<>();
    }

    public void init(String filePathGraph, String filePathData) {
        setTime(0);

        List<String> graph = new ArrayList<>();
        List<String> data = new ArrayList<>();

        // Lese Zeilen aus Graph-Datei
        try (BufferedReader br1 = new BufferedReader(new FileReader(filePathGraph))) {
            String line1;
            while ((line1 = br1.readLine()) != null) {
                graph.add(line1);
            }
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }

        // Lese Zeilen aus Data-Datei
        try (BufferedReader br2 = new BufferedReader(new FileReader(filePathData))) {
            String line2;
            while ((line2 = br2.readLine()) != null) {
                data.add(line2);
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }

        // Zeilen aus Graph-Datei auswerten
        for (int i = 0; i < graph.size(); i++) {
            String[] edges = graph.get(i).split(";");

            int[] neighbours = new int[edges.length];
            int[] weights = new int[graph.size()];
            Arrays.fill(weights, Integer.MAX_VALUE);
            for (int j = 0; j < edges.length; j++) {
                String[] values = edges[j].split(",");
                neighbours[j] = Integer.parseInt(values[0]);
                weights[neighbours[j]] = Integer.parseInt(values[1]);
            }
            getNodes().add(new ProcessNode<>(i, neighbours, weights, null, null, this));
        }

        // Zeilen aus Data-Datei auswerten
        for (int k = 0; k < data.size(); k++) {
            String dataLine = data.get(k).replace(";", "");
            String[] dataObject = dataLine.split(":");
            ProcessNode<String> node = getNodes().get(k);
            node.setContentID(dataObject[0]);
            getContentIDs().add(dataObject[0]);
            node.setData(dataObject[1]);
        }

        // Zufällige Requests
        setStartRequestTimes(new int[getNodes().size()]);
        for (int i = 0; i < getStartRequestTimes().length; i++) {
            getStartRequestTimes()[i] = new Random().nextInt(5000) + 1;
        }
    }

    public void step() {

        // Kommunikationslatenz abwarten/simulieren
        List<WaitingMessage<String>> remove = new ArrayList<>();
        for (WaitingMessage<String> msg : getCommunicationLatencyQueue()) {
            msg.setWaitTime(msg.getWaitTime()-1);
            if (msg.getWaitTime() <= 0) {
                getNodes().get(msg.getMessage().getReceiver()).receive(msg.getMessage());
                remove.add(msg);
            }
        }

        for (WaitingMessage<String> waitingMessage : remove) {
            getCommunicationLatencyQueue().remove(waitingMessage);
        }

        // Zufällige Requests ausführen
        for (int i = 0; i < getStartRequestTimes().length; i++) {
            if (getTime() == getStartRequestTimes()[i]) {
                getNodes().get(i).request(getContentIDs().get(new Random().nextInt(getContentIDs().size())));
            }
        }

        // Bearbeitungszeit simulieren
        for (ProcessNode<String> p : getNodes()) {
            p.step();
        }

        setTime(getTime()+1);
    }

    // Message mit Latenz weiterleiten
    public <T> void send(Message<T> message, int latency) {
        // System.out.println("Adding message from process " + message.getSender() + " to process " + message.getReceiver() + " with latency "+latency+" to latency queue");
        // System.out.println("-> Message: " + message);
        Message<String> stringMessage = new Message<>(message.getSource(), message.getTarget(), message.getSender(), message.getReceiver(), message.getContentID(), String.valueOf(message.getData()));
        getCommunicationLatencyQueue().add(new WaitingMessage<>(stringMessage, latency));
    }

    public String toString() {
        return "[\"time\": "+ getTime() +", \"nodes\": "+ getNodes().toString() +", \"communicationLatencyQueue\": "+ getCommunicationLatencyQueue().toString() +", \"startRequestTimes\": "+ Arrays.toString(getStartRequestTimes())+", \"ContentIDs\": "+ getContentIDs().toString()+"]";
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<ProcessNode<String>> getNodes() {
        return nodes;
    }

    public void setNodes(List<ProcessNode<String>> nodes) {
        this.nodes = nodes;
    }

    public List<WaitingMessage<String>> getCommunicationLatencyQueue() {
        return communicationLatencyQueue;
    }

    public void setCommunicationLatencyQueue(List<WaitingMessage<String>> communicationLatencyQueue) {
        this.communicationLatencyQueue = communicationLatencyQueue;
    }

    public int[] getStartRequestTimes() {
        return startRequestTimes;
    }

    public void setStartRequestTimes(int[] startRequestTimes) {
        this.startRequestTimes = startRequestTimes;
    }

    public List<String> getContentIDs() {
        return contentIDs;
    }

    public void setContentIDs(List<String> contentIDs) {
        this.contentIDs = contentIDs;
    }
}
