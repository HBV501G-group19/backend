package is.hi.hbvproject.models.responseObjects;

import is.hi.hbvproject.persistence.entities.Message;

import java.util.List;

public class ConversationResponse {
    private int conversationId;
    private final long[] users = new long[2];
    private final long ride;
    private final boolean isDriver;
    private final List<Message> rideConversation;

    public ConversationResponse(long user1, long user2, long ride, boolean isDriver, List<Message> rideConversation) {
        this.users[0] = user1;
        this.users[1] = user2;
        this.ride = ride;
        this.isDriver = isDriver;
        this.rideConversation = rideConversation;

        long firstId = Math.max(user1, user2);
        long secondId = Math.min(user1, user2);
        this.conversationId = (
            "r" + ride +
            "u" + firstId +
            "u" + secondId
        ).hashCode();
    }

    public long getConversationId() {
        return conversationId;
    }

    public long[] getUserIds() {
        return users;
    }

    public long getRideId() {
        return ride;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public List<Message> getMessages() {
        return rideConversation;
    }
}
