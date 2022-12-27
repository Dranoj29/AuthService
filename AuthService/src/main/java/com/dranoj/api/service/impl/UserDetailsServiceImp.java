package com.dranoj.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dranoj.api.model.UserDetailsImpl;
import com.dranoj.api.model.dto.UserData;
import com.dranoj.api.repo.UserDataRepo;

@Service
public class UserDetailsServiceImp implements UserDetailsService{
	
	@Autowired
	private UserDataRepo userDataRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserData userData = userDataRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("No User Found"));
		return new UserDetailsImpl(userData);
	}

}
