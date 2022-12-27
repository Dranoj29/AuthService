package com.dranoj.api.controller.impl;



import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dranoj.api.constant.JWTConstant;
import com.dranoj.api.controller.IAuthController;
import com.dranoj.api.controller.model.dto.AuthRequest;
import com.dranoj.api.model.UserDetailsImpl;
import com.dranoj.api.model.dto.Role;
import com.dranoj.api.model.dto.UserData;
import com.dranoj.api.repo.RoleRepo;
import com.dranoj.api.repo.UserDataRepo;
import com.dranoj.api.util.JWTUtil;

@RestController
public class AuthController implements IAuthController{
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserDataRepo userDataRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	
	@PostMapping("/insert")
	public ResponseEntity<String> insert(@RequestParam String userId, @RequestParam String username, @RequestParam String password){
		UserData user = new UserData();
		user.setUserId(userId);
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		System.out.println("pass: "+user.getPassword());
		
		Role role = new Role();
		role.setName("ADMIN");
		this.roleRepo.save(role);
		
		user.getRoles().add(role);
		this.userDataRepo.save(user);
		
		return ResponseEntity.ok("Success");
	}
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<Map<String, Object>> authenticate(@RequestBody AuthRequest auth){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		   Authentication authentication = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
		   if(authentication.isAuthenticated()) {		
			   UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();  
			   String token = JWTUtil.generateToken(userDetails);
			   response.put("token", token);
			   response.put("prefix", JWTConstant.TOKEN_PREFIX);
			   response.put("header", JWTConstant.HEADER_KEY);
			   response.put("message", "Successfully Authenticated");
			   return ResponseEntity.ok(response);
		   }	   
		} catch (AuthenticationException e) {
			response.put("error", e.getMessage());
        }
	   
	   	response.put("massage", "Authentication failed");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
	
	@PostMapping("/validate")
	public ResponseEntity<Map<String, String>> validate(HttpServletRequest request){
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    	//Map<String, Claim> claims = JWTUtil.getTokenClaim(token);
		
    	Map<String, String> response = new HashMap<>();
    	//response.put("userId", claims.get("userId").asString());
    	response.put("username", JWTUtil.getTokenDetails(token));
    	
		return ResponseEntity.ok().body(response);
	}
}
