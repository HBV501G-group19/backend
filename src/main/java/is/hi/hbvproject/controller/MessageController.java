package is.hi.hbvproject.controller;

import is.hi.hbvproject.models.requestObjects.message.CreateMessageRequest;
import is.hi.hbvproject.models.responseObjects.ConversationResponse;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

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
    public Message createMessage(@RequestBody @Valid CreateMessageRequest message) {
        Optional<User> sender = userService.findById(message.getSender());
        if (!sender.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender not found");
        }
        Optional<User> recipient = userService.findById(message.getRecipient());
        if (!recipient.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipient not found");
        }
        Optional<Ride> ride = rideService.findById(message.getRide());
        if (!ride.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ride not found");
        }
        String messageBody = message.getBody();
        if(messageBody.trim().length() == 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message must contain a message body");
        }

        Message messageEntity = new Message(messageBody, recipient.get(), sender.get(), ride.get());
        messageService.save(messageEntity);
        return messageEntity;
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
        long senderId = json.getLong("senderId");

        if(!userService.existsById(senderId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SenderId not found");
        }

        long recipientId = json.getLong("recipientId");
        if(!userService.existsById(recipientId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RecipientId not found");
        }

        long rideId = json.getLong("rideId");
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
            value = "/users/{recipientId}/received",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<Message> getReceivedMessage(@PathVariable long recipientId) {
        Optional<User> recipient = userService.findById(recipientId);
        if(!recipient.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id: " + recipientId + ", not found");
        }
        List<Message> message = messageService.findReceived(recipient.get());
        return message;
    }

    @RequestMapping(
        value = "/messages/conversation/{conversationId}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public List<Message> getConversation(@PathVariable int conversationId){
        return messageService.findConversation(conversationId);
    }

    @RequestMapping(
        value = "/messages/user/{userId}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public List<ConversationResponse> getUsersConversations(@PathVariable long userId) {
        List<Message> messages = messageService.findAllConversations(userId);
        List<ConversationResponse> convos = new ArrayList<>();

        long id = Long.MAX_VALUE;
        List<Message> convo = new ArrayList<>();
        for(Message m : messages) {
            if (id == Long.MAX_VALUE) {
                id = m.getConversationId();
                convo.add(m);
            } else if (id != m.getConversationId()){
                ConversationResponse cr = makeConversationResponse(convo, userId);
                convos.add(cr);
                id = m.getConversationId();
                convo = new ArrayList<>();
            }
            convo.add(m);
        }
        if (messages.size() > 0)
            convos.add(makeConversationResponse(convo, userId));
        return convos;
    }

    private ConversationResponse makeConversationResponse(List<Message> convo, long userId) {
        Message m = convo.get(0);
        long rideDriver = m.getRide().getDriver();
        long talkingTo = m.getSenderId();
        if (talkingTo == userId) {
            talkingTo = m.getRecipientId();
        }

        return new ConversationResponse(userId, talkingTo, m.getRideId(), (rideDriver ==  userId), convo);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> responseStatusHandler(Exception e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);

    }
}
