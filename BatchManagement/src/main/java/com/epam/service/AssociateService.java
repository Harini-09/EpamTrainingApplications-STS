package com.epam.service;

import java.util.List;

import com.epam.customexceptions.AssociateException;
import com.epam.dtos.AssociateDto;

public interface AssociateService {
	public AssociateDto saveAssociate(AssociateDto associateDto);
	public List<AssociateDto> getAssociateByGender(String gender);
	public AssociateDto updateAssociate(int id,AssociateDto associateDto) throws AssociateException;
	public void deleteAssociate(int id);
}
