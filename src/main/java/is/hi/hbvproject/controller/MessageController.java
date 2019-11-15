package is.hi.hbvproject.controller;

import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.persistence.entities.Message;
import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.service.UserService;
import is.hi.hbvproject.service.RideService;
import is.hi.hbvproject.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import kong.unirest.json.JSONObject;

@RestController
public class MessageController {
    MessageService messageService;
    UserService userService;
    RideService rideService;

    @Autowired
    public MessageController(UserService userService, MessageService messageService, RideService rideService) {
        this.userService = userService;
        this.messageService = messageService;
        this.rideService = rideService;
    }


    @RequestMapping(
            value = "/messages/create",
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot send empty message");
        }

        Message message = new Message(messageBody, recipient.get(), sender.get(), ride.get());
        messageService.save(message);
        return message;
    }

    @RequestMapping(
            value = "/messages/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public Optional<Message> getMessageById(@PathVariable long id) {
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
    public List<Message> getConversation(@RequestBody String body) {
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

        Long rideId = json.getLong("rideId");
        if(!rideService.existsById(rideId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rideId not found");
        }

        List<Message> conversation = messageService.findConversation(senderId, recipientId, rideId);
        return conversation;
    }

    @RequestMapping(
            value = "/users/{senderId}/sent",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<Message> getSentMessage(@PathVariable long senderId) {
        Optional<User> sender = userService.findById(senderId);
        if(!sender.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id: " + senderId + ", not found");
        }
        List<Message> message = messageService.findSent(sender.get());
        return message;
    }

    @RequestMapping(
            value = "/users/{recipientId}/recieved",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<Message> getRecievedMessage(@PathVariable long recipientId) {
        Optional<User> recipient = userService.findById(recipientId);
        if(!recipient.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id: " + recipientId + ", not found");
        }
        List<Message> message = messageService.findRecieved(recipient.get());
        return message;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> responseStatusHandler(Exception e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);

    }
}
