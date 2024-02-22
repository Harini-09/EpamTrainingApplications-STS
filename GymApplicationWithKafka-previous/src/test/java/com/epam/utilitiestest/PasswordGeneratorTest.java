package com.epam.utilitiestest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.utilities.PasswordGenerator;

public class PasswordGeneratorTest {
	@InjectMocks
    private PasswordGenerator passwordGenerator;

    @Mock
    private SecureRandom secureRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGeneratePassword() {
        char[] possibleCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=".toCharArray();
        when(secureRandom.nextInt(possibleCharacters.length)).thenReturn(3, 6, 10, 20, 15, 7, 2, 1, 11, 5);

        String generatedPassword = passwordGenerator.generatePassword();

        assertNotNull(generatedPassword);
}
}