package ar.edu.ips.aus.seminario2.sampleproject;

public class Message<T> {
    public enum MessageType {
        PLAYER_DATA,
        GAME_DATA,
        GAME_STATUS,
    }

    private MessageType type;
    private T payload;

    public Message(MessageType type, T payload) {
        this.type = type;
        this.payload = payload;
    }

    public MessageType getType() {
        return type;
    }

    public T getPayload() {
        return payload;
    }
}
