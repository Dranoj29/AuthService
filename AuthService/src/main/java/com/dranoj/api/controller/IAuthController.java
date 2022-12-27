package com.dranoj.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dranoj.api.controller.model.dto.AuthRequest;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api")
@Tag(name = "Auth", description = "Endpoints for authentication and autorization")
public interface IAuthController {
	
	@Hidden
	@Operation(summary = "insert", security = @SecurityRequirement(name = "api"))
	ResponseEntity<String> insert(String userId, String username, String password);
	
	@Operation(summary = "authenticate and get access token")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Authenticated",
					content = {
						@Content(examples = @ExampleObject(value = "{\r\n"
								+ "  \"prefix\": \"Bearer \",\r\n"
								+ "  \"header\": \"Authorization\",\r\n"
								+ "  \"message\": \"Successfully Authenticated\",\r\n"
								+ "  \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiZXhwIjoxNjcyOTk3OTI4fQ.1MuFc6lrJBQelmTNcxV41hZ4RO1JwMqyJOFXqKGQ1I6Cbo3BbaG8LWkx5ZGguoQdUEPzic5tmaKni8aK4_qfBg\"\r\n"
								+ "}"))	
					}
			),
			@ApiResponse(
					responseCode = "403",
					description = "Authentication Failure",
					content = {
						@Content(examples = @ExampleObject(value = "{\r\n"
								+ "  \"error\": \"Bad credentials\",\r\n"
								+ "  \"massage\": \"Authentication failed\"\r\n"
								+ "}"))	
					}
			),
	})
	ResponseEntity<Map<String, Object>> authenticate(AuthRequest auth);
	
	@Operation(summary = "validate token", security = @SecurityRequirement(name = "api"))
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Authorized",
					content = {
						@Content(examples = @ExampleObject(value = "{\r\n"
								+ "  \"username\": \"user@test.com\",\r\n"
								+ "}"))	
					}
			),
			@ApiResponse(
					responseCode = "401",
					description = "Unauthorize",
					content = {
						@Content(examples = @ExampleObject(value = "{\r\n"
								+ "  \"timestamp\": \"2022-12-27T10:14:18.546+00:00\",\r\n"
								+ "  \"status\": 401,\r\n"
								+ "  \"error\": \"Unauthorized\",\r\n"
								+ "  \"path\": \"/api/validate\"\r\n"
								+ "}"))	
					}
			),
	})
	ResponseEntity<Map<String, String>> validate(HttpServletRequest request);

}
