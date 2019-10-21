package is.hi.hbvproject.service.Implementation;
import is.hi.hbvproject.service.UserService;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.persistence.repositories.UserRepository;

@Service
public class MessageServiceImplementation implements MessageService {

    MessageRepository messageRepository;

    @Autowired
    public MessageServiceImplementation(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void	delete(Message message) {
        messageRepository.delete(message);
    };

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    };

    @Override
    public long count() {
        return userRepository.count();
    };

    @Override
    public void	deleteById(Long id) {
        userRepository.deleteById(id);
    };

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    };

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    };

    @Override
    public List<User> findAllById(Iterable<Long> ids) {
        return userRepository.findAllById(ids);
    };

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    };

    @Override
    public User save(User user) {
        return userRepository.save(user);
    };

    @Override
    public void deleteAll(Iterable<? extends User> users) {
        userRepository.deleteAll(users);
    };
}
