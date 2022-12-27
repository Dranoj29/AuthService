package com.dranoj.api.util;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.dranoj.api.constant.JWTConstant;
import com.dranoj.api.model.UserDetailsImpl;


public class JWTUtil {


	private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
	
	public static String generateTokenFromMap(Map<String, String> payload) {
		Builder jwtBuilder = JWT.create();
		payload.forEach((key, value) -> {
			jwtBuilder.withClaim(key, value);
		});
		return jwtBuilder
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTConstant.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(JWTConstant.SECRET_KEY.getBytes()));
	}
	
	public static String generateToken(UserDetailsImpl user) {
		return JWT
			.create()
			.withSubject(user.getUsername())
			//.withClaim("userId", user.getUserData().getUserId())
			.withExpiresAt(new Date(System.currentTimeMillis() + JWTConstant.EXPIRATION_TIME))
			.sign(Algorithm.HMAC512(JWTConstant.SECRET_KEY.getBytes()));
	}

	public static String getTokenDetails(String token) {
		String username = JWT
			.require(Algorithm.HMAC512(JWTConstant.SECRET_KEY.getBytes()))
			.build().verify(token.replace(JWTConstant.TOKEN_PREFIX, ""))
			.getSubject();
		return username;
	}
	
	public static Map<String, Claim> getTokenClaim(String token) {
		return JWT
			.require(Algorithm.HMAC512(JWTConstant.SECRET_KEY.getBytes()))
			.build().verify(token.replace(JWTConstant.TOKEN_PREFIX, ""))
			.getClaims();
	}

	public static boolean validateToken(String token) {
		try {
			JWT.require(Algorithm.HMAC512(JWTConstant.SECRET_KEY.getBytes()))
				.build()
				.verify(token.replace(JWTConstant.TOKEN_PREFIX, ""))
				.getSubject();
			return true;

		} catch (Exception ex) {
			logger.error("error validateToken: " + ex.getMessage());
		}
		return false;
	}
    
    
}
