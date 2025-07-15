package com.QuickBites.app.mapper;

import java.math.BigDecimal;

import com.QuickBites.app.DTO.AgentResponseDTO;
import com.QuickBites.app.entities.DeliveryAgent;

public class DeliveryAgentMapper {

	
	public static AgentResponseDTO entityToDto(DeliveryAgent agent) {
	    AgentResponseDTO dto = new AgentResponseDTO();
	    dto.setFirstName(agent.getUser().getFirstName());
	    dto.setLastName(agent.getUser().getLastName());
	    dto.setUserName(agent.getUser().getUserName());
	    dto.setCitizenshipPhotoFront(agent.getCitizenshipPhotoFront());
	    dto.setCitizenshipPhotoBack(agent.getCitizenshipPhotoBack());
	    dto.setDrivingLicense(agent.getDrivingLicense());
	    dto.setEmail(agent.getUser().getEmail());
	    dto.setEarning(agent.getTotalEarning() != null ? agent.getTotalEarning() : BigDecimal.ZERO);
	    return dto;
	}
}
