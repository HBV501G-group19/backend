package is.hi.hbvproject.models.requestObjects.ride;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.LineString;
import org.wololo.geojson.Point;

public class CreateRideRequest {
  @JsonProperty("driver")
  @NotEmpty
  private Long driver;

  @JsonProperty("passengers")
  private List<Long> passengers;

  @JsonProperty("origin")
  @NotEmpty
  private Point origin;

  @JsonProperty("destination")
  @NotEmpty
  private Point destination;

  @JsonProperty("route")
  @NotEmpty
  private LineString route;

  @JsonProperty("departure_time")
  @NotEmpty
  private String departureTime;

  @JsonProperty("seats")
  @NotEmpty
  private short seats;


  private CreateRideRequest() {}

  public CreateRideRequest(
    Long driver,
    List<Long> passengers,
    Point origin,
    Point destination,
    LineString route,
    String departureTime,
    short seats
  ) {
    this.driver = driver;
    if (passengers == null) passengers = new ArrayList<Long>();
    else this.passengers = passengers;
    this.origin = origin;
    this.destination = destination;
    this.route = route;
    this.departureTime = departureTime;
    this.seats = seats;
  }

  public Long getDriver() {
    return driver;
  }

  public List<Long> getPassengers() {
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