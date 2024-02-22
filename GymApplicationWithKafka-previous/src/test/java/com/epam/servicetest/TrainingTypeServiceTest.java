package com.epam.servicetest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.entities.TrainingType;
import com.epam.repository.TrainingTypeRepository;
import com.epam.service.TrainingTypeServiceImpl;

@ExtendWith(MockitoExtension.class) 
public class TrainingTypeServiceTest {
	
	@Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    public void testSaveTrainingType_ValidInput_Success() {
        String trainingTypeName = "Technical";
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(trainingTypeName);

        when(trainingTypeRepository.save(trainingType)).thenReturn(trainingType);
        trainingTypeService.saveTrainingType(trainingTypeName);
         verify(trainingTypeRepository).save(any(TrainingType.class));
    }

}
