package is.hi.hbvproject.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

class LoginRequest {
  @JsonProperty("username")
  String username;
  @JsonProperty("password")
  String password;

  private LoginRequest() {
  }

  public LoginRequest(String username, String password) {
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

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private String jwtSecret;

  private AuthenticationManager authManager;

  public AuthenticationFilter(AuthenticationManager authManager) {
    this.authManager = authManager;
  }

  public void setSecret(String secret) {
    this.jwtSecret = secret;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
    try {
      LoginRequest credentials = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);

      return authManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(),
          credentials.getPassword(), new ArrayList<>()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {
    String token = JWT.create()
        .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + 864_000_000)).sign(HMAC512(jwtSecret.getBytes()));

    res.addHeader("Authorization", token);
  }
}