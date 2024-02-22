package com.epam.utilities;

import java.security.SecureRandom;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {

	public String generatePassword() {
		char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=").toCharArray();

        return RandomStringUtils.random(10, 0, possibleCharacters.length - 1, false, false, possibleCharacters,new SecureRandom());
	}

}
 