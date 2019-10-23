package is.hi.hbvproject.persistence.entities;

import java.time.LocalDate;
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

import javax.validation.constraints.NotNull;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.PositionSequenceBuilders;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.geolatte.geom.PositionSequenceBuilder;

import org.wololo.geojson.Point;

import com.fasterxml.jackson.annotation.JsonGetter;

import org.wololo.geojson.LineString;

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
	@Column(columnDefinition = "geometry")
	private org.geolatte.geom.Point<G2D> origin;
	
	@NotNull
	@Column(columnDefinition = "geometry")
	private org.geolatte.geom.Point<G2D> destination;
	
	@NotNull
	@Column(columnDefinition = "geometry")
	private org.geolatte.geom.LineString<G2D> route;
	
	@NotNull
	private LocalDate departureTime;
	
	@NotNull
	private long duration;
	
	@NotNull
	private short totalSeats;
	
	@NotNull
	private short freeSeats;
		
	public Ride() {}
	//public Ride(Point o, Point d, LineString r, Date departureTime, long duration, short seats, User driver) {

	public Ride(
			Point o,
			Point d,
			LineString r,
			LocalDate departureTime,
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
        
		double[] oCoords = o.getCoordinates();
		org.geolatte.geom.Point<G2D> origin = Geometries.mkPoint(new G2D(oCoords[0], oCoords[1]), CoordinateReferenceSystems.WGS84);
		//org.locationtech.jts.geom.Coordinate c = new org.locationtech.jts.geom.Coordinate(oCoords[0], oCoords[1]);
		//org.locationtech.jts.geom.Point p = (org.locationtech.jts.geom.Point) org.locationtech.jts.geom.;
		//org.geojson.Point origin = new org.geojson.Point(new LngLatAlt(oCoords[0], oCoords[1], 0.0));
		this.origin = origin;
		
		double[] dCoords = d.getCoordinates();
		//org.geojson.Point destination = new org.geojson.Point(new LngLatAlt(dCoords[0], dCoords[1], 0.0));
		org.geolatte.geom.Point<G2D> destination = Geometries.mkPoint(new G2D(dCoords[0], dCoords[1]), CoordinateReferenceSystems.WGS84);
		this.destination = destination;
		
		//org.geolatte.geom.LineString<G2D> x = Geometries.mkLineString(, null);
		
		double[][] routeCoords = r.getCoordinates();
		// Húrra fyrir java
		PositionSequenceBuilder<G2D> x = 
			PositionSequenceBuilders.fixedSized(
				routeCoords.length,
				origin.getPositionClass()
			);
		
		for(double[] coord : routeCoords) {
			x.add(coord[0], coord[1]);
		}
		org.geolatte.geom.LineString<G2D> route = 
			new org.geolatte.geom.LineString<G2D>(
				x.toPositionSequence(),
				origin.getCoordinateReferenceSystem()
			);
		this.route = route;
		
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
	
	public org.geolatte.geom.Point<G2D> getOrigin() {
		return this.origin;
	}
	
	@JsonGetter("origin")
	public Point getOriginJson() {
		double[] coords = new double[2];
		origin.getPositions().getCoordinates(0, coords);
		Point o = new Point(coords);
		return o;
	}

	public void setOrigin(Point o) {
		double[] oCoords = o.getCoordinates();
		org.geolatte.geom.Point<G2D> origin = Geometries.mkPoint(new G2D(oCoords[0], oCoords[1]), CoordinateReferenceSystems.WGS84);
		//org.geojson.Point origin = new org.geojson.Point(new LngLatAlt(oCoords[0], oCoords[1], 0.0));
		this.origin = origin;
	}

	public org.geolatte.geom.Point<G2D> getDestination() {
		return this.destination;
	}
	
	@JsonGetter("destination")
	public Point getDestinationJson() {
		double[] coords = new double[2];
		origin.getPositions().getCoordinates(0, coords);
		Point d = new Point(coords);
		return d;
	}
	
	public void setDestination(Point d) {
		double[] dCoords = d.getCoordinates();
		org.geolatte.geom.Point<G2D> destination = Geometries.mkPoint(new G2D(dCoords[0], dCoords[1]), CoordinateReferenceSystems.WGS84);
		//org.geojson.Point destination = new org.geojson.Point(new LngLatAlt(dCoords[0], dCoords[1], 0.0));
		this.destination = destination;
	}

	public org.geolatte.geom.LineString<G2D> getRoute() {
		return this.route;
	}
	
	@JsonGetter("route")
	public LineString getRouteGeoJson() {
		List<double[]> coords = new ArrayList<>();
		double[][] coordArray = new double[this.route.getNumPositions()][this.route.getCoordinateDimension()];
		this.route.getPositions().forEach(position -> {
			double[] coord = new double[2];
			position.toArray(coord);
			coords.add(coord);
		});
		
		coordArray = coords.toArray(coordArray);
		LineString r = new LineString(coordArray);
		return r;
	}

	public void setRoute(LineString r) {
		//org.geojson.LineString newRoute = new org.geojson.LineString();
		//for(double[] coord : route) {
		//	LngLatAlt lngLatAlt = new LngLatAlt(coord[0], coord[1], 0.0);
		//	newRoute.add(lngLatAlt);
		//}
		
		//this.route = newRoute;
		
		double[][] routeCoords = r.getCoordinates();
		// Húrra fyrir java
		PositionSequenceBuilder<G2D> x = 
			PositionSequenceBuilders.fixedSized(
				routeCoords.length,
				origin.getPositionClass()
			);
		
		for(double[] coord : routeCoords) {
			x.add(coord[0], coord[1]);
		}
		org.geolatte.geom.LineString<G2D> newRoute = 
			new org.geolatte.geom.LineString<G2D>(
				x.toPositionSequence(),
				origin.getCoordinateReferenceSystem()
			);
		this.route = newRoute;
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
