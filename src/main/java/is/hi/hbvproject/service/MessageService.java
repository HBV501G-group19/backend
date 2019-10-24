package is.hi.hbvproject.service;

import is.hi.hbvproject.persistence.entities.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    void delete(Message message);

    void deleteAll();

    long count();

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean Message(String message);

    List<Message> findAll();

    List<Message> findAllById(Iterable<Long> ids);

    Optional<Message> findById(Long id);

    Message save(Message message);

    void deleteAll(Iterable<? extends Message> messages);
}
