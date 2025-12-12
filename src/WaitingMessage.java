public class WaitingMessage<T> {

    private Message<T> message;
    private int waitTime;


    public WaitingMessage(Message<T> message, int waitTime) {
        this.message = message;
        this.waitTime = waitTime;
    }

    public String toString()
    {
        return "\n[\n\"message\": "+message+", \n\"waitTime\": "+waitTime+"\n]";
    }

    public Message<T> getMessage() {
        return message;
    }

    public void setMessage(Message<T> message) {
        this.message = message;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
