package is.hi.hbvproject.controller;

import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.persistence.entities.Message;
import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.service.UserService;
import is.hi.hbvproject.service.RideService;
import is.hi.hbvproject.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import kong.unirest.json.JSONObject;

@RestController
public class MessageController {
    MessageService messageService;
    UserService userService;
    RideService rideService;

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
        return userService.findAll();
    }

    @RequestMapping(
            value = "/users/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public Optional<User> getUserById(@PathVariable long id) {
        Optional<User> user = userService.findById(id);
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
    public Message createMessage(@RequestBody String body) {
        JSONObject json = new JSONObject(body);
        long senderId = json.getLong("senderId");
        Optional<User> sender = userService.findById(senderId);
        if (!sender.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found");
        }
        long recipientId = json.getLong("recipientId");
        Optional<User> recipient = userService.findById(recipientId);
        if (!recipient.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recpient not found");
        }
        long rideId = json.getLong("rideId");
        Optional<Ride> ride = rideService.findById(rideId);
        if (!ride.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found");
        }
        String messageBody = json.getString("messageBody");
        if(messageBody.trim().length() == 0)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot send empty message");
        }

        Message message = new Message(messageBody, recipient.get(), sender.get(), ride.get());
        messageService.save(message);
        return message;
    }

    @RequestMapping(
            value = "/message/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public Optional<Message> findMessageById(@PathVariable long id) {
        Optional<Message> message = messageService.findMessage(id);
        if (!message.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        return message;
    }

    @RequestMapping(
            value = "/messages/conversation",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public List<Message> findConversation(@RequestBody String body) {
        JSONObject json = new JSONObject(body);
        Long senderId = json.getLong("senderId");

        if(!userService.existsById(senderId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SenderId not found");
        }

        Long recipientId = json.getLong("recipientId");
        if(!userService.existsById(recipientId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RecipientId not found");
        }

        Long rideId = json.getLong("senderId");
        if(!rideService.existsById(rideId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rideId not found");
        }

        List<Message> conversation = messageService.findConversation(senderId, recipientId, rideId);
        return conversation;
    }

    @RequestMapping(
            value = "/users/{id}/sent",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<Message> findSentMessage(@PathVariable long sender) {
        List<Message> message = messageService.findSent(sender);
        return message;
    }

    @RequestMapping(
            value = "/users/{id}/recieved",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<Message> findRecievedMessage(@PathVariable long recipient) {
        List<Message> message = messageService.findRecieved(recipient);
        return message;
    }

}
