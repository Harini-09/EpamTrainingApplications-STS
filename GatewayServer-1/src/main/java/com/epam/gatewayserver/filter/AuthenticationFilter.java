package com.epam.gatewayserver.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.epam.gatewayserver.customexceptions.UnauthorizedException;
import com.epam.gatewayserver.dtos.AuthRequest;
import com.epam.gatewayserver.proxy.AuthenticationServerProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
    private RouteValidator routeValidator;
    
	@Autowired
    private AuthenticationServerProxy authenticationServerProxy;
	
	public AuthenticationFilter() {
		super(Config.class);
	}
    

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new UnauthorizedException("Missing authorization header");
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                	authenticationServerProxy.validateToken(authHeader);
                    //jwtService.validateToken(authHeader);
                } catch ( Exception e ) {
                    throw new UnauthorizedException("Unauthorized access to application");
                }
            }

            return chain.filter(exchange);
        };
    }

    private String getLoginToken(AuthRequest authRequest) {
        return authenticationServerProxy.getToken(authRequest);
    }


    private AuthRequest readRequestBody(InputStream inputStream) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, AuthRequest.class);
        } catch ( IOException e) {
            throw new RuntimeException("Failed to read request body", e);
        }
    }


    public static class Config {
        // Empty class for configuration if needed
    }
}
