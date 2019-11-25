package is.hi.hbvproject.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;
import is.hi.hbvproject.service.RideService;
import is.hi.hbvproject.service.UserService;
import kong.unirest.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@CrossOrigin
public class UserController {
	UserService service;
	RideService rideService;
	BCryptPasswordEncoder passwordEncoder;


	@Autowired
	public UserController(UserService service, RideService rideService, BCryptPasswordEncoder passwordEncoder) {
		this.service = service;
		this.rideService = rideService;
		this.passwordEncoder = passwordEncoder;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
	public List<User> getUsers() {
		return service.findAll();
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = "application/json")
	public Optional<User> getUserById(@PathVariable long id) {
		Optional<User> user = service.findById(id);
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		return user;
	}

	@RequestMapping(value = "/users/{id}/drives", method = RequestMethod.GET, produces = "application/json")
	public Set<Ride> getUserDrives(@PathVariable long id) {
		Optional<User> user = service.findById(id);
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		return user.get().getDrives();
	}

	@RequestMapping(value = "/users/{id}/rides", method = RequestMethod.GET, produces = "application/json")
	public Set<Ride> getUserRides(@PathVariable long id) {
		Optional<User> user = service.findById(id);
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		return user.get().getRides();
	}

	@RequestMapping(value = "/users/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public User createUser(@RequestBody String body) throws JsonParseException, JsonMappingException, IOException {
		JSONObject user = new JSONObject(body);
		String username = user.getString("username");
		String password = user.getString("password");

		if (service.existsByUsername(username)){
			// ekki viss með þennan status kóða
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User already exists");
		}

		User userObject = new User();
		userObject.setUsername(username);
		userObject.setPassword(passwordEncoder.encode(password));
		service.save(userObject);
		return userObject;
	}
}
