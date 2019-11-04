package is.hi.hbvproject.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import is.hi.hbvproject.models.requestObjects.AuthenticationRequest;
import is.hi.hbvproject.models.responseObjects.AuthenticationResponse;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
	@Autowired
  private AuthenticationManager authenticationManager;
  
	@RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(
    @RequestBody AuthenticationRequest authenticationRequest,
    @Value("${jwt_secret}") String jwtSecret
  ) throws Exception {
		Authentication auth = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    String token = JWT.create()
    .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
    // set token to expire in 10 days
    .withExpiresAt(new Date(System.currentTimeMillis() + 864_000_000)).sign(HMAC512(jwtSecret.getBytes()));

    return ResponseEntity.ok(new AuthenticationResponse(token));
  }
  
	private Authentication authenticate(String username, String password) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
  }
  
  @ExceptionHandler
  public ResponseEntity<Map<String, Object>> badCredentailsHandler(BadCredentialsException e) {
    String m = e.getMessage();
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("error", m);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
  }
}