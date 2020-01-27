package is.hi.hbvproject.models.requestObjects.ride;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddPassengerRequest {
  @JsonProperty("passenger")
  @NotEmpty
  @Positive
  private Long passenger;

  @JsonProperty("ride")
  @NotEmpty
  @Positive
  private Long ride;
  
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