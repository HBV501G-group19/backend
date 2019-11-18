package is.hi.hbvproject.persistence.entities;

import is.hi.hbvproject.persistence.entities.User;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonGetter;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String body;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User recipient;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Ride ride;
    @CreationTimestamp
    private Timestamp created;

    private int conversationId;

    public Message() {
    }

    public Message(String body, User recipient, User sender, Ride ride) {
        this.body = body;
        this.recipient = recipient;
        this.sender = sender;
        this.ride = ride;

        // 'unique' identity fyrir convoið
        // 32bit integer er allt of lítið fyrir alvöru app
        long rideId = ride.getId();
        long senderId = sender.getId();
        long recipientId = recipient.getId();
        long firstId = Math.max(senderId, recipientId);
        long secondId = Math.min(senderId, recipientId);
        this.conversationId = (
            "r" + rideId +
            "u" + firstId +
            "u" + secondId
        ).hashCode();
    }

    public int getConversationId() {
        return conversationId;
    }

    public User getRecipient() {
        return recipient;
    }

    @JsonGetter("recipient")
    public long getRecipientId() {
        return recipient.getId();
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    @JsonGetter("sender")
    public long getSenderId() {
        return sender.getId();
    }
    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Ride getRide() {
        return ride;
    }

    @JsonGetter("ride")
    public long getRideId() {
        return ride.getId();
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

}
