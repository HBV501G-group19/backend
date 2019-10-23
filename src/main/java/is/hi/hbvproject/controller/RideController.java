package is.hi.hbvproject.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.wololo.geojson.LineString;
import org.wololo.geojson.Point;
import org.wololo.geojson.GeoJSONFactory;

import is.hi.hbvproject.service.RideService;
import is.hi.hbvproject.service.UserService;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;
@RestController
public class RideController {
	RideService rideService;
	UserService userService;
	@Autowired
	public RideController(RideService rideService, UserService userService) {
		this.rideService = rideService;
		this.userService = userService;
	}
	
	@RequestMapping(
			value = "/rides/{id}/dafdasfds",
			method = RequestMethod.GET,
			produces = "application/json"
	)
	public List<Ride> getConvinientRides() {
		
		return null;
	}
	
	@RequestMapping(
		value = "/rides",
		method = RequestMethod.GET,
		produces = "application/json"
	)
	public List<Ride> getRides() {
		return rideService.findAll();
	}
	
	@RequestMapping(
			value = "/rides/{id}",
			method = RequestMethod.GET,
			produces = "application/json"
	)
	public Ride getRide(@PathVariable long id) {
		Optional<Ride> ride = rideService.findById(id);
		if (!ride.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found");
		}
		return ride.get();
	}
	
	@RequestMapping(
			value = "/rides/create",
			method = RequestMethod.POST,
			consumes = "application/json",
			produces = "application/json"
	)
	public Ride createRide(@RequestBody String body) {
		JSONObject json = new JSONObject(body);
		long driverId = json.getLong("driverId");
		Optional<User> driver = userService.findById(driverId);
		if (!driver.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id: " + driverId + " not found");
		}
		
		String originJson = json.getString("origin");
		String destinationJson = json.getString("destination");
		String routeJson = json.getString("route");
		
		Point origin = (Point) GeoJSONFactory.create(originJson);
		Point destination = (Point) GeoJSONFactory.create(destinationJson);
		LineString route = (LineString) GeoJSONFactory.create(routeJson);
		
		String departureTimeJson = json.getString("departureTime");
		LocalDate departureTime = LocalDate.parse(departureTimeJson);
		
		long duration = json.getLong("duration");
		
		short seats = (short) json.getInt("seats");
		
		List<User> passengers = new ArrayList<>();
		if (json.has("passengers")) {
			JSONArray passengerIds = json.getJSONArray("passengers");
			passengerIds.forEach(id -> {
				Long passengerId = ((Integer) id).longValue();
				if (passengerId == driverId) {
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Driver can not be a passenger");
				}
				Optional<User> passenger = userService.findById(passengerId);
				if (!passenger.isPresent()) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id: " + passengerId + " not found");
				}
				passengers.add(passenger.get());
			});
		}
			
		Ride ride = new Ride(
			origin,
			destination,
			route,
			departureTime,
			duration,
			seats,
			driver.get(),
			passengers
		);

		return ride;
	}
	
}
