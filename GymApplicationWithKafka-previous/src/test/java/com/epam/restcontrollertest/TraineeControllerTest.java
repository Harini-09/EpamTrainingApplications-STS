package com.epam.restcontrollertest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dtos.request.TraineeDetailsDto;
import com.epam.dtos.request.TraineeProfileUpdate;
import com.epam.dtos.response.TraineeBasicDetailsDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeTrainingResponseDto;
import com.epam.restcontroller.TraineeController;
import com.epam.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TraineeController.class)
class TraineeControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeService traineeService;

    private TraineeProfileUpdate traineeProfileUpdate;
     
    @BeforeEach
    void setUp() {
        traineeProfileUpdate = new TraineeProfileUpdate();       
        List.of("trainer1", "trainer2"); 
        traineeProfileUpdate.setFirstName("trainee1");
    	traineeProfileUpdate.setLastName("lastname");
    	traineeProfileUpdate.setAddress("address");
    	traineeProfileUpdate.setActive(false);
    	traineeProfileUpdate.setUsername("testUsername");
    } 
  
    @Test 
    void registerTraineeTest() throws Exception {
        TraineeDetailsDto traineeDetails = new TraineeDetailsDto();
        traineeDetails.setEmail("username@gmail.com");
        traineeDetails.setAddress("address");
        traineeDetails.setFirstName("firstname");
        traineeDetails.setLastName("lastname");
 
        TraineeBasicDetailsDto mockBasicDetails = new TraineeBasicDetailsDto();
        mockBasicDetails.setUserName("username");
        mockBasicDetails.setPassword("password");
        Mockito.when(traineeService.saveTrainee(traineeDetails))
               .thenReturn(mockBasicDetails);

        mockMvc.perform(post("/gym/trainee/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(traineeDetails)))
                .andExpect(status().isCreated());
    }
       
    @Test 
    void getTraineeProfileTest() throws Exception {
        String username = "testUsername";
        TraineeProfileDto mockProfileDto = new TraineeProfileDto(/* Initialize with relevant data */);
        Mockito.when(traineeService.getTrainee(username))
                .thenReturn(mockProfileDto);
        mockMvc.perform(get("/gym/trainee/{username}", username))
                .andExpect(status().isOk());
    }
    
    @Test
    void updateTraineeProfileTest() throws Exception {
        mockMvc.perform(put("/gym/trainee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(traineeProfileUpdate)))
                .andExpect(status().isOk());
        Mockito.verify(traineeService).updateTrainee(traineeProfileUpdate);
    }

    @Test
    void deleteTraineeProfileTest() throws Exception {
        String username = "testUsername";
        mockMvc.perform(delete("/gym/trainee/{username}", username))
                .andExpect(status().isOk());
        Mockito.verify(traineeService).deleteTrainee(username);
    }
   

    @Test
    void updateTraineeTrainerListTest() throws Exception {
    	String traineeUserName = "testUsername";
        List<String> trainerUserNames = List.of("trainer1", "trainer2"); 
        mockMvc.perform(put("/gym/trainee/{username}/{trainerUserNames}", traineeUserName, String.join(",", trainerUserNames))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerUserNames)))
                .andExpect(status().isOk());
        Mockito.verify(traineeService).updateTraineeTrainerList(traineeUserName, trainerUserNames);
    }
    
    @Test
    void getTraineeTrainingsListTest() throws Exception {
        String username = "testUsername";
        LocalDate periodFrom = LocalDate.now().minusMonths(1);
        LocalDate periodTo = LocalDate.now();
        String trainerName = "trainer1";
        String trainingType = "Cardio";
        List<TraineeTrainingResponseDto> mockTrainingsList = List.of();
        Mockito.when(traineeService.getTraineeTrainingsList(username, periodFrom, periodTo, trainerName, trainingType))
               .thenReturn(mockTrainingsList);

        mockMvc.perform(get("/gym/trainee/traineeTrainings")
                .param("username", username)
                .param("periodFrom", periodFrom.toString())
                .param("periodTo", periodTo.toString())
                .param("trainerName", trainerName)
                .param("trainingType", trainingType))
                .andExpect(status().isOk());
    }

}
 









