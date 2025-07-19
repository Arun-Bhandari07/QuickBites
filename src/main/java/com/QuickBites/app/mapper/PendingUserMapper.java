package com.QuickBites.app.mapper;

import com.QuickBites.app.DTO.PendingUserReapplyDTO;
import com.QuickBites.app.entities.PendingUser;

public class PendingUserMapper {
	
//Maps a PendingUser entity to a DTO suitable for the reapply scene
	
	public static PendingUserReapplyDTO toReapplyDTO(PendingUser user)
	{
		if(user==null)
		{
			return null;
		}
		
		return new PendingUserReapplyDTO(
	            user.getFirstName(),
	            user.getLastName(),
	            user.getUserName(),
	            user.getEmail()
	        );
	}
}
