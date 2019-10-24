package is.hi.hbvproject.service.Implementation;

import is.hi.hbvproject.persistence.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImplementation implements MessageService {

    MessageRepository messageRepository;

    @Autowired
    public MessageServiceImplementation(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    ;

    @Override
    public void deleteAll() {
        messageRepository.deleteAll();
    }

    ;

    @Override
    public long count() {
        return messageRepository.count();
    }

    ;

    @Override
    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    ;

    @Override
    public boolean existsById(Long id) {
        return messageRepository.existsById(id);
    }

    ;

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    ;

    @Override
    public List<Message> findAllById(Iterable<Long> ids) {
        return messageRepository.findAllById(ids);
    }

    ;

    @Override
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    ;

    @Override
    public Message save(Message message) {
        return messageRepository.save(Message);
    }

    ;

    @Override
    public void deleteAll(Iterable<? extends Message> messages) {
        messageRepository.deleteAll(messages);
    }

    ;
}
