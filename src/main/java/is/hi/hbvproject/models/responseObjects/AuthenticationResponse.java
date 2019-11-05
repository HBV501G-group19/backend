package is.hi.hbvproject.models.responseObjects;

import com.fasterxml.jackson.annotation.JsonGetter;

public class AuthenticationResponse {
	private final Long id;
	private final String username;
	private final String jwttoken;

	public AuthenticationResponse(String jwttoken, String username, Long id) {
		this.id = id;
		this.username = username;
		this.jwttoken = jwttoken;
	}

	@JsonGetter("id")
	public String id() {
		return id.toString();
	}

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return jwttoken;
	}
}