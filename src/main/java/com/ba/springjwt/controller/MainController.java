package com.ba.springjwt.controller;


import com.ba.springjwt.model.AuthenticationRequest;
import com.ba.springjwt.model.AuthenticationResponse;
import com.ba.springjwt.service.MyUserDetailsService;
import com.ba.springjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/{message}")
    public java.lang.String welcome(@PathVariable("message") String message) {
        return message;
    }

    @GetMapping("/")
    public java.lang.String hello() {
        return "Hello Sir";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassWord()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect Username or Password", e);
        }
        //fetch Userdetail from UserDetailService
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final java.lang.String jwtToken = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
}
