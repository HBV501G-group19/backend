package is.hi.hbvproject.persistence.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="messages") // "user" is a reserved word in postgres
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

    public Message() {}
    public Message(String body, User recipient, User sender) {
        this.body = body;
        this.recipient = recipient;
        this.sender = sender;
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

    public String getBody() { return body; }

    public void setBody(String body) { this.body = body; }
}
