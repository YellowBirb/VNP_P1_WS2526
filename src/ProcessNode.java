import java.util.ArrayList;
import java.util.List;

public class ProcessNode<T> {

    private int id;
    private int[] neighbours;
    private int[] weights;
    private int[] routing;
    private String contentID;
    private T data;
    private SystemGraph<T> system;
    List<WaitingMessage<T>> processingQueue;

    public ProcessNode(int id, int[] neighbours, int[] weights, String contentID, T data, SystemGraph<T> system) {
        this.id = id;
        this.neighbours = neighbours;
        this.weights = weights;
        this.routing = new int[neighbours.length];
        this.contentID = contentID;
        this.data = data;
        this.system = system;
        this.processingQueue = new ArrayList<>();
    }

    public void receive(Message<T> message) {
        int processingTime = 5;

        // Falls message eine Response ist
        if (message.getTarget() >= 0) {
            processingTime += 5;
        }

        // +1 zu processingTime da in SystemGraph.step() zur selben millisekunde, in der eine Nachricht erhalten wird, bereits ein step() ausgeführt wird
        processingQueue.add(new WaitingMessage<>(message, processingTime+1));
    }

    // Hier wir die Bearbeitungszeit der Nachrichten simuliert
    // Es wird angenommen, das Nachrichten parallel bearbeitet werden, nicht sequentiell
    public void step() {
        for (WaitingMessage<T> waitingMessage : processingQueue) {
            waitingMessage.setWaitTime(waitingMessage.getWaitTime()-1);
            if (waitingMessage.getWaitTime() <= 0) {
                process(waitingMessage.getMessage());
                processingQueue.remove(waitingMessage);
            }
        }
    }

    private void process(Message<T> message) {
        message.setTtl(message.getTtl()-1);

        // Falls Nachricht eine Response bzw. hat ein Target
        if (message.getTarget() >= 0 && message.getTtl() > 0) {
            if (message.getTarget() == getId()) {
                System.out.println("Node "+getId()+" Received Response for Content-Identifier " + message.getContentID() + " with contents " + message.getData() + " at time " + getSystem().getTime() + "ms");
            }
            else {
                message.setReceiver(getRouting()[message.getTarget()]);
                send(message);
            }
        }
        // Falls Nachricht eine Request ist und Daten vorhanden sind
        else if (message.getSource() >=0 && message.getContentID().equals(getContentID())) {
            message.setReceiver(getWeights()[getRouting()[message.getSource()]]);
            send(new Message<>(
                            -1,
                            message.getSource(),
                            getId(),
                            getRouting()[message.getSource()],
                            getContentID(),
                            getData()
                    )
            );
        }
        // Nachricht an Nachbarn weitersenden
        else if (message.getTtl() > 0) {
            for (int i = 0; i < getNeighbours().length; i++) {
                if (message.getSender() != getNeighbours()[i]) {
                    message.setReceiver(getWeights()[getNeighbours()[i]]);
                    send(message);
                }
            }
        }
    }

    public void send(Message<T> message) {
        getSystem().send(message, getWeights()[message.getReceiver()]);
    }



    public void request(String contentID) {
        for (int i = 0; i < getNeighbours().length; i++) {
            send(new Message<>(
                            getId(),
                            -1,
                            getId(),
                            getNeighbours()[i],
                            contentID,
                            null
                    )
            );
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(int[] neighbours) {
        this.neighbours = neighbours;
    }

    public int[] getWeights() {
        return weights;
    }

    public void setWeights(int[] weights) {
        this.weights = weights;
    }

    public int[] getRouting() {
        return routing;
    }

    public void setRouting(int[] routing) {
        this.routing = routing;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public SystemGraph<T> getSystem() {
        return system;
    }

    public void setSystem(SystemGraph<T> system) {
        this.system = system;
    }
}