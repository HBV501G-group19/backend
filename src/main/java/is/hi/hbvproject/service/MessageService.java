package is.hi.hbvproject.service;

import is.hi.hbvproject.persistence.entities.Message;
import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message save(Message message);

    void delete(Message);

    void deleteAll();

    List<Messages> findConversation(User sender, User recipient, Ride ride);

    Optional<Message> findMessage(Long id);

    List<Message> findMessage(Long senderId);

    List<Messages> findRecieved(long recipientId);
}
