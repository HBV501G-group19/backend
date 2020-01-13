package is.hi.hbvproject.models.requestObjects.ride;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddPassengerRequest {
  @JsonProperty("passenger")
  Long passenger;
  @JsonProperty("ride")
  Long ride;
  
  private AddPassengerRequest() {}

  public AddPassengerRequest(Long passenger, Long ride) {
    this.passenger = passenger;
    this.ride = ride;
  }

  public Long getPassenger() {
    return passenger;
  }

  public Long getRide() {
    return ride;
  }
}