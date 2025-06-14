package com.QuickBites.app.services;

import org.springframework.stereotype.Service;

import com.QuickBites.app.entities.PendingUser;

@Service
public class AdminNotificationService {	
	public void notifyAdmin(PendingUser pendingUser) {
		System.out.println("Received user for approval"+pendingUser.getUserName());
	}
}
