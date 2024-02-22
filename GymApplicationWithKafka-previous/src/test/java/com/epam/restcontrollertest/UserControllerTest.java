package com.epam.restcontrollertest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dtos.request.Credential;
import com.epam.restcontroller.UserController;
import com.epam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    private Credential credential = new Credential();
	
    @BeforeEach
    public void setUp() {
        credential.setUsername("username");
        credential.setPassword("password");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginUserTest() throws Exception {
        Mockito.doNothing().when(userService).loginUser(credential);

        mockMvc.perform(post("/gym/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(credential)))
                .andExpect(status().isOk());
        
        Mockito.verify(userService).loginUser(credential);
    }

    @Test
    void changeLoginTest() throws Exception {
        
        String newPassword = "newPassword";

        Mockito.doNothing().when(userService).updatePassword(credential, newPassword);

        mockMvc.perform(post("/gym/user/changepassword/{newPassword}", newPassword)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(credential))
                .param("newPassword", newPassword))
                .andExpect(status().isOk());
        
        Mockito.verify(userService).updatePassword(credential, newPassword);
    }
}
