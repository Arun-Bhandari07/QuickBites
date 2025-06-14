package com.QuickBites.app.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.QuickBites.app.Exception.EmailMessagingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService  {
	
	@Autowired
	OTPService otpService;

	private final JavaMailSender mailSender;
	
	private final int OTP_VALIDITY_PERIOD = 5;
	
	private final String emailSender = "kar81056@gmail.com";
	
	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail( String to){
		try {
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
		}catch(UnsupportedEncodingException | MessagingException ex) {
			throw new EmailMessagingException("Error with sending Mail",ex);
		}
		
	}
	
	
	public String emailTemplateBuilder(String verificationCode) {
		return """
				  <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333333;">
                <div style="max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #dddddd; border-radius: 5px;">
                    <h2 style="color: #007bff; text-align: center;">Account Verification Request</h2>
                    <p>Hello,</p>
                    <p>We received a request to verify your QuickBites account associated with this email address.</p>
                    <p>Please use the following One-Time Password (OTP) to verify your account registration:</p>
                    <p style="background-color: #f8f9fa; border-left: 5px solid #007bff; padding: 15px; font-size: 24px; font-weight: bold; text-align: center; margin: 25px 0;">
                        %s
                    </p>
                    <p>This OTP is valid for <strong>%d minutes</strong>. For your security, please do not share this OTP with anyone.</p>
                    <p>If you did not request a password reset, you can safely ignore this email. No changes will be made to your account unless this OTP is used.</p>
                    <p>Thank you,<br>The QuickBites Team</p>
                </div>
                <div style="text-align: center; font-size: 12px; color: #777777; margin-top: 20px;">
                    This is an automated message. Please do not reply directly to this email.<br>
                    QuickBites © %d
                </div>
            </div>
				""".formatted(verificationCode,OTP_VALIDITY_PERIOD,java.time.Year.now().getValue());
	}
	
	public void sendPasswordResetOtpEmail(String email,String otp){
		try {
			MimeMessage resetPasswordMessage=mailSender.createMimeMessage();
			MimeMessageHelper mimeMessage=new MimeMessageHelper(resetPasswordMessage,true,"UTF-8");
			mimeMessage.setFrom(emailSender,"QuickBites Password Reset");
			mimeMessage.setTo(email);
			mimeMessage.setSubject("Your password reset code for QuickBites");
			String content = emailTemplateBuilderResetPassword(otp);
			mimeMessage.setText(content,true);
			mailSender.send(resetPasswordMessage);
		}catch(UnsupportedEncodingException | MessagingException ex) {
			throw new EmailMessagingException("Error with sending Mail",ex);
		}
		
	}
	
	public String emailTemplateBuilderResetPassword(String otp)
	{
		int otpValidityMinutes = 5; 
        return """
            <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333333;">
                <div style="max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #dddddd; border-radius: 5px;">
                    <h2 style="color: #007bff; text-align: center;">Password Reset Request</h2>
                    <p>Hello,</p>
                    <p>We received a request to reset the password for your QuickBites account associated with this email address.</p>
                    <p>Please use the following One-Time Password (OTP) to reset your password:</p>
                    <p style="background-color: #f8f9fa; border-left: 5px solid #007bff; padding: 15px; font-size: 24px; font-weight: bold; text-align: center; margin: 25px 0;">
                        %s
                    </p>
                    <p>This OTP is valid for <strong>%d minutes</strong>. For your security, please do not share this OTP with anyone.</p>
                    <p>If you did not request a password reset, you can safely ignore this email. No changes will be made to your account unless this OTP is used.</p>
                    <p>Thank you,<br>The QuickBites Team</p>
                </div>
                <div style="text-align: center; font-size: 12px; color: #777777; margin-top: 20px;">
                    This is an automated message. Please do not reply directly to this email.<br>
                    QuickBites © %d
                </div>
            </div>
            """.formatted(otp, otpValidityMinutes, java.time.Year.now().getValue());
    }
	
}
