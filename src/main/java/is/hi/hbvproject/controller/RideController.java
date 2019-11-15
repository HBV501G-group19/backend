package is.hi.hbvproject.controller;

import org.geolatte.geom.G2D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.wololo.geojson.LineString;
import org.wololo.geojson.Point;
import org.wololo.geojson.GeoJSONFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import is.hi.hbvproject.service.RideService;
import is.hi.hbvproject.service.UserService;
import is.hi.hbvproject.utils.ORSUtils;
import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;

class ConvinientRequestBody{
	@JsonProperty("user_id")
	long userId;
	@JsonProperty("origin")
	double[] origin;
	@JsonProperty("destination")
	double[] destination;
	@JsonProperty("range")
	double[] range;
	@JsonProperty("departureTime")
	Timestamp departureTime;
	
	public long getUserId() {
		return userId;
	}
	
	public List<double[]> getLocations() {
		List<double[]> locations = new ArrayList<>();
		locations.add(origin);
		locations.add(destination);
		return locations;
	}
	
	public double[] getRange() {
		return range;
	}
	
	public void setDepartureTime(String time) {
		departureTime = Timestamp.valueOf(time);
	}
	
	public Timestamp getDepartureTime() {
		return departureTime;
	}
}


@RestController
public class RideController {
	RideService rideService;
	UserService userService;
	final String orsKey;
	@Autowired
	public RideController(@Value("${ors.key}") String orsKey, RideService rideService, UserService userService) {
		this.rideService = rideService;
		this.userService = userService;
		this.orsKey = orsKey;
	}
	
	@RequestMapping(
			value = "/rides/convenient",
			method = RequestMethod.POST,
			produces = "application/json"
	)
	public List<Ride> getConvinientRides(@RequestBody ConvinientRequestBody body) {
		long userId = body.getUserId();
		List<double[]> locations = body.getLocations();
		Timestamp departureTime = body.getDepartureTime();
		double[] range = body.getRange();
		
		Optional<User> user = userService.findById(userId);
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id: " + userId + " not found");
		}

		List<org.geolatte.geom.Polygon<G2D>> isochrones = ORSUtils.getIsochrones(locations, range, orsKey);
		
		Timestamp minTimestamp = (Timestamp) departureTime.clone();
		minTimestamp.setTime((departureTime.getTime() - 600000));
		Timestamp maxTimestamp = (Timestamp) departureTime.clone();
		maxTimestamp.setTime((departureTime.getTime() + 600000));

		List<Ride> rides = rideService.findNearby(
			isochrones.get(0),
			isochrones.get(1),
			minTimestamp,
			maxTimestamp
		);
		return rides;
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
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ride not found");
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
		long driverId = json.getLong("driver_id");
		Optional<User> driver = userService.findById(driverId);
		if (!driver.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id: " + driverId + " not found");
		}
		
		JSONObject originJson = json.getJSONObject("origin");
		JSONObject destinationJson = json.getJSONObject("destination");
		JSONObject routeJson = json.getJSONObject("route");
		
		Point origin = (Point) GeoJSONFactory.create(originJson.toString());
		
		Point destination = (Point) GeoJSONFactory.create(destinationJson.toString());

		LineString route = (LineString) GeoJSONFactory.create(routeJson.toString());
		
		String departureTimeJson = json.getString("departure_time");
		Timestamp departureTime = Timestamp.valueOf(departureTimeJson);
		
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
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id: " + passengerId + " not found");
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
		rideService.save(ride);
		return ride;
	}

	@RequestMapping(
			value = "/rides/addpassenger",
			method = RequestMethod.PATCH,
			produces = "application/json"
	)
	public Ride addPassenger(@RequestBody String body) {
		JSONObject json = new JSONObject(body);
		long passengerId = json.getLong("passengerId");
		Optional<User> findPassenger = userService.findById(passengerId);
		if(!findPassenger.isPresent()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passenger " + passengerId + " not found");
		}
		User passenger = findPassenger.get();

		long rideId = json.getLong("rideId");
		Optional<Ride> findRide = rideService.findById(rideId);
		if (!findRide.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ride "+ rideId +"not found");
		}
		Ride ride = findRide.get();

		if(ride.getDriver() == passengerId){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Driver cannot be added as Passenger");
		}

		for (long p:ride.getPassengers()) {
			if(p == passengerId) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passenger: " + passengerId + " is already a passenger" +
						" and cannot be added multiple times");
			}
		}

		ride.addPassenger(passenger);
		rideService.save(ride);

		return ride;
	}
	
}
