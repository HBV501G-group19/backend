package is.hi.hbvproject.models.requestObjects.ride;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.wololo.geojson.Point;

public class ConvinientRideRequest{
	@JsonProperty("user")
	Long user;
	@JsonProperty("origin")
	Point origin;
	@JsonProperty("destination")
	Point destination;
	@JsonProperty("range")
	double[] range;
	@JsonProperty("departure_time")
	String departureTime;
  
  private ConvinientRideRequest() {}

  public ConvinientRideRequest(
    Long user,
    Point origin,
    Point destination,
    double[] range,
    String departureTime
  ) {
    this.user = user;
    this.origin = origin  ;
    this.destination = destination  ;
    this.range = range;
    this.departureTime = departureTime  ;
  }

  public long getUser() {
		return user;
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