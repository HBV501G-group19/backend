package is.hi.hbvproject.models.responseObjects;

public class AuthenticationResponse {
	private final String jwttoken;
	public AuthenticationResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}
	public String getToken() {
		return this.jwttoken;
	}
}