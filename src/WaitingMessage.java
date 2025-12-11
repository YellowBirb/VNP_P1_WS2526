public class WaitingMessage {

    private Message message;
    private int waitTime;


    public WaitingMessage(Message message, int waitTime) {
        this.message = message;
        this.waitTime = waitTime;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
