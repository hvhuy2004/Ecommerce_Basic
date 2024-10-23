package com.ecom.service;

import org.springframework.stereotype.Service;

import com.ecom.model.UserDtls;

public interface UserService {
	
	public UserDtls saveUser(UserDtls user);
	
	public UserDtls getUserByEmail(String email);
	
}
