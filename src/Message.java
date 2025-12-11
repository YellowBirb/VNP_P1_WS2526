public class Message<T> {

    private int ttl;
    private ProcessNode source;
    private ProcessNode target;
    private ProcessNode sender;
    private ProcessNode receiver;
    private String contentID;
    private T data;

    public Message(ProcessNode source, ProcessNode target, ProcessNode sender, ProcessNode receiver, String contentID, T data) {
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

    public ProcessNode getSource() {
        return source;
    }

    public void setSource(ProcessNode source) {
        this.source = source;
    }

    public ProcessNode getTarget() {
        return target;
    }

    public void setTarget(ProcessNode target) {
        this.target = target;
    }

    public ProcessNode getSender() {
        return sender;
    }

    public void setSender(ProcessNode sender) {
        this.sender = sender;
    }

    public ProcessNode getReceiver() {
        return receiver;
    }

    public void setReceiver(ProcessNode receiver) {
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
