package com.QuickBites.app.services;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.QuickBites.app.repositories.ReapplyTokenRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenCleanupService {
	public static final Logger logger = LoggerFactory.getLogger(TokenCleanupService.class);
	@Autowired
	private ReapplyTokenRepository reapplyTokenRepo;

	/*
	 * deletes the expired tokens from the database expiry is 24hrs
	 */
	
	@PostConstruct
	@Transactional
	public void runCleanupOnStartup()
	{
		logger.info("Running initial cleanup for expired tokens on application startup..");
		cleanupExpiredReapplyTokens();
	}
	
	@Scheduled(fixedRateString = "${token.cleanup.rate.in.milliseconds:86400000}")
	@Transactional
	public void cleanupExpiredReapplyTokens() {
		logger.info("Running scheduled cleanup for the expired tokens...");
		try {
			reapplyTokenRepo.deleteAllByExpiresAtBefore(Instant.now());
			logger.info("Expired reapply tokens cleanup complete");
		} catch (Exception e) {
			logger.error("Error during expired re-apply token cleanup", e);

		}
	}
}
