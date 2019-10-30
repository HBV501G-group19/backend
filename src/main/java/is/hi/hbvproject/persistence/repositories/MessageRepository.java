package is.hi.hbvproject.persistence.repositories;

import is.hi.hbvproject.persistence.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    public Message save(Message message);

    public void delete(Message);

    public void deleteAll();

    long count();

    public Optional<Message> findById(long id);

    public List<Message> findSentMessages(long senderId);

    public List<Message> findRecievedMessage(long recipientId);

    public List<Message> findConversation(long senderId, long recipientId, long rideId);
    
}
