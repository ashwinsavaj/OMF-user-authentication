package com.mindtree.authorization.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindtree.authorization.dao.AuthorizationDao;
import com.mindtree.authorization.entity.PrincipalData;
import com.mindtree.authorization.entity.User;
import com.mindtree.authorization.repository.UserRepository;
import com.mindtree.authorization.service.UserServiceProxy;

@Service
public class AuthorizationDaoImpl implements AuthorizationDao {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserServiceProxy userServiceProxy;

	@Override
	public void register(PrincipalData userPrincipal) {
		userRepository.save(userPrincipal);
	}

	@Override
	public void socialRegisterUser(User user) {
		userServiceProxy.newSocail(user);
	}
}
