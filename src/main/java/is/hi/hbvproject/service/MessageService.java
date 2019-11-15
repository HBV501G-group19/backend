package is.hi.hbvproject.service;

import is.hi.hbvproject.persistence.entities.Message;
import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message save(Message message);

    void delete(Message message);

    void deleteAll();

    List<Message> findConversation(long sender, long recipient, long ride);

    Optional<Message> findMessage(long id);

    List<Message> findSent(User sender);

    List<Message> findReceived(User recipient);
}
