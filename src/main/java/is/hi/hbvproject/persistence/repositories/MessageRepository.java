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

    @Query(value = "select m from Message m where m.sender = senderId" +
            "and m.recipient = recipientId" +
            "and m.ride = rideId")
    public List<Message> findConversation(
            @Param("senderId") long senderId,
            @Param("recipientId") long recipientId,
            @Param("rideId") long rideId
    );

    @Query(value = "select m from Message m where m.id = id")
    public Optional<Message> findMessage( @Param("id") long id );

    @Query(value = "select s from Message m where s.id = id")
    public List<Message> findSent( @Param("id") long sentId );

    @Query(value = "select r from Message m where r.id = id")
    public List<Message> findRecieved( @Param("id") long recievedId );

}
