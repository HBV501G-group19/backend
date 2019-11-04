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

    @Query("select m from Messages where m.sender = senderId" +
            "and m.recipient = recipientId" +
            "and m.ride = rideId")
    public List<Message> findConversation(
            @Param("senderId") long senderId,
            @Param("recipientId") long recipientId,
            @Param("rideId") long rideId
    );

    @Query("select m from Messages where m.id = id")
    public Optional<Message> findMessage( @Param("id") long id );

    @Query("select s from Messages where s.id = id")
    public List<Message> findSent( @Param("id") long sentId );

    @Query("select r from Messages where r.id = id")
    public List<Message> findRecieved( @Param("id") long recievedId );

}
