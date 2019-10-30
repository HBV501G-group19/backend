package is.hi.hbvproject.persistence.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "messages") // "user" is a reserved word in postgres
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

    private Ride ride;

    public Message() {
    }

    public Message(String body, User recipient, User sender, Ride ride) {
        this.body = body;
        this.recipient = recipient;
        this.sender = sender;
        this.ride = ride;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
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

    public Ride getBody() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

}
