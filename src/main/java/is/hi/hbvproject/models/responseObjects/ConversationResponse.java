package is.hi.hbvproject.models.responseObjects;

import is.hi.hbvproject.persistence.entities.Message;

import java.util.List;

public class ConversationResponse {
    private final long user;
    private final List<Message> rideConversation;

    public ConversationResponse(long user, List<Message> rideConversation) {
        this.user = user;
        this.rideConversation = rideConversation;
    }

    public long getUserId() {
        return user;
    }

    public List<Message> getMessages() {
        return rideConversation;
    }



}
