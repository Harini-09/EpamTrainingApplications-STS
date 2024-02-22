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

import com.epam.restcontroller.TrainingTypeController;
import com.epam.service.TrainingTypeService;

@WebMvcTest(TrainingTypeController.class)
class TrainingTypeControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
    @MockBean
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTrainingTypeTest() throws Exception {
        String trainingTypeName = "fitness"; 

        Mockito.doNothing().when(trainingTypeService).saveTrainingType(trainingTypeName);

        mockMvc.perform(post("/gym/trainingType/{trainingTypeName}", trainingTypeName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(trainingTypeService).saveTrainingType(trainingTypeName);
    }
    
}
