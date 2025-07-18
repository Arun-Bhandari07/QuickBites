package com.QuickBites.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.services.DeliveryAgentService;

@Controller
@RequestMapping("/agent")
public class DeliveryAgentController {
	
	@Autowired
	DeliveryAgentService deliveryAgentService;
	
	@Autowired
	DeliveryAgentRepository deliveryAgentRepo;

	@PutMapping("/{id}/online")
	public ResponseEntity<String> setOnline(@PathVariable(name="id") Long id){
			return deliveryAgentService.setOnline(id);
	}
	
	@PutMapping("/{id}/offline")
	public ResponseEntity<String> setOffline(@PathVariable(name="id") Long id){
		return deliveryAgentService.setOffline(id);
	}
	
	@PutMapping("/{id}/hearbeat")
	public ResponseEntity<String> updateLastSeen(@PathVariable(name="id") Long id){
		return deliveryAgentService.updateLastSeen(id);
	}
}
