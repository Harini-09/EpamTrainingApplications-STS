package com.epam.restcontrollertest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dtos.request.TrainerDetailsDto;
import com.epam.dtos.request.TrainerProfileUpdateDto;
import com.epam.dtos.response.TrainerBasicDetailsDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerTrainingResponseDto;
import com.epam.restcontroller.TrainerController;
import com.epam.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;
      
    @Test
    void testRegisterTrainer() throws Exception {
        TrainerDetailsDto trainerDetails = new TrainerDetailsDto();
        trainerDetails.setFirstName("Trainer");
        trainerDetails.setLastName("trainer");
        trainerDetails.setEmail("Trainer@gmail.com");
        trainerDetails.setTrainingTypeName("yoga");

        TrainerBasicDetailsDto expectedResponse = new TrainerBasicDetailsDto();
        expectedResponse.setUserName("Trainer");
        expectedResponse.setPassword("ahsdgaoieughaiu");
        when(trainerService.saveTrainer(trainerDetails)).thenReturn(expectedResponse);

        mockMvc.perform(post("/gym/trainer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerDetails)))
                .andExpect(status().isCreated());

        verify(trainerService, times(1)).saveTrainer(any(TrainerDetailsDto.class));
        verifyNoMoreInteractions(trainerService);
    }
 
    @Test
    void testGetTrainerProfile() throws Exception {
        String username = "john_doe";

        TrainerProfileDto expectedResponse = new TrainerProfileDto();
        expectedResponse.setFirstName("firstname");

        when(trainerService.getTrainer(username)).thenReturn(expectedResponse);

        mockMvc.perform(get("/gym/trainer/{username}", username))
                .andExpect(status().isOk());

        verify(trainerService, times(1)).getTrainer(username);
        verifyNoMoreInteractions(trainerService);
    }

    @Test
     void testUpdateTrainerProfile() throws Exception {
        TrainerProfileUpdateDto trainerProfileUpdateDto = new TrainerProfileUpdateDto();
        trainerProfileUpdateDto.setUsername("username");
        trainerProfileUpdateDto.setFirstName("firstname");
        trainerProfileUpdateDto.setLastName("lastname");
        trainerProfileUpdateDto.setTrainingTypeName("yoga");
        trainerProfileUpdateDto.setActive(false);
        TrainerProfileDto expectedResponse = new TrainerProfileDto();
        expectedResponse.setFirstName("firstname");
        expectedResponse.setTrainingTypeName("yoga");
        expectedResponse.setLastName("lastname");
        expectedResponse.setTraineesList(List.of());
        expectedResponse.setActive(false);
        when(trainerService.updateTrainer(trainerProfileUpdateDto)).thenReturn(expectedResponse);
 
        mockMvc.perform(put("/gym/trainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerProfileUpdateDto)))
                .andExpect(status().isOk());
                 
 
        verify(trainerService, times(1)).updateTrainer(any(TrainerProfileUpdateDto.class));
        verifyNoMoreInteractions(trainerService);
    }

    @Test
    void testGetNotAssignedActiveTrainers() throws Exception {
        String username = "john_doe";

        TrainerDto expectedResponse = new TrainerDto();
        expectedResponse.setUserName(username);
        expectedResponse.setFirstName(username);
        expectedResponse.setLastName(username);
        expectedResponse.setTrainingType("Zumba");        

        when(trainerService.getNotAssignedActiveTrainers(username)).thenReturn(expectedResponse);

        mockMvc.perform(get("/gym/trainer/activeTrainer/{username}", username))
                .andExpect(status().isOk());

        verify(trainerService, times(1)).getNotAssignedActiveTrainers(username);
        verifyNoMoreInteractions(trainerService);
    } 
    
    @Test
    public void testGetTrainerTrainingsList() throws Exception {
        String username = "john_doe";
        LocalDate periodFrom = LocalDate.of(2023, 1, 1);
        LocalDate periodTo = LocalDate.of(2023, 12, 31);
        String traineeName = "alice";

        List<TrainerTrainingResponseDto> expectedResponse = new ArrayList<>();
       expectedResponse.add(new TrainerTrainingResponseDto("exercise", periodFrom, "Zumba", 5l, traineeName));

        when(trainerService.getTrainerTrainingsList(username, periodFrom, periodTo, traineeName)).thenReturn(expectedResponse);

        mockMvc.perform(get("/gym/trainer/trainerTrainings")
                .param("username", username)
                .param("periodFrom", periodFrom.toString())
                .param("periodTo", periodTo.toString())
                .param("traineeName", traineeName))
                .andExpect(status().isOk());

        verify(trainerService, times(1)).getTrainerTrainingsList(username, periodFrom, periodTo, traineeName);
        verifyNoMoreInteractions(trainerService);
    }     
} 
