package com.epam.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainerDetailsDto;
import com.epam.dtos.request.TrainerProfileUpdateDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TraineeDto;
import com.epam.dtos.response.TrainerBasicDetailsDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerTrainingResponseDto;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.TrainingType;
import com.epam.entities.User;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import com.epam.service.TrainerServiceImpl;
import com.epam.utilities.PasswordGenerator;
import com.epam.utilities.TrainerValueMapper;

public class TrainerServiceTest {
	@Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private TrainerValueMapper mapper;

    @Mock
    private KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testSaveTrainer_ValidDetails_Success() throws TrainingTypeException {
        TrainerDetailsDto trainerDetails = new TrainerDetailsDto();
        trainerDetails.setTrainingTypeName("Valid Training Type Name");

        String randomPassword = "randomGeneratedPassword";
        User newUser = new User();
        Trainer trainer = new Trainer(); 
        TrainingType trainingType = new TrainingType();
        NotificationDto notificationDto = new NotificationDto();
        
        when(passwordGenerator.generatePassword()).thenReturn(randomPassword);
        when(mapper.convertToUser(trainerDetails, randomPassword)).thenReturn(newUser);
        when(trainingTypeRepository.findByTrainingTypeName("Valid Training Type Name")).thenReturn(Optional.of(trainingType));
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(mapper.convertToNotificationDtoAfterSavingTrainer(newUser, trainer, trainerDetails)).thenReturn(notificationDto);
        when(kafkaTemplate.send(any(Message.class))).thenReturn(null);
        when(mapper.convertToTrainerBasicDetailsDto(newUser,randomPassword)).thenReturn(new TrainerBasicDetailsDto());
 
        TrainerBasicDetailsDto result = trainerService.saveTrainer(trainerDetails);
  
        assertNotNull(result);
    }  
    
    @Test
    public void testSaveTrainer_TrainingTypeNotFound_Exception() throws TrainingTypeException {
        TrainerDetailsDto trainerDetails = new TrainerDetailsDto();
        trainerDetails.setTrainingTypeName("Nonexistent Training Type");

        String randomPassword = "randomGeneratedPassword";
        User newUser = new User();

        when(passwordGenerator.generatePassword()).thenReturn(randomPassword);
        when(mapper.convertToUser(trainerDetails, randomPassword)).thenReturn(newUser);
        when(trainingTypeRepository.findByTrainingTypeName("Nonexistent Training Type")).thenReturn(Optional.empty());

        assertThrows(TrainingTypeException.class, () -> trainerService.saveTrainer(trainerDetails));
         
    }
    
    @Test
    public void testGetTrainer_ValidUsername_Success() throws UserException {
    	String validUsername = "validUsername";
        User user = new User();
        user.setUserName(validUsername);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        List<TraineeDto> traineeDtoList = new ArrayList<>();
        TrainerProfileDto expectedProfileDto = new TrainerProfileDto();
        when(userRepository.findByUserName(validUsername)).thenReturn(Optional.of(user));
        when(mapper.convertToTraineeDtoList(trainer)).thenReturn(traineeDtoList);
        when(mapper.convertToTrainerProfileDto(user, trainer, traineeDtoList)).thenReturn(expectedProfileDto);

        TrainerProfileDto result = trainerService.getTrainer(validUsername);

        assertEquals(expectedProfileDto, result);
    }
    
    @Test
    public void testGetTrainer_InvalidUsername_Exception() {
        String invalidUsername = "invalidUsername";

        when(userRepository.findByUserName(invalidUsername)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> trainerService.getTrainer(invalidUsername));
    }
    
    @Test
    public void testUpdateTrainer_ValidInput_Success() throws UserException {
        TrainerProfileUpdateDto trainerProfileUpdateDto = new TrainerProfileUpdateDto();
        trainerProfileUpdateDto.setUsername("validUsername");
        trainerProfileUpdateDto.setFirstName("NewFirstName");
        trainerProfileUpdateDto.setLastName("NewLastName");
        trainerProfileUpdateDto.setActive(true);
        
        User existingUser = new User();
        existingUser.setUserName("validUsername");  
        Trainer existingTrainer = new Trainer();
        existingTrainer.setUser(existingUser);
        List<TraineeDto> traineeDtoList = new ArrayList<>();
        TrainerProfileDto updatedProfileDto = new TrainerProfileDto();
        
        when(userRepository.findByUserName(trainerProfileUpdateDto.getUsername())).thenReturn(Optional.of(existingUser));
        when(mapper.convertToTraineeDtoListForUpdate(existingTrainer)).thenReturn(traineeDtoList);
        when(mapper.convertToNotificationDtoAfterUpdatingTrainer(existingUser)).thenReturn(new NotificationDto());
        when(kafkaTemplate.send(any(Message.class))).thenReturn(null);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(mapper.convertToTrainerProfileDtoForUpdate(existingUser, existingTrainer, traineeDtoList)).thenReturn(updatedProfileDto);
        
        TrainerProfileDto result = trainerService.updateTrainer(trainerProfileUpdateDto);
        
        assertEquals(updatedProfileDto, result);
        assertEquals("NewFirstName", existingUser.getFirstName());
        assertEquals("NewLastName", existingUser.getLastName());
    }
    
    @Test
    public void testUpdateTrainer_UserNotFound_Exception() {
        TrainerProfileUpdateDto trainerProfileUpdateDto = new TrainerProfileUpdateDto();
        trainerProfileUpdateDto.setUsername("nonExistentUsername");
        
        when(userRepository.findByUserName(trainerProfileUpdateDto.getUsername())).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> trainerService.updateTrainer(trainerProfileUpdateDto));
        
    }
    
    @Test
    public void testGetNotAssignedActiveTrainers_ValidInput_Success() throws UserException, TrainerException {
        String username = "validUsername";
        User user = new User();
        user.setUserName(username);
        Trainer trainer1 = new Trainer();
        trainer1.setUser(user);
        trainer1.getUser().setActive(true);
        Trainer trainer2 = new Trainer();
        trainer2.setUser(user);
        trainer2.getUser().setActive(true);
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer1);
        trainers.add(trainer2);
        
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(trainerRepository.findAll()).thenReturn(trainers);
        when(mapper.convertToTrainerDto(trainers, user)).thenReturn(new TrainerDto());

        TrainerDto result = trainerService.getNotAssignedActiveTrainers(username);

        assertNotNull(result);
        
    }
    
    @Test
    public void testGetNotAssignedActiveTrainers_UserNotFound_Exception() {
        String nonExistentUsername = "nonExistentUsername";
        
        when(userRepository.findByUserName(nonExistentUsername)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> trainerService.getNotAssignedActiveTrainers(nonExistentUsername));
    }
    
    @Test
    public void testGetTrainerTrainingsList_ValidInput_Success() throws UserException {
        String username = "validUsername";
        LocalDate periodFrom = LocalDate.of(2023, 1, 1);
        LocalDate periodTo = LocalDate.of(2023, 12, 31);
        String traineeName = "John Doe";
        
        User user = new User();
        List<Training> trainerTrainings = new ArrayList<>();
        trainerTrainings.add(new Training());
        trainerTrainings.add(new Training());
        
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(trainerRepository.findTrainingsForTrainer(username, periodFrom, periodTo, traineeName)).thenReturn(trainerTrainings);
        when(mapper.convertToTrainerTrainingsList(trainerTrainings)).thenReturn(new ArrayList<>());

        List<TrainerTrainingResponseDto> result = trainerService.getTrainerTrainingsList(username, periodFrom, periodTo, traineeName);

        assertNotNull(result);
       
    }
    
    @Test
    public void testGetTrainerTrainingsList_UserNotFound_Exception() {
        String nonExistentUsername = "nonExistentUsername";
        LocalDate periodFrom = LocalDate.of(2023, 1, 1);
        LocalDate periodTo = LocalDate.of(2023, 12, 31);
        String traineeName = "John Doe";
        
        when(userRepository.findByUserName(nonExistentUsername)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> trainerService.getTrainerTrainingsList(nonExistentUsername, periodFrom, periodTo, traineeName));
    }


}
