package is.hi.hbvproject.models.requestObjects.ride;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.LineString;
import org.wololo.geojson.Point;

public class CreateRideRequest {
  @JsonProperty("driver")
  private Long driver;
  @JsonProperty("passengers")
  private Long[] passengers;
  @JsonProperty("origin")
  private Point origin;
  @JsonProperty("destination")
  private Point destination;
  @JsonProperty("route")
  private LineString route;
  @JsonProperty("departure_time")
  private String departureTime;
  @JsonProperty("seats")
  private short seats;

  private CreateRideRequest() {}

  public CreateRideRequest(
    Long driver,
    Long[] passengers,
    Point origin,
    Point destination,
    LineString route,
    String departureTime,
    short seats
  ) {
    this.driver = driver;
    this.passengers = passengers;
    this.origin = origin;
    this.destination = destination;
    this.route = route;
    this.departureTime = departureTime;
    this.seats = seats;
  }

  public Long getDriver() {
    return driver;
  }

  public Long[] getPassengers() {
    return passengers;
  }

  public Point getOrigin() {
    return origin;
  }

  public Point getDestination() {
    return destination;
  }

  public LineString getRoute() {
    return route;
  }

  public Timestamp getDepartureTime() {
    return Timestamp.valueOf(departureTime);
  }

  public short getSeats() {
    return seats;
  }
}