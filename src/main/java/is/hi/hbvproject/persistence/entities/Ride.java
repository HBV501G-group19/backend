package is.hi.hbvproject.persistence.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.geolatte.geom.G2D;
import org.wololo.geojson.Point;
import org.wololo.geojson.LineString;

import com.fasterxml.jackson.annotation.JsonGetter;

import is.hi.hbvproject.utils.WololoGeolatteConverter;

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
	private Timestamp created;
	@UpdateTimestamp
	private Timestamp upTimestampd;
	
	
	@NotNull
	@Column(columnDefinition = "geometry")
	private org.geolatte.geom.Point<G2D> origin;
	
	@NotNull
	@Column(columnDefinition = "geometry")
	private org.geolatte.geom.Point<G2D> destination;
	
	@NotNull
	@Column(columnDefinition = "geometry")
	private org.geolatte.geom.LineString<G2D> route;
	
	@NotNull
	private Timestamp departureTime;
	
	@NotNull
	private long duration;
	
	@NotNull
	private short totalSeats;
	
	@NotNull
	private short freeSeats;
		
	public Ride() {}

	public Ride(
			Point o,
			Point d,
			LineString r,
			Timestamp departureTime,
			long duration,
			short seats,
			User driver,
			List<User> passengers
	) {
		this.driver = driver;
		this.departureTime= departureTime;
		this.duration = duration;
		this.totalSeats = seats;
		this.freeSeats = seats;
        
		this.origin = WololoGeolatteConverter.toGeolattePoint(o);
		this.destination = WololoGeolatteConverter.toGeolattePoint(d);
		this.route = WololoGeolatteConverter.toGeolatteLineString(r);
		
		passengers.forEach(passenger -> this.addPassenger(passenger));
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
		if (freeSeats == 0) {
			throw new Error("No available space in ride");
		}
		this.passengers.add(passenger);
		this.setFreeSeats((short)(freeSeats - 1));
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
	public Timestamp getCreated() {
		return created;
	}
	
	public Timestamp getUpTimestampd() {
		return upTimestampd;
	}
	
	public org.geolatte.geom.Point<G2D> getOrigin() {
		return this.origin;
	}
	
	@JsonGetter("origin")
	public Point getOriginJson() {
		return WololoGeolatteConverter.toWololoPoint(origin);
	}

	public void setOrigin(Point o) {
		this.origin = WololoGeolatteConverter.toGeolattePoint(o);
	}

	public org.geolatte.geom.Point<G2D> getDestination() {
		return this.destination;
	}
	
	@JsonGetter("destination")
	public Point getDestinationJson() {
		return WololoGeolatteConverter.toWololoPoint(destination);
	}
	
	public void setDestination(Point d) {
		this.destination = WololoGeolatteConverter.toGeolattePoint(d);

	}

	public org.geolatte.geom.LineString<G2D> getRoute() {
		return this.route;
	}
	
	@JsonGetter("route")
	public LineString getRouteGeoJson() {
		return WololoGeolatteConverter.toWololoLineString(route);
	}

	public void setRoute(LineString r) {
		this.route = WololoGeolatteConverter.toGeolatteLineString(r);
	}

	public Timestamp getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Timestamp departureTime) {
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
		} else if (seats > totalSeats | seats > freeSeats) {
			throw new Error("Trying to set more free seats then available seats");
		}
		
		freeSeats = seats;
	}
}
