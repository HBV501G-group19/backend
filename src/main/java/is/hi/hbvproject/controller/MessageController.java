package is.hi.hbvproject.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class MessageController {
    MessageService messageService;
    UserService userService;
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
        if(!sender.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found");
        }
        long recipientId = body.getLong("recipientId");
        Optional<User> recipient = userService.findById(recipientId);
        if(!recipient.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recpient not found");
        }
        String messageBody = body.getString("messageBody");
        Message message = new Message(body,recipient,sender);
        messageService.save(message);
        return message;
    }
}
