package com.QuickBites.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.services.DeliveryAgentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/agent")
@Tag(name="Delivery-Agent ")
public class DeliveryAgentController {
	
	@Autowired
	DeliveryAgentService deliveryAgentService;
	
	@Autowired
	DeliveryAgentRepository deliveryAgentRepo;
	
	
	
}
