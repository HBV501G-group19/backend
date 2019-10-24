package is.hi.hbvproject.persistence.repositories;

import is.hi.hbvproject.persistence.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    public Message save(Message message);

    public Optional<Message> findById(long id);

    public Message deleteById(long id);

    public boolean existsById(long id);

    public boolean existsByUsername(String username);

    public List<Message> findAll();

    public List<Message> findAllById(Iterable<Long> ids);

    public void deleteAll(Iterable<? extends Messages> messages);

    public void deleteAll();
}
