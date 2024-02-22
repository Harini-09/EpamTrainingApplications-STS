package com.epam.servicetest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainingDetailsDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.TrainingType;
import com.epam.entities.User;
import com.epam.repository.TrainingRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import com.epam.service.TrainingServiceImpl;
import com.epam.utilities.TrainingValueMapper;

@ExtendWith(MockitoExtension.class) 
public class TrainingServiceTest {
	 @InjectMocks
	    private TrainingServiceImpl trainingService;

	    @Mock
	    private UserRepository userRepo;

	    @Mock
	    private TrainingTypeRepository trainingTypeRepo;

	    @Mock
	    private TrainingRepository trainingRepo;

	    @Mock
	    private KafkaTemplate<String, NotificationDto> notificationKafkaTemplate;

	    @Mock
	    private KafkaTemplate<String, TrainingReportDto> reportKafkaTemplate;

	    @Mock
	    private TrainingValueMapper mapper;

	    @Test
	    public void testSaveTraining_ValidDetails_Success() throws UserException, TrainingTypeException, TrainerException {
	    	User user1 = new User();
	    	user1.setUserName("trainee");
	    	User user2 = new User();
	    	user2.setUserName("trainer");
	    	Trainer trainer = new Trainer();
	    	trainer.setUser(user2);
	    	TrainingType trainingType = new TrainingType();
	    	trainingType.setTrainingTypeName("trainingType");
	    	trainer.setTrainingType(trainingType);
	    	Trainee trainee = new Trainee();
	    	trainee.setUser(user1);	    	
	    	trainee.setTrainersList(List.of(trainer));
	        TrainingDetailsDto validTrainingDetails = new TrainingDetailsDto();
	        validTrainingDetails.setTraineeUserName("trainee");
	        validTrainingDetails.setTrainerUserName("trainer");
	        validTrainingDetails.setTrainingTypeName("trainingType");
	    
	        when(userRepo.findByUserName(eq("trainee"))).thenReturn(Optional.of(user1));
	        when(userRepo.findByUserName(eq("trainer"))).thenReturn(Optional.of(user2));
	        when(trainingTypeRepo.findByTrainingTypeName(eq("trainingType"))).thenReturn(Optional.of(trainingType));
	        TrainingReportDto t1 = new TrainingReportDto();
	        NotificationDto n1 = new NotificationDto();
	        when(mapper.convertToTrainingReportDto(any(), any())).thenReturn(t1);
	        when(mapper.convertToNotificationDto(any(), any(), any())).thenReturn(n1);
	        Message<NotificationDto> m1=null;
	        Message<TrainingReportDto> m2 = null; 

	      trainingService.saveTraining(validTrainingDetails);
	       verify(trainingRepo, times(1)).save(any(Training.class));
	        verify(notificationKafkaTemplate, times(1)).send(m1);
	        verify(reportKafkaTemplate, times(1)).send(m2);
	        
	    } 
	    
	    @Test
	    void testSaveTaining_InvalidTrainee_ThrowsUserException() throws UserException, TrainingTypeException {
	    	User user1 = new User();
	    	user1.setUserName("trainee");
	    	TrainingType trainingType = new TrainingType();
	    	trainingType.setTrainingTypeName("trainingType");
	    	Trainee trainee = new Trainee();
	    	trainee.setUser(user1);	    	
	        TrainingDetailsDto validTrainingDetails = new TrainingDetailsDto();
	        validTrainingDetails.setTraineeUserName("trainee1");
	        validTrainingDetails.setTrainerUserName("trainer67");
	        validTrainingDetails.setTrainingTypeName("trainingType");
	        when(userRepo.findByUserName(eq("trainee1"))).thenReturn(Optional.empty());
	        assertThrows(UserException.class, () -> trainingService.saveTraining(validTrainingDetails));
	    }
	    @Test
	    void testSaveTraining_TrainerNotAllocatedToTrainee_ThrowsTrainerException() throws UserException, TrainingTypeException {
	    	Trainer trainer = new Trainer();
	    	User user2 = new User();
	    	user2.setUserName("trainer");
	    	trainer.setUser(user2);
	    	User user1 = new User();
	    	user1.setUserName("trainee");
	    	Trainee trainee = new Trainee();
	    	trainee.setUser(user1);	  
	    	trainee.setTrainersList(List.of(trainer));
	        TrainingDetailsDto validTrainingDetails = new TrainingDetailsDto();
	        validTrainingDetails.setTraineeUserName("trainee");
	        validTrainingDetails.setTrainerUserName("trainer2");
	        validTrainingDetails.setTrainingTypeName("trainingType");
	        when(userRepo.findByUserName(eq("trainee"))).thenReturn(Optional.of(user1));
	        assertThrows(TrainerException.class, () -> trainingService.saveTraining(validTrainingDetails));
	    }
	    
	    @Test
	    void testSaveTraining_TrainerNotAllocatedToTrainee_ThrowsTrainerException2() throws UserException, TrainingTypeException {
	    	User user1 = new User();
	    	user1.setUserName("trainee");
	    	User user2 = new User();
	    	user2.setUserName("trainer");
	    	Trainer trainer = new Trainer();
	    	trainer.setUser(user2);
	    	TrainingType trainingType = new TrainingType();
	    	trainingType.setTrainingTypeName("trainingType");
	    	trainer.setTrainingType(trainingType);
	    	Trainee trainee = new Trainee();
	    	trainee.setUser(user1);	    	
	    	trainee.setTrainersList(List.of(trainer));
	        TrainingDetailsDto validTrainingDetails = new TrainingDetailsDto();
	        validTrainingDetails.setTraineeUserName("trainee");
	        validTrainingDetails.setTrainerUserName("trainer89");
	        validTrainingDetails.setTrainingTypeName("trainingType");
 
	      when(userRepo.findByUserName(eq("trainee"))).thenReturn(Optional.of(user1));
	        assertThrows(TrainerException.class, () -> trainingService.saveTraining(validTrainingDetails));
	    }
	    
	    @Test
	    void testSaveTraining_TrainerNotAllocatedToTrainee_ThrowsTrainerException4() throws TrainerException {
	    	User user1 = new User();
	    	user1.setUserName("trainee");
	    	User user2 = new User();
	    	user2.setUserName("trainer");
	    	Trainer trainer = new Trainer();
	    	trainer.setUser(user2);
	    	TrainingType trainingType = new TrainingType();
	    	trainingType.setTrainingTypeName("trainingType100");
	    	trainer.setTrainingType(trainingType);
	    	Trainee trainee = new Trainee();
	    	trainee.setUser(user1);	    	
	    	trainee.setTrainersList(List.of(trainer));
	        TrainingDetailsDto validTrainingDetails = new TrainingDetailsDto();
	        validTrainingDetails.setTraineeUserName("trainee");
	        validTrainingDetails.setTrainerUserName("trainer");
	        validTrainingDetails.setTrainingTypeName("trainingType");
       when(userRepo.findByUserName(eq("trainee"))).thenReturn(Optional.of(user1));
     when(userRepo.findByUserName(eq("trainer"))).thenReturn(Optional.of(user2));
	        assertThrows(TrainerException.class, () -> trainingService.saveTraining(validTrainingDetails));
	    }
	     
	      
 

}
