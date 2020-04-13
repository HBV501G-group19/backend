package is.hi.hbvproject.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import is.hi.hbvproject.service.UserService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
  UserService userService;

	@Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  public JwtAuthenticationController(UserService userService) {
    this.userService = userService;
  }

	@RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(
    @RequestBody AuthenticationRequest authenticationRequest,
    @Value("${jwt_secret}") String jwtSecret
  ) throws Exception {
    String username = authenticationRequest.getUsername();

		Authentication auth = authenticate(username, authenticationRequest.getPassword());

    String token = JWT.create()
    .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
    // set token to expire in 10 days
    .withExpiresAt(new Date(System.currentTimeMillis() + Integer.MAX_VALUE)).sign(HMAC512(jwtSecret.getBytes()));  

    Optional<Long> userId = userService.findId(username);
    return ResponseEntity.ok(new AuthenticationResponse(token, username, userId.get()));
  }
  
	private Authentication authenticate(String username, String password) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
  }

  @ExceptionHandler
  public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException e) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("error", "authentication");
    responseBody.put("message", "incorrect username or password");
    responseBody.put("status", HttpStatus.BAD_REQUEST);
    responseBody.put("path", "/users/authenticate");
    return ResponseEntity.badRequest().body(responseBody);
  }
}