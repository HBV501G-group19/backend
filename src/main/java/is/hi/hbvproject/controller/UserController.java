package is.hi.hbvproject.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class UserController {
	UserService service;
	@Autowired
	public UserController(UserService service) {
		this.service = service;
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
		value = "/users/register",
		method = RequestMethod.POST,
		consumes = "application/json",
		produces = "application/json"
	)
	public User createUser(
		@RequestBody String body
	) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(body, User.class);
		
		if (service.existsByUsername(user.getUsername())) {
			// ekki viss með þennan status kóða
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User already exists");
		}
		service.save(user);
		return user;
	}
}
