package com.epam.restcontrollertest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dtos.request.TrainingDetailsDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.TrainingType;
import com.epam.entities.User;
import com.epam.restcontroller.TrainingController;
import com.epam.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TrainingController.class)
class TrainingControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;
    
    private TrainingDetailsDto trainingDetails = new TrainingDetailsDto();
    
    @BeforeEach
	public void setUp() {
		User user1 = new User();
		user1.setUserName("user1");
		User user2 = new User();
		user2.setUserName("user2");
		Trainee t1 = new Trainee();
		t1.setUser(user1);
		TrainingType type=new TrainingType();
		type.setTrainingTypeName("Zumba");
		Trainer t2 = new Trainer();
		t2.setUser(user2);
		t2.getTraineesList().add(t1);
		t2.setTrainingType(type);
		
		trainingDetails.setTraineeUserName("user1");
		trainingDetails.setTrainerUserName("user2");
		trainingDetails.setTrainingDuration(5l);
		trainingDetails.setTrainingName("training");
		trainingDetails.setTrainingTypeName("Zumba");
		MockitoAnnotations.openMocks(this);
	}

    @Test
    void testSaveTraining() throws Exception {
    	doNothing().when(trainingService).saveTraining(trainingDetails);
        mockMvc.perform(post("/gym/training")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(trainingDetails)))
                .andExpect(status().isOk());

        verify(trainingService, times(1)).saveTraining(any(TrainingDetailsDto.class));
        verifyNoMoreInteractions(trainingService);
    }

}
