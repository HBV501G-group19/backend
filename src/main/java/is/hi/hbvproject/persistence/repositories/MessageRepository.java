package is.hi.hbvproject.persistence.repositories;

import is.hi.hbvproject.persistence.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import is.hi.hbvproject.persistence.entities.Message;
import is.hi.hbvproject.persistence.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    public Message save(Message message);

    public void delete(Message message);

    public void deleteAll();

    long count();

    public Optional<Message> findById(long id);
    //select sender as s, recipient as rec, ride as r
    @Query(value = "select m from Message m " +
            "where m.sender.id = :senderId " +
            "and m.recipient.id = :recipientId " +
            "or m.sender.id = :recipientId " +
            "and m.recipient.id = :senderId " +
            "and m.ride.id = :rideId " +
            "order by m.created")
    public List<Message> findConversation(
            @Param("senderId") long senderId,
            @Param("recipientId") long recipientId,
            @Param("rideId") long rideId
    );

    public Optional<Message> findMessageById(long id);

    public List<Message> findBySender(User sender);

    public List<Message> findByRecipient(User recipient );

}
