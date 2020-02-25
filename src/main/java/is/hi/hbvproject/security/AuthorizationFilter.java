package is.hi.hbvproject.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.SignatureVerificationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {
  private String jwtSecret;

  public AuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  public void setSecret(String secret) {
    this.jwtSecret = secret;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    String header = req.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer")) {
      chain.doFilter(req, res);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
    String token = req.getHeader("Authorization");
    if (token != null) {
      try {
        String user = JWT.require(HMAC512(jwtSecret.getBytes())).build().verify(token.replace("Bearer ", ""))
          .getSubject();
        if (user != null) {
          return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }
      } catch (SignatureVerificationException ex) {
        // throwing this exception fucks up Springs error handling
        // and prevents a response from being sent back
        return null;
      }
      return null;
    }

    return null;
  }
}