package com.epam.gatewayserver.proxy;

import org.springframework.stereotype.Service;

import com.epam.gatewayserver.dtos.AuthRequest;

@Service
public class AuthenticationServerProxyImpl implements AuthenticationServerProxy{

	@Override
	public String getToken(AuthRequest authRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String validateToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
