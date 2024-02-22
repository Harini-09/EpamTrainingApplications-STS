package com.epam.dtos.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationDto {

	private String subject;
	private String body;
	private List<String> toEmails;
	private List<String> ccEmails;

}
