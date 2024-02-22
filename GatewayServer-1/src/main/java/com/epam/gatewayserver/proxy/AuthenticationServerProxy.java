package com.epam.gatewayserver.proxy;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.gatewayserver.dtos.AuthRequest;

@FeignClient(name="gym-authentication-app",fallback = AuthenticationServerProxyImpl.class)
@LoadBalancerClient(name="gym-authentication-app",configuration = AuthenticationServerProxyImpl.class)
public interface AuthenticationServerProxy {

	 @PostMapping("/auth/login")
	 public String getToken(@RequestBody AuthRequest authRequest);
	 
	 @GetMapping("/auth/validate")
	 public String validateToken(@RequestParam("token") String token);
	
}
