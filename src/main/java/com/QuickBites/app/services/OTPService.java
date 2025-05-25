package com.QuickBites.app.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuickBites.app.entities.OTPVerification;
import com.QuickBites.app.repositories.OTPrepository;

import jakarta.transaction.Transactional;

@Service
public class OTPService {
	
	@Autowired
	OTPrepository otpRepo;

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
		if(otpVerify.isEmpty()) {
			throw new RuntimeException("Invalid request");
		}
		OTPVerification otpRecord = otpVerify.get();
		
		//check if otp expired
		if(otpRecord.getExpiryAt().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Otp expired");
		}
		
		if(!otpRecord.getOtp().equals(otp)) {
			throw new RuntimeException("Invalid OTP");
		}
		
		return true;
	}
	
	
	
	
}
