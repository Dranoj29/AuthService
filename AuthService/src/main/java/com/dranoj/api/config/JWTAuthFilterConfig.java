package com.dranoj.api.config;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dranoj.api.constant.JWTConstant;
import com.dranoj.api.util.JWTUtil;

@Component
public class JWTAuthFilterConfig extends OncePerRequestFilter{
	
	@Autowired
	private UserDetailsService userDatailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader = request.getHeader(JWTConstant.HEADER_KEY);
		final String username;
		
		if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(JWTConstant.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
		
        if (!JWTUtil.validateToken(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        username = JWTUtil.getTokenDetails(authHeader);	
		UserDetails userDetail = userDatailService.loadUserByUsername(username);
		
		UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(
				username,
				userDetail, 
				//userDetail.getAuthorities()
				new ArrayList<>()
				);
		
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}

}
