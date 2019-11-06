package is.hi.hbvproject.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import is.hi.hbvproject.service.Implementation.UserDetailsServiceImplementation;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Value("${jwt_secret}")
  private String jwtSecret;
  private UserDetailsServiceImplementation userDetailsService;
  private BCryptPasswordEncoder passwordEncoder;

  public WebSecurityConfig(UserDetailsServiceImplementation userDetailsService, BCryptPasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
      web
          .ignoring()
          .antMatchers(HttpMethod.POST, "/users/register")
          .antMatchers(HttpMethod.POST, "/users/authenticate");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    AuthenticationFilter authentication = new AuthenticationFilter(authenticationManager());
    authentication.setSecret(jwtSecret);

    AuthorizationFilter authorization = new AuthorizationFilter(authenticationManager());
    authorization.setSecret(jwtSecret);

    http.cors().and().csrf().disable()
    .authorizeRequests().antMatchers(HttpMethod.POST, "/users/register")
    .permitAll().anyRequest().authenticated()
    .and().addFilter(authentication).addFilter(authorization)
    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Bean()
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }
}