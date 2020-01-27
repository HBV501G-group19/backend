package is.hi.hbvproject.models.requestObjects.message;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateMessageRequest {
  @JsonProperty("sender")
  @NotEmpty
  private Long sender;
  @JsonProperty("recipient")
  @NotEmpty
  private Long recipient;
  @JsonProperty("ride")
  @NotEmpty
  private Long ride;
  @JsonProperty("body")
  @NotEmpty
  private String body;


  private CreateMessageRequest() {}

  public CreateMessageRequest(Long sender, Long recipient, Long ride, String body) {
    this.sender = sender;
    this.recipient = recipient;
    this.ride = ride;
    this.body = body;
  }

  public Long getSender() {
    return sender;
  }
  public Long getRecipient() {
    return recipient;
  }
  public Long getRide() {
    return ride;
  }

  public String getBody() {
    return body;
  }

}