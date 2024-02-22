package com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.epam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    	log.info(usersRepository.findByUserName(userName).get().toString());
        return usersRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Name is not Found"));
    }
}