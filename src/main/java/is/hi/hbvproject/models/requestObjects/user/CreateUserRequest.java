package is.hi.hbvproject.models.requestObjects.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {
  @JsonProperty("username")
  private String username;
  @JsonProperty("email")
  private String email;
  @JsonProperty("password")
  private String password;

  private CreateUserRequest() {}

  public CreateUserRequest(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}