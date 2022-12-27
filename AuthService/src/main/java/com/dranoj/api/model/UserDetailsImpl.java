package com.dranoj.api.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dranoj.api.model.dto.UserData;

public class UserDetailsImpl implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4325846759650915187L;
	private UserData userData;
	
	public UserDetailsImpl() {}
	
	public UserDetailsImpl(UserData userData) {
		this.userData = userData;
	}
	
	public UserData getUserData() {
		return this.userData;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {		
		return userData.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userData.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
