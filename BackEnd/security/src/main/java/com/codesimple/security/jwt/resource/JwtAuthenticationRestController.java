package com.codesimple.security.jwt.resource;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codesimple.security.jwt.JwtTokenUtil;
import com.codesimple.security.jwt.JwtUserDetails;
import com.codesimple.security.objects.User;
import com.codesimple.security.service.UserService;

@RestController
//@CrossOrigin(origins="**")
public class JwtAuthenticationRestController {

  @Value("${jwt.http.request.header}")
  private String tokenHeader;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private UserDetailsService jwtUserDetailsService;
  
  @Autowired
  private JwtAuthenticationService jwtAuthenticationService;
  
  @Autowired
  private UserService userService;
  
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
      throws AuthenticationException {

    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new JwtTokenResponse(token));
  }

  @RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
  public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
    String authToken = request.getHeader(tokenHeader);
    final String token = authToken.substring(7);
    String username = jwtTokenUtil.getUsernameFromToken(token);
    JwtUserDetails user = (JwtUserDetails) jwtUserDetailsService.loadUserByUsername(username);

    if (jwtTokenUtil.canTokenBeRefreshed(token)) {
      String refreshedToken = jwtTokenUtil.refreshToken(token);
      return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @PutMapping(value="/forgotpassword")
  public ResponseEntity<String> fogotPassword(@RequestBody JwtTokenRequest authenticationRequest) {
	  User user = userService.findByUserName(authenticationRequest.getUsername());
	  String message;
	  if(user == null) {
			 throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", authenticationRequest.getUsername()));		
	   }
	  try {
		 message = jwtAuthenticationService.forgotPassword(user);
	} catch (Exception e) {
		logger.info("***Error occoures while retrieving the lost password***"+e.getMessage());
		return new ResponseEntity<String>("Problem occoured while sending the message", HttpStatus.BAD_GATEWAY);
	}
	  return new ResponseEntity<String>(message , HttpStatus.OK);	  
  }
  
  @PostMapping(value="/registration")
  public ResponseEntity<String> newUserRegistration(@RequestBody JwtTokenRequest authenticationRequest) {
	  User user = userService.findByUserName(authenticationRequest.getUsername());
	  String message;
	  if(user != null) {
			 throw new UsernameFoundException(String.format("USER_EXISTS '%s'.", authenticationRequest.getUsername()));		
	   }
	  try {
		 message = jwtAuthenticationService.newUserRegistration(authenticationRequest);
	} catch (Exception e) {
		logger.info("***Error occoures while new user registration***"+e.getMessage());
		return new ResponseEntity<String>("Problem occoured while sending the message", HttpStatus.BAD_GATEWAY);
	}
	  return new ResponseEntity<String>(message , HttpStatus.OK);
  }
  
  @PutMapping(value="/passwordChange")
  public ResponseEntity<String> passwordChange(HttpServletRequest request, @RequestBody JwtTokenRequest authenticationRequest) {
	  String message;
	  String authToken = request.getHeader(tokenHeader);
	  final String token = authToken.substring(7);
	  String username = jwtTokenUtil.getUsernameFromToken(token);
	  User user = new User(username, authenticationRequest.getPassword());
	  try {
		  message = jwtAuthenticationService.passwordChange(user);
	} catch (Exception e) {
		logger.info("***Error occoures while changing the password***"+e.getMessage());
		return new ResponseEntity<String>("Problem occoured while changing the password", HttpStatus.BAD_GATEWAY);
		
	}	  
	  return new ResponseEntity<String>(message , HttpStatus.OK);	  
  }
  
  @ExceptionHandler({ AuthenticationException.class })
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  private void authenticate(String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);

    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new AuthenticationException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new AuthenticationException("INVALID_CREDENTIALS", e);
    }
  }
}

