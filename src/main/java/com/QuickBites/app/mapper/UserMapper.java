package com.QuickBites.app.mapper;

import org.springframework.stereotype.Component;

import com.QuickBites.app.DTO.UserProfileDTO;
import com.QuickBites.app.entities.User;
@Component
public class UserMapper {

	public UserProfileDTO populateUserProfileDTO(User user)
	{
		if(user==null)
		{
			return null;
		}
		UserProfileDTO dto = new UserProfileDTO();
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setAddress(user.getAddress());
		dto.setEmail(user.getEmail());
		dto.setPhone(user.getPhone());
		dto.setUsername(user.getUserName());
		return dto;
	}
}
