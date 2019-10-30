package is.hi.hbvproject.controller;

import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class MessageController {
    MessageService messageService;
    UserService userService;
    RideServices rideService;
    AuthenticationService authenticationService;

    @Autowired
    public MessageController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }


    @RequestMapping(
            value = "/users",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<User> getUsers() {
        return service.findAll();
    }

    @RequestMapping(
            value = "/users/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public Optional<User> getUserById(@PathVariable long id) {
        Optional<User> user = service.findById(id);
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    @RequestMapping(
            value = "/messages",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public Message createMessage(@RequestBody JSONObject body) {
        long senderId = body.getLong("senderId");
        Optional<User> sender = userService.findById(senderId);
        if (!sender.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found");
        }
        long recipientId = body.getLong("recipientId");
        Optional<User> recipient = userService.findById(recipientId);
        if (!recipient.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recpient not found");
        }
        String messageBody = body.getString("messageBody");
        Message message = new Message(body, recipient, sender);
        messageService.save(message);
        return message;
    }

    public Optional<Message> findMessageById(@PathVariable long id) {
        Optional<Message> message = messageService.findById(id);
        if (!message.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        return message;
    }

    public List<Message> findConversation(@PathVariable User sender, User recipient, Ride ride) {
        List<Message> conversation = messageService.findConversation(sender, recipient, ride);
        if (!conversation.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found");
        }
        return conversation;
    }

    public List<Message> findSentMessage(@PathVariable User sender) {
        List<Message> message = messageService.findSent(sender);
        if (!message.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender message not found");
        }
        return message;
    }

    public List<Message> findRecievedMessage(@PathVariable User recipient) {
        List<Message> message = messageService.findRecieved(recipient);
        if (!message.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient message not found");
        }
        return message;
    }

}
