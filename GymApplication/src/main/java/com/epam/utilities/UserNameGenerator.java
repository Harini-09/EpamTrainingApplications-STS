package com.epam.utilities;

import org.springframework.stereotype.Component;

@Component
public class UserNameGenerator {

	public String generateUserName(String email) {
		return email.substring(0, email.indexOf("@"));
	}

}
