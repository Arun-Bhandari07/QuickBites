package com.QuickBites.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.AgentReapplyRequest;
import com.QuickBites.app.DTO.PendingUserReapplyDTO;
import com.QuickBites.app.Exception.InvalidTokenException;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.mapper.PendingUserMapper;
import com.QuickBites.app.repositories.ReapplyTokenRepository;
import com.QuickBites.app.services.ReapplyService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/reapply")
@Tag(name = "Agent Reapplication", description = "Endpoints for agents to re-submit their applications")

public class ReapplyController {
	@Autowired
	private ReapplyTokenRepository reapplyTokenRepo;

	@Autowired
	private ReapplyService reapplyService;

	
	@GetMapping("/validate/{token}")
	public ResponseEntity<?> validateTokenAndGetUserData(@PathVariable(name="token") String token) {
		try {
			PendingUser user = reapplyService.getPendingUserDetailsByToken(token);
			PendingUserReapplyDTO requestDto = PendingUserMapper.toReapplyDTO(user);
			return ResponseEntity.ok(requestDto);
		} catch (InvalidTokenException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
	}

	

	@PostMapping(path = "/submit/{token}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> submitReapplication(@PathVariable (name="token") String token, AgentReapplyRequest request) {
		try {
			reapplyService.processReapplication(token, request);
			return ResponseEntity
					.ok(Map.of("message", "YOur reapplication has been resubmitted sucessfully for review"));
		} catch (ResourceAlreadyExistsException | InvalidTokenException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "An unexpected server error occured please try again later"));
		}
	}

}
