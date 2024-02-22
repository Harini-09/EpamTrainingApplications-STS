package com.epam.associateservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.epam.customexceptions.AssociateException;
import com.epam.dtos.AssociateDto;
import com.epam.entities.Associate;
import com.epam.entities.Batch;
import com.epam.repository.AssociateRepository;
import com.epam.repository.BatchRepository;
import com.epam.service.AssociateServiceImpl;

@ExtendWith(MockitoExtension.class)
class AssociateServiceTest {

	@Mock
	ModelMapper modelMapper;
	
	@Mock
	AssociateRepository associateRepo;
	
	@Mock
	BatchRepository batchRepo;
	
	@InjectMocks
	AssociateServiceImpl associateService;
	
	private Associate associate;
	
	private AssociateDto associateDto;
	
	private Batch batch;
	
	private Optional<Batch> optionalBatch;
	
	@BeforeEach
	void setUp() {
		batch = new Batch(1,"Java-Jan2023","java",null,null);
		associateDto = new AssociateDto("vidya","vidya@gmail.com","F","vmr","ACTIVE",batch);
		associate = new Associate(1,"vidya","vidya@gmail.com","F","vmr","ACTIVE",batch);
		optionalBatch = Optional.of(batch);
	}
	
	@Test
	void testSaveAssociate() {
		Mockito.when(modelMapper.map(associateDto, Associate.class)).thenReturn(associate);
		Mockito.when(batchRepo.findByBatchName("Java-Jan2023")).thenReturn(optionalBatch);
		Mockito.when(associateRepo.save(associate)).thenReturn(associate);
		Mockito.when(modelMapper.map(associate, AssociateDto.class)).thenReturn(associateDto);
		AssociateDto returnedAssociateDto = associateService.saveAssociate(associateDto);
		assertEquals(associateDto,returnedAssociateDto);
		}
	
	@Test 
	void testGetAssociateByGender() {
		List<Associate> associates = List.of(associate);
		List<AssociateDto> dtoAssociates = List.of(associateDto);
		Mockito.when(associateRepo.findAllByGender("F")).thenReturn(associates);
		Mockito.when(modelMapper.map(associate, AssociateDto.class)).thenReturn(associateDto);
		List<AssociateDto> returnedAssociates = associateService.getAssociateByGender("F");
		assertEquals(dtoAssociates,returnedAssociates);
	}
	
	@Test 
	void testUpdateAssociateWithoutException() throws Exception{
		Mockito.when(associateRepo.existsById(1)).thenReturn(true);
		Mockito.when(modelMapper.map(associateDto, Associate.class)).thenReturn(associate);
		Mockito.when(batchRepo.findByBatchName("Java-Jan2023")).thenReturn(optionalBatch);
		Mockito.when(associateRepo.save(associate)).thenReturn(associate);
		Mockito.when(modelMapper.map(associate, AssociateDto.class)).thenReturn(associateDto);
		AssociateDto returnedAssociateDto = associateService.updateAssociate(1,associateDto);
		assertEquals(associateDto,returnedAssociateDto);
	}
	
	@Test 
	void testUpdateAssociateWithException() throws Exception {
		Mockito.when(associateRepo.existsById(2)).thenReturn(false);
		assertThrows(AssociateException.class,()->associateService.updateAssociate(2, associateDto));
	} 
	
	@Test
	void testDeleteAssociate() {
		doNothing().when(associateRepo).deleteById(1);
		associateService.deleteAssociate(1);
		Mockito.verify(associateRepo).deleteById(1);
	}
	 
 
}
