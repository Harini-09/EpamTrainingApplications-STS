package com.epam.service;

import com.epam.customexceptions.UserException;
import com.epam.dtos.request.Credential;

public interface UserService {
	public void loginUser(Credential credential) throws UserException;
	public void updatePassword(Credential credential,String newPassword) throws UserException;
}
