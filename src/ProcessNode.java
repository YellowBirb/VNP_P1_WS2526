import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessNode<T> {

    private int id;
    private int[] neighbours;
    private int[] weights;
    private int[] routing;
    private String contentID;
    private T data;
    private SystemGraph system;
    List<WaitingMessage<T>> processingQueue;

    public ProcessNode(int id, int[] neighbours, int[] weights, String contentID, T data, SystemGraph system) {
        this.id = id;
        this.neighbours = neighbours;
        this.weights = weights;
        this.routing = new int[weights.length];
        Arrays.fill(routing, -1);
        this.contentID = contentID;
        this.data = data;
        this.system = system;
        this.processingQueue = new ArrayList<>();
    }

    public void receive(Message<T> message) {
        // System.out.println("["+getSystem().getTime()+"] process " + getId() + " has received a message");
        int processingTime = 5;

        // Falls eine Response erstellt werden muss
        if (message.getTarget() == getId()) {
            processingTime += 5;
        }

        // +1 zu processingTime da in SystemGraph.step() zur selben millisekunde, in der eine Nachricht erhalten wird, bereits ein step() ausgeführt wird
        getProcessingQueue().add(new WaitingMessage<>(message, processingTime+1));
    }

    // Hier wir die Bearbeitungszeit der Nachrichten simuliert
    // Es wird angenommen, das Nachrichten parallel bearbeitet werden, nicht sequentiell
    public void step() {
        List<WaitingMessage<T>> remove = new ArrayList<>();
        for (WaitingMessage<T> waitingMessage : getProcessingQueue()) {

            waitingMessage.setWaitTime(waitingMessage.getWaitTime()-1);
            if (waitingMessage.getWaitTime() <= 0) {
                process(waitingMessage.getMessage());
                remove.add(waitingMessage);
            }
        }
        for (WaitingMessage<T> msg : remove) {
            getProcessingQueue().remove(msg);
        }
    }

    private void process(Message<T> message) {

        // System.out.println("["+getSystem().getTime()+"] process " + getId() + " has processed a message");

        // TTL reduzieren
        message.setTtl(message.getTtl()-1);

        // Message ist eine Response
        if (message.getTarget() >= 0) {
            // System.out.println("["+getSystem().getTime()+"] Process " + getId() + " has determined Message to be a response");

            // Falls Response auf eigene Request
            if (message.getTarget() == getId()) {
                System.out.println("["+getSystem().getTime()+"] Process "+getId()+" Received Response originating from Process "+message.getSource()+" via Process "+message.getSender()+" for Content-Identifier " + message.getContentID() + " with contents " + message.getData() + " at time " + getSystem().getTime() + "ms");
            }
            // Response weiterleiten
            else {
                // nur weiterleiten, wenn
                if (message.getTtl() > 0) {
                    // System.out.println("["+getSystem().getTime()+"] Process " + getId() + " is relaying a response received from Process "+ message.getSender() );
                    message.setSender(getId());
                    message.setReceiver(getRouting()[message.getTarget()]);
                    send(message);
                }
            }
        }

        // Message ist eine Request
        else {

            // Falls noch keine Nachricht von dieser Source: routing speichern
            // Falls bereits gespeichert: nur weitermachen wenn routing übereinstimmt
            if (!checkRouting(message.getSource(), message.getSender())) {
                // System.out.println("["+getSystem().getTime()+"] Process "+getId()+" not relaying request received from Process "+ message.getSender() +", routing escape");
                return;
            }

            // System.out.println("["+getSystem().getTime()+"] Process " + getId() + " has determined Message to be a request");

            // Falls in der Request gesuchte Daten im Prozess vorhanden sind
            if (message.getContentID().equals(getContentID())) {

                // System.out.println("["+getSystem().getTime()+"] Process " + getId() + " is sending a response");

                // Schicke Reponse
                send(new Message<>(
                                getId(),
                                message.getSource(),
                                getId(),
                                getRouting()[message.getSource()],
                                getContentID(),
                                getData()
                        )
                );
            }

            // Request weiterleiten
            else {
                // nur weiterleiten, wenn
                if (message.getTtl() > 0) {

                    // System.out.println("["+getSystem().getTime()+"] Process " + getId() + " is relaying a request received from Process "+ message.getSender());

                    // an jeden Nachbar weiterleiten
                    for (int i = 0; i < getNeighbours().length; i++) {
                        if (message.getSender() != getNeighbours()[i]) {
                            send(new Message<>(
                                    message.getSource(),
                                    message.getTarget(),
                                    getId(),
                                    getNeighbours()[i],
                                    message.getContentID(),
                                    message.getData()
                            ));
                        }
                    }
                }
            }
        }
    }

    private boolean checkRouting(int source, int sender) {
        if (getRouting()[source] == -1) {
            getRouting()[source] = sender;
            return true;
        }
        else {
            return getRouting()[source] == sender;
        }
    }

    public void send(Message<T> message) {
        // System.out.println("["+getSystem().getTime()+"] Process "+getId()+" sends message to process " + message.getReceiver());
        getSystem().send(message, getWeights()[message.getReceiver()]);
    }



    public void request(String contentID) {
        System.out.println("["+getSystem().getTime()+"] Process "+getId()+" sends request for contentID " + contentID);

        if (contentID.equals(getContentID())) {
            System.out.println("["+getSystem().getTime()+"] Process "+getId()+" Received Response originating from Process "+getId()+" for Content-Identifier " + contentID + " with contents " + getData() + " at time " + getSystem().getTime() + "ms");
        }
        else {
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
    }

    public String toString() {
        String dataString = getData() == null ? "null" : getData().toString();
        return "\n[\n\"id\": "+getId()+ ", \n\"neighbours\": "+ Arrays.toString(getNeighbours())+", \n\"weights\": "+ Arrays.toString(getWeights())+", \n\"routing\": "+ Arrays.toString(getRouting())+", \n\"contentID\": "+ getContentID() +", \n\"data\": "+ dataString +", \n\"system\": "+ "system" +", \n\"processingQueue\": "+ getProcessingQueue().toString()+", \n]";
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

    public SystemGraph getSystem() {
        return system;
    }

    public void setSystem(SystemGraph system) {
        this.system = system;
    }

    public List<WaitingMessage<T>> getProcessingQueue() {
        return processingQueue;
    }

    public void setProcessingQueue(List<WaitingMessage<T>> processingQueue) {
        this.processingQueue = processingQueue;
    }
}