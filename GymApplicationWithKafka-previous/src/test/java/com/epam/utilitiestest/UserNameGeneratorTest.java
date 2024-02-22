package com.epam.utilitiestest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.epam.utilities.UserNameGenerator;

public class UserNameGeneratorTest {
	@Test
    void testGenerateUserName() {
        UserNameGenerator userNameGenerator = new UserNameGenerator();

        String email = "john.doe@example.com";
        String expectedUserName = "john.doe";

        String generatedUserName = userNameGenerator.generateUserName(email);

        assertEquals(expectedUserName, generatedUserName);
    }
}
