package com.mindtree.authorization.dao;

import com.mindtree.authorization.entity.PrincipalData;
import com.mindtree.authorization.entity.User;

public interface AuthorizationDao {

	public void register(PrincipalData userPrincipal);
	
	public void socialRegisterUser(User user);

}