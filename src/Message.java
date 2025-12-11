public class Message<T> {

    private int ttl;
    private int source;
    private int target;
    private int sender;
    private int receiver;
    private String contentID;
    private T data;

    public Message(int source, int target, int sender, int receiver, String contentID, T data) {
        this.ttl = 24;
        this.source = source;
        this.target = target;
        this.sender = sender;
        this.receiver = receiver;
        this.contentID = contentID;
        this.data = data;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
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
}
