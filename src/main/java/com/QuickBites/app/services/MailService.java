package com.QuickBites.app.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService  {
	
	@Autowired
	OTPService otpService;

	private final JavaMailSender mailSender;
	
	private final String emailSender = "kar81056@gmail.com";
	
	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail( String to) throws MessagingException, UnsupportedEncodingException{
		 MimeMessage mimeMessage =mailSender.createMimeMessage();
		 MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		 mimeMessageHelper.setFrom(emailSender,"Quick Bites Support");
		 mimeMessageHelper.setTo(to);
		 mimeMessageHelper.setSubject("Your QuickBites Verification Code");
		 
		 String otp = otpService.generateOTP();
		 String content = emailTemplateBuilder(otp);
		 mimeMessageHelper.setText(content,true);

		 otpService.saveOTP(otp, to);
		 mailSender.send(mimeMessage);
			
	}
	
	
	public String emailTemplateBuilder(String verificationCode) {
		return """
				<div>
		 			<h2>Account Verification - QuickBites</h2>
		 			<h4>Your Token: %s</h4>
		 			<h4>This token expires in 10 min</h4>
		 		</div>
		 		<p style="color: #7f8c8d;">If you didn't create an account, please ignore this email.</p>
            
            <hr style="border: 1px solid #ecf0f1; margin: 20px 0;">
            <p style="font-size: 12px; color: #95a5a6; text-align: center;">
                QuickBites Team<br>
                This is an automated message, please do not reply.
            </p>
				""".formatted(verificationCode);
	}
	
	
	
}
