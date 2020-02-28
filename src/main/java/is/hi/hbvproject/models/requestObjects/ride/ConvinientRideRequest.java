package is.hi.hbvproject.models.requestObjects.ride;

import java.sql.Timestamp;

import java.time.LocalDateTime;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.Point;

public class ConvinientRideRequest{
 
  @JsonProperty("origin")
  @NotNull
  private Point origin;
  
  @JsonProperty("destination")
  @NotNull
  private Point destination;
  
  @JsonProperty("range")
  @NotNull
  private double[] range;
  
  @FutureOrPresent
  private LocalDateTime departureTime;
  
  private ConvinientRideRequest() {}

  public ConvinientRideRequest(
    Point origin,
    Point destination,
    double[] range,
    LocalDateTime departureTime
  ) {
    this.origin = origin;
    this.destination = destination;
    this.range = range;
  }

  @JsonProperty("departureTime")
  private void unpackTimestamp(String timeString){
    LocalDateTime time = LocalDateTime.parse(timeString);
    this.departureTime = time;
  }
	
	public double[] getRange() {
		return range;
  }
  
  public Point getOrigin() {
    return origin;
  }

  public Point getDestination() {
    return destination;
  }

	public Timestamp getDepartureTime() {
		return Timestamp.valueOf(departureTime);
	}
}