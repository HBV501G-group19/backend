package is.hi.hbvproject.persistence.entities;

import java.util.Date;
import java.util.HashSet;
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

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.wololo.jts2geojson.GeoJSONWriter;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "ride")
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
	private Date created;
	@UpdateTimestamp
	private Date updated;
	
	@NotNull
	@JsonRawValue
	@Column(columnDefinition = "geometry")
	private Point origin;
	
	@NotNull
	@JsonRawValue
	@Column(columnDefinition = "geometry")
	private Point destination;
	
	@NotNull
	@JsonRawValue
	@Column(columnDefinition = "geometry")
	private LineString route;
	
	@Transient
	@JsonIgnore
	private GeoJSONWriter writer;
	
	@NotNull
	private Date time;
	private boolean isArrivalTime;
	
	@NotNull
	private short totalSeats;
	
	@NotNull
	private short freeSeats;
		
	public Ride() {}
	
	public Ride(Point o, Point d, LineString r, Date time, boolean isArrivalTime, short seats, User driver) {
		this.driver = driver;
		this.origin = o;
		this.destination = d;
		this.route = r;
		this.time = time;
		this.isArrivalTime = isArrivalTime;
		this.totalSeats = seats;
		this.freeSeats = seats;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public User getDriver() {
		return driver;
	}
	
	public void setDriver(User driver) {
		this.driver = driver;
	}
	
	public void addPassenger(User passenger) {
		this.passengers.add(passenger);
		this.setFreeSeats(--freeSeats);
		passenger.getRides().add(this);
	}
	
	public Set<User> getPassengers() {
		return passengers;
	}
	
	public void setPassengers(Set<User> passengers) {
		this.passengers = passengers;
	}
	public Date getCreated() {
		return created;
	}
	
	public Date getUpdated() {
		return updated;
	}
	
	public Point getOrigin() {
		return origin;
	}
	
	@JsonGetter("origin")
	public String getOriginJSON() {
		this.writer = new GeoJSONWriter();
		return writer.write(this.origin).toString();
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Point getDestination() {
		return destination;
	}
	
	@JsonGetter("destination")
	public String getDestinationJSON() {
		this.writer = new GeoJSONWriter();
		return writer.write(this.destination).toString();
	}

	public void setDestination(Point destination) {
		this.destination = destination;
	}

	public LineString getRoute() {
		return route;
	}
	
	@JsonGetter("route")
	public String getRouteJSON() {
		this.writer = new GeoJSONWriter();
		return writer.write(this.route).toString();
	}

	public void setRoute(LineString route) {
		this.route = route;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public boolean isArrivalTime() {
		return isArrivalTime;
	}

	public void setArrivalTime(boolean isArrivalTime) {
		this.isArrivalTime = isArrivalTime;
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
