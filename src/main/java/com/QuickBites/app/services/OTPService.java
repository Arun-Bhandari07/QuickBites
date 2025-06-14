package com.QuickBites.app.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.QuickBites.app.Exception.OtpValidationException;
import com.QuickBites.app.entities.OTPVerification;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.entities.RoleName;
import com.QuickBites.app.repositories.OTPrepository;
import com.QuickBites.app.repositories.PendingUserRepository;

import jakarta.transaction.Transactional;

@Service
public class OTPService {
	
	@Autowired
	OTPrepository otpRepo;
	
	@Autowired
	UserRegistrationService userRegistration;
	
	@Autowired
	PendingUserRepository pendingUserRepo;

	public String generateOTP() {
		SecureRandom secureRandom = new SecureRandom();
		int secureIntInRange = secureRandom.nextInt(999999);
		String myOTP = String.format("%06d", secureIntInRange);
		return myOTP;
	}
	
	public void saveOTP(String OTP, String email) {
		deleteOTPByEmail(email);   //delete previous otp of this email before adding new
		OTPVerification otpVerification = new OTPVerification();
		otpVerification.setOtp(OTP);
		otpVerification.setEmail(email);
		otpRepo.save(otpVerification);
	}
	
	@Transactional
	public void deleteOTPByEmail(String email) {	
	List<OTPVerification> emailList = otpRepo.findAllByEmail(email);
	otpRepo.deleteAll(emailList);  // no exception is thrown even if list is empty(for new email)
		
	}
	
	
	public boolean verifyOTP(String email , String otp) {
		Optional<OTPVerification> otpVerify = otpRepo.findByEmail(email);
		//check if otp exists for email
		if(otpVerify.isEmpty() || otp.isBlank()) {
			throw new OtpValidationException("Otp verification failed: The Otp provided was empty.");
		}
		OTPVerification otpRecord = otpVerify.get();
		
		//check if otp expired
		if(otpRecord.getExpiryAt().isBefore(LocalDateTime.now())) {
			throw new OtpValidationException("OTP verification failed: The OTP has expired. Please request a new one.");		}
		
		if(!otpRecord.getOtp().equals(otp)) {
			throw new OtpValidationException("OTP verification failed: The OTP you entered is incorrect.");
		}
		
		PendingUser pendingUser = pendingUserRepo.findByEmail(email)
									.orElseThrow(()->new RuntimeException("User doesn't exists in pending Area"));

		pendingUser.setOtpVerified(true);
		pendingUserRepo.save(pendingUser);
		
		if(isDeliveryAgent(pendingUser)) {
			userRegistration.notifyAdminForApproval(pendingUser);
		}else {
			userRegistration.registerCustomer(email);
		}
		return true;
	}
	
	//clean expired OTPs
	@Scheduled(fixedRate=60*60*1000)
	@Transactional
	public void cleanUpExpiredOTPs() {
		LocalDateTime now = LocalDateTime.now();
		List<OTPVerification> expiredOTPs = otpRepo.findByExpiryAtBefore(now);
		otpRepo.deleteAll(expiredOTPs);
	}
	
	public boolean isDeliveryAgent(PendingUser pendingUser) {
			return pendingUser.getRoleName().equals(RoleName.ROLE_DELIVERYAGENT) && pendingUser.getRoleName()!=null;		
	}
	
}
