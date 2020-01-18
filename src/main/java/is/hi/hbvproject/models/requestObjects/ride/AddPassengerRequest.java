package is.hi.hbvproject.models.requestObjects.ride;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddPassengerRequest {
  @JsonProperty("passenger")
  @NotEmpty
  Long passenger;

  @JsonProperty("ride")
  @NotEmpty
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