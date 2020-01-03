package com.mindtree.authorization.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mindtree.authorization.entity.CustomUserData;
import com.mindtree.authorization.entity.User;

@Service
public class CustomUserServiceImpl implements UserDetailsService {
	@Autowired
	private UserServiceProxy userServiceProxy;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> userdata = userServiceProxy.getUserByIdAdmin(userName);
		User user = userdata.get();
		return new CustomUserData(user);
	}
}
