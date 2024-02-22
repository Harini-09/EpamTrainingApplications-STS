package com.epam.associaterestcontroller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.epam.dtos.AssociateDto;
import com.epam.entities.Batch;
import com.epam.restcontroller.AssociateRestController;
import com.epam.service.AssociateService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AssociateRestController.class)
class AssociateRestControllerTest {

	@MockBean
	private AssociateService associateService;
	
	@Autowired
	private MockMvc mockMvc;
			
	private AssociateDto associateDto;
	
	private Batch batch;

	@BeforeEach
	void setUp() {
		batch = new Batch(1,"Java-Jan2023","java",new Date(),new Date());
		associateDto = new AssociateDto("vidya","vidya@gmail.com","F","vmr","ACTIVE",batch);
	}
	
	@Test
	void testGetAssociatesByGender() throws Exception {
		List<AssociateDto> dtoAssociates = List.of(associateDto);
		Mockito.when(associateService.getAssociateByGender("F")).thenReturn(dtoAssociates);
		MvcResult mvcResult = mockMvc.perform(get("/rd/associates/{gender}","F"))
								.andExpect(status().isOk())
								.andReturn();
		assertNotNull(mvcResult); 
	}
	
	@Test
	void testSaveAssociate() throws Exception {
		Mockito.when(associateService.saveAssociate(associateDto)).thenReturn(associateDto);
		mockMvc.perform(post("/rd/associates")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(associateDto)))
			.andExpect(status().isCreated());
	}
	
	@Test
	void testUpdateAssociate() throws Exception {
		Mockito.when(associateService.updateAssociate(1,associateDto)).thenReturn(associateDto);
		mockMvc.perform(put("/rd/associates?id=1") 
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(associateDto)))
			.andExpect(status().isOk());
	} 
	
	@Test
	void testDeleteAssociate() throws Exception {
		doNothing().when(associateService).deleteAssociate(1);
		mockMvc.perform(delete("/rd/associates/{id}",1))
				.andExpect(status().isNoContent());
	}
} 
 