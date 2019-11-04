package is.hi.hbvproject.models.requestObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationRequest {
  @JsonProperty("username")
  String username;
  @JsonProperty("password")
  String password;

  private AuthenticationRequest() {
  }

  public AuthenticationRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}