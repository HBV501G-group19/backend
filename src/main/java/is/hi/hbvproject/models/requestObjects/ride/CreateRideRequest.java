package is.hi.hbvproject.models.requestObjects.ride;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.LineString;
import org.wololo.geojson.Point;

public class CreateRideRequest {
  @JsonProperty("driver")
  @NotNull
  private Long driver;

  @JsonProperty("passengers")
  private List<Long> passengers;

  @JsonProperty("origin")
  @NotNull
  private Point origin;

  @JsonProperty("destination")
  @NotNull
  private Point destination;

  @JsonProperty("route")
  @NotNull
  private LineString route;

  @JsonProperty("distance")
  @NotNull
  private long distance;
  
  @JsonProperty("duration")
  @NotNull
  private long duration;

  @NotNull
  @FutureOrPresent
  private LocalDateTime departureTime;

  @JsonProperty("seats")
  @NotNull
  @Positive
  private short seats;


  private CreateRideRequest() {}

  public CreateRideRequest(
    Long driver,
    List<Long> passengers,
    Point origin,
    Point destination,
    LineString route,
    long distance,
    long duration,
    short seats
  ) {
    this.driver = driver;
    if (passengers == null) passengers = new ArrayList<Long>();
    else this.passengers = passengers;
    this.origin = origin;
    this.destination = destination;
    this.route = route;
    this.distance = distance;
    this.duration = duration;
    this.seats = seats;
  }

  @JsonProperty("departureTime")
  private void unpackTimestamp(String timeString){
    LocalDateTime time = LocalDateTime.parse(timeString);
    this.departureTime = time;
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

  public long getDistance() {
    return distance;
  }

  public long getDuration() {
    return duration;
  }

  public Timestamp getDepartureTime() {
    return Timestamp.valueOf(departureTime);
  }

  public short getSeats() {
    return seats;
  }
}