package is.hi.hbvproject.persistence.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

//import org.wololo.geojson.Point;

import com.fasterxml.jackson.annotation.JsonRawValue;

import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.Point;

//import org.wololo.geojson.LineString;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "rides")
public class Ride {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private User driver;
	
	@ManyToMany(
			mappedBy = "rides",
			fetch = FetchType.LAZY
			)
	private Set<User> passengers = new HashSet<>();
	
	@CreationTimestamp
	private LocalDate created;
	@UpdateTimestamp
	private LocalDate updated;
	
	@NotNull
	@JsonRawValue
	@Column(columnDefinition = "geometry")
	private Point origin;
	
	@NotNull
	@JsonRawValue
	@Column(columnDefinition = "geometry")
	private Point destination;
	
	//@NotNull
	@JsonRawValue
	@Column(columnDefinition = "geometry")
	private LineString route;
	
	@NotNull
	private LocalDate departureTime;
	
	@NotNull
	private long duration;
	
	@NotNull
	private short totalSeats;
	
	@NotNull
	private short freeSeats;
		
	public Ride() {}
	//public Ride(org.wololo.geojson.Point o, org.wololo.geojson.Point d, LineString r, Date departureTime, long duration, short seats, User driver) {

	public Ride(
			org.wololo.geojson.Point o,
			org.wololo.geojson.Point d,
			org.wololo.geojson.LineString r,
			LocalDate departureTime,
			long duration,
			short seats,
			User driver,
			List<User> passengers
	) {
		this.driver = driver;
		this.origin = null;
		this.destination = null;
		//this.route = r;
		this.departureTime= departureTime;
		this.duration = duration;
		this.totalSeats = seats;
		this.freeSeats = seats;
		
		double[] oCoords = o.getCoordinates();
		Point origin = new Point(new LngLatAlt(oCoords[0], oCoords[1], 0.0));
		this.origin = origin;
		
		double[] dCoords = d.getCoordinates();
		Point destination = new Point(new LngLatAlt(dCoords[0], dCoords[1], 0.0));
		this.destination = destination;
		
		passengers.forEach(passenger -> this.passengers.add(passenger));
	}
	
	public Long getId() {
		return this.id;
	}
	
	public long getDriver() {
		return driver.getId();
	}
	
	public void setDriver(User driver) {
		this.driver = driver;
	}
	
	public void addPassenger(User passenger) {
		this.passengers.add(passenger);
		this.setFreeSeats(--freeSeats);
		passenger.getRides().add(this);
	}
	
	public List<Long> getPassengers() {
		List<Long> ids = new ArrayList<>();
		passengers.forEach(passenger -> ids.add(passenger.getId()));
		return ids;
	}
	
	public void setPassengers(Set<User> passengers) {
		this.passengers = passengers;
	}
	public LocalDate getCreated() {
		return created;
	}
	
	public LocalDate getUpdated() {
		return updated;
	}
	
	public org.wololo.geojson.Point getOrigin() {
		org.wololo.geojson.Point o = null;
		double[] coords = {
			origin.getCoordinates().getLongitude(),
			origin.getCoordinates().getLatitude()
		};
		o = new org.wololo.geojson.Point(coords);
		return o;
	}

	public void setOrigin(org.wololo.geojson.Point o) {
		double[] oCoords = o.getCoordinates();
		Point origin = new Point(new LngLatAlt(oCoords[0], oCoords[1], 0.0));
		this.origin = origin;
	}

	public org.wololo.geojson.Point getDestination() {
		org.wololo.geojson.Point d = null;
		double[] coords = {
			destination.getCoordinates().getLongitude(),
			destination.getCoordinates().getLatitude()
		};
		d = new org.wololo.geojson.Point(coords);
		return d;
	}
	
	public void setDestination(org.wololo.geojson.Point d) {
		double[] dCoords = d.getCoordinates();
		Point destination = new Point(new LngLatAlt(dCoords[0], dCoords[1], 0.0));
		this.destination = destination;
	}

	public LineString getRoute() {
		return route;
	}

	public void setRoute(LineString route) {
		this.route = route;
	}

	public LocalDate getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalDate departureTime) {
		this.departureTime = departureTime;
	}

	public long getDuration() {
		return duration;
	}

	public void duration(long duration) {
		this.duration = duration;
	}
	
	public short getTotalSeats() {
		return totalSeats;
	}
	
	public short getFreeSeats() {
		return freeSeats;
	}
	
	public void setFreeSeats(short seats) {
		if (seats < 0) {
			// handle
			throw new Error("Trying to set less then 0 free seats");
		} else if (seats > totalSeats) {
			throw new Error("Trying to set more free seats then total seats");
		}
		
		freeSeats = seats;
	}
}
