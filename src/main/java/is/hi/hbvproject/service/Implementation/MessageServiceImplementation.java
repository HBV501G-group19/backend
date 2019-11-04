package is.hi.hbvproject.service.Implementation;


import is.hi.hbvproject.persistence.entities.Message;
import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import is.hi.hbvproject.service.MessageService;
import is.hi.hbvproject.persistence.repositories.RideRepository;
import is.hi.hbvproject.persistence.repositories.UserRepository;
import is.hi.hbvproject.persistence.repositories.MessageRepository;


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
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    ;

    @Override
    public List<Message> findConversation(long sender, long recipient, long ride)
    {
        return messageRepository.findConversation(sender, recipient, ride);
    }

    @Override
    public Optional<Message> findMessage(long id)
    {
        return messageRepository.findMessageById(id);
    }

    @Override
    public List<Message> findSent(User sender)
    {
        return messageRepository.findBySender(sender);
    }

    @Override
    public List<Message> findRecieved(User recipient)
    {
        return messageRepository.findByRecipient(recipient);
    }

}
