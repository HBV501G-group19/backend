package is.hi.hbvproject.controller;

import org.geolatte.geom.G2D;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;

import org.wololo.geojson.Feature;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import is.hi.hbvproject.service.OrsService;
import is.hi.hbvproject.service.RideService;
import is.hi.hbvproject.service.UserService;
import is.hi.hbvproject.models.requestObjects.ride.AddPassengerRequest;
import is.hi.hbvproject.models.requestObjects.ride.ConvinientRideRequest;
import is.hi.hbvproject.models.requestObjects.ride.CreateRideRequest;
import is.hi.hbvproject.persistence.entities.Ride;
import is.hi.hbvproject.persistence.entities.User;

@RestController
public class RideController {
	RideService rideService;
	UserService userService;
	OrsService orsService;
	
	@Autowired
	public RideController(RideService rideService, UserService userService, OrsService orsService){
		this.rideService = rideService;
		this.userService = userService;
		this.orsService = orsService;
	}

	@RequestMapping(
		value = "/rides/convenient",
		method = RequestMethod.POST,
		consumes = "application/json",
		produces = "application/json"
	)
	public List<Ride> getConvinientRides(@RequestBody @Valid ConvinientRideRequest body) {
		// TODO: Refactor to not need JSONArrays here
		JSONArray locations = new JSONArray();
		locations.put(body.getOrigin().getCoordinates());
		locations.put(body.getDestination().getCoordinates());

		JSONArray range = new JSONArray();
		for(double r : body.getRange()) {
			range.put(r);
		}
		List<org.geolatte.geom.Polygon<G2D>> isochrones = orsService.getIsochrones(locations, range);
		Timestamp minTimestamp = new Timestamp(body.getDepartureTime().getTime() - 300000); // five minutes ago
		Timestamp maxTimestamp = new Timestamp(body.getDepartureTime().getTime() - 28800000); // in 8 hours

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
	public Ride createRide(@RequestBody @Valid CreateRideRequest body) {
		Optional<User> driver = userService.findById(body.getDriver());
		if (!driver.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id: " + body.getDriver() + " not found");
		}
		
		Feature originProps = orsService.getGeoNames(body.getOrigin(), new JSONObject("{}"));
		Feature destinationProps = orsService.getGeoNames(body.getDestination(), new JSONObject("{}"));

		List<User> passengers = new ArrayList<>();
		for(Long passengerId : body.getPassengers()) {
				if (passengerId == body.getDriver()) {
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Driver can not be a passenger");
				}
				Optional<User> passenger = userService.findById(passengerId);
				if (!passenger.isPresent()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id: " + passengerId + " not found");
				}
				passengers.add(passenger.get());
		}

		Ride ride = new Ride(
			body.getOrigin(),
			body.getDestination(),
			body.getRoute(),
			body.getDepartureTime(),
			50,
			50,
			/* 
				TODO: need to figure out a good way to pass the duration and distance properties
							might take some refactoring in the Ride entity
			*/
			// body.getDuration(),
			// body.getDistance(),
			body.getSeats(),
			driver.get(),
			passengers
		);

		ride.setProperty("origin", originProps.getProperties());
		ride.setProperty("destination", destinationProps.getProperties());
		rideService.save(ride);
		return ride;
	}

	@RequestMapping(
			value = "/rides/addpassenger",
			method = RequestMethod.PATCH,
			produces = "application/json"
	)
	public Ride addPassenger(@RequestBody @Valid  AddPassengerRequest body) {
		Long passengerId = body.getPassenger();

		Optional<User> findPassenger = userService.findById(passengerId);
		if(!findPassenger.isPresent()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passenger " + passengerId + " not found");
		}
		User passenger = findPassenger.get();

		Optional<Ride> findRide = rideService.findById(body.getRide());
		if (!findRide.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ride "+ body.getRide() +"not found");
		}
		Ride ride = findRide.get();

		if(ride.getDriver() == passengerId){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Driver cannot be added as Passenger");
		}

		if (ride.getPassengers().contains(passengerId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passenger: " + passengerId + " is already a passenger" +
					" and cannot be added multiple times");
		};

		ride.addPassenger(passenger);
		rideService.save(ride);

		return ride;
	}
	
}
