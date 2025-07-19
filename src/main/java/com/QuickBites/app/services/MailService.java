package com.QuickBites.app.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.QuickBites.app.Exception.EmailMessagingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    OTPService otpService;
    
    @Value("${quickbites.frontend.base-url}")
    private String frontendBaseUrl; 

    private final JavaMailSender mailSender;
    private final int OTP_VALIDITY_PERIOD = 5;
    private final String emailSender = "kar81056@gmail.com";

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // --- Method for new user registration ---
    @Async("taskExecutor")
    public void sendMail(String to) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(emailSender, "Quick Bites Support");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("Your QuickBites Verification Code");

            String otp = otpService.generateOTP();
            String content = emailTemplateBuilder(otp);
            mimeMessageHelper.setText(content, true);

            otpService.saveOTP(otp, to);
            mailSender.send(mimeMessage);
            System.out.println("Sending mail with thread"+Thread.currentThread().getName());
        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new EmailMessagingException("Error with sending Mail", ex);
        }
    }

    // --- Method for "Forgot Password" flow ---
    @Async("taskExecutor")
    public void sendPasswordResetOtpEmail(String email, String otp) {
        try {
            MimeMessage resetPasswordMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessage = new MimeMessageHelper(resetPasswordMessage, true, "UTF-8");
            mimeMessage.setFrom(emailSender, "QuickBites Password Reset");
            mimeMessage.setTo(email);
            mimeMessage.setSubject("Your password reset code for QuickBites");
            String content = emailTemplateBuilderResetPassword(otp);
            mimeMessage.setText(content, true);
            mailSender.send(resetPasswordMessage);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new EmailMessagingException("Error with sending Mail", ex);
        }
    }

    // --- Method for "Change Password" (when logged in) ---
    @Async("taskExecutor")
    public void sendPasswordChangeOtp(String to, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(emailSender, "Quick Bites Support");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("Your Password Change Confirmation Code");
            String content = emailTemplateBuilder(otp);
            mimeMessageHelper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new EmailMessagingException("Error with sending password change OTP", ex);
        }
    }

    // --- Private helper method to avoid duplicating email sending code ---
    private void sendCustomEmail(String to, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(emailSender, "Quick Bites Support");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new EmailMessagingException("Error with sending custom email", ex);
        }
    }

    // --- New public method to send the OTP for an email change ---
    @Async("taskExecutor")
    public void sendEmailChangeVerificationOtp(String newEmailAddress, String otp) {
        String emailContent = emailTemplateBuilder(otp);
        sendCustomEmail(
            newEmailAddress,
            "Verify Your New Email for QuickBites",
            emailContent
        );
    }

    @Async("taskExecutor")
    public  void sendAgentApprovalEmail(String email,String userName) {
        try {
       
          
            String content = emailTemplateBuilderAgentApproved(userName);
            String subject = " You're officially a QuickBites Delivery Agent!";

         
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(emailSender, "Quick Bites Support");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);

        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new EmailMessagingException("Error sending delivery agent approval email", ex);
        }
    }

    
    @Async("taskExecutor")
    public void sendAgentRejectionEmail(String email, String reason) {
        try {
            String userName = email.contains("@") ? email.substring(0, email.indexOf("@")) : "Applicant";
            String content = emailTemplateBuilderAgentRejected(userName, reason);
            String subject = "QuickBites – Delivery Agent Application Status";

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(emailSender, "Quick Bites Support");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new EmailMessagingException("Failed to send agent rejection email", ex);
        }
    }

    
    
    // --- New public method to send the security alert for an email change ---
    @Async("taskExecutor")
    public void sendEmailChangeAlert(String oldEmailAddress) {
        String alertContent = """
            <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h3>Security Alert</h3>
                <p>A request has been made to change the email address for your QuickBites account.</p>
                <p>If this was you, please check your new email's inbox for a verification code.</p>
                <p><b>If you did NOT make this request, please contact support immediately.</b></p>
                <p>Thank you,<br>The QuickBites Team</p>
            </div>
            """;
        sendCustomEmail(
            oldEmailAddress,
            "Security Alert: Email Change Initiated",
            alertContent
        );
    }
    @Async("taskExecutor")
    public void sendAgentRejectionWithReapplyLinkEmail(String email, String reason, String token) {
        try {
            String userName = email.contains("@") ? email.substring(0, email.indexOf("@")) : "Applicant";
            
            String reapplyUrl = frontendBaseUrl + "/agent/reapply/" + token; 

            String content = emailTemplateBuilderAgentReapply(userName, reason, reapplyUrl);
            String subject = "Update on your QuickBites Application & Chance to Reapply";

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(emailSender, "Quick Bites Support");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new EmailMessagingException("Failed to send agent reapply email", ex);
        }
    }

    // --- TEMPLATE BUILDERS ---

    public String emailTemplateBuilder(String verificationCode) {
        return """
                  <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333333;">
                <div style="max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #dddddd; border-radius: 5px;">
                    <h2 style="color: #007bff; text-align: center;">Account Verification Request</h2>
                    <p>Hello,</p>
                    <p>We received a request to verify your QuickBites account associated with this email address.</p>
                    <p>Please use the following One-Time Password (OTP) to complete your action:</p>
                    <p style="background-color: #f8f9fa; border-left: 5px solid #007bff; padding: 15px; font-size: 24px; font-weight: bold; text-align: center; margin: 25px 0;">
                        %s
                    </p>
                    <p>This OTP is valid for <strong>%d minutes</strong>. For your security, please do not share this OTP with anyone.</p>
                    <p>If you did not request this action, you can safely ignore this email.</p>
                    <p>Thank you,<br>The QuickBites Team</p>
                </div>
                <div style="text-align: center; font-size: 12px; color: #777777; margin-top: 20px;">
                    This is an automated message. Please do not reply directly to this email.<br>
                    QuickBites © %d
                </div>
            </div>
                """.formatted(verificationCode, OTP_VALIDITY_PERIOD, java.time.Year.now().getValue());
    }

    public String emailTemplateBuilderResetPassword(String otp) {
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
    
    
    
    public String emailTemplateBuilderAgentApproved(String userName) {
        return """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 620px; margin: 25px auto; padding: 30px; border: 1px solid #e0e0e0; border-radius: 10px; background-color: #ffffff;">
                    <h2 style="text-align: center; color: #28a745;">🎉 Welcome to QuickBites Delivery Team!</h2>
                    <p>Dear %s,</p>
                    <p>We’re excited to inform you that your application to become a Delivery Agent with <strong>QuickBites</strong> has been <span style="color: #28a745;"><strong>approved</strong></span> by our team!</p>
                    <p>You are now officially part of our fast-moving, customer-centric delivery network. 🚀</p>
                    <p style="margin-top: 20px;">Here’s what’s next:</p>
                    <ul style="margin-left: 20px;">
                        <li>✅ Set up your delivery profile</li>
                        <li>✅ Get assigned orders in your region</li>
                        <li>✅ Start earning and delivering with confidence</li>
                    </ul>
                    <p>If you have any questions or need help getting started, feel free to reach out to our support team.</p>
                    <p>Thank you for choosing to ride with us.<br>We’re thrilled to have you on board!</p>
                    <p style="margin-top: 30px;">Warm regards,<br><strong>The QuickBites Team</strong></p>
                </div>
                <div style="text-align: center; font-size: 12px; color: #777; margin-top: 20px;">
                    This is an automated message. Please do not reply directly to this email.<br>
                    QuickBites © %d
                </div>
            </div>
            """.formatted(userName, java.time.Year.now().getValue());
    }

    
    public String emailTemplateBuilderAgentRejected(String userName, String reason) {
        return """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 620px; margin: 25px auto; padding: 30px; border: 1px solid #e0e0e0; border-radius: 10px; background-color: #ffffff;">
                    <h2 style="text-align: center; color: #dc3545;">🚫 Delivery Agent Application Rejected</h2>
                    <p>Dear %s,</p>
                    <p>Thank you for applying to become a Delivery Agent with <strong>QuickBites</strong>.</p>
                    <p>After careful review, we regret to inform you that your application has been <span style="color: #dc3545;"><strong>rejected</strong></span>.</p>
                    <p><strong>Reason provided:</strong></p>
                    <blockquote style="background-color: #f8d7da; border-left: 5px solid #dc3545; padding: 12px; font-style: italic; margin: 20px 0;">
                        %s
                    </blockquote>
                    <p>If you believe this was a mistake or you'd like to reapply after making corrections, you are welcome to initiate a new application.</p>
                    <p>We appreciate your interest in QuickBites and wish you all the best.</p>
                    <p style="margin-top: 30px;">Warm regards,<br><strong>The QuickBites Team</strong></p>
                </div>
                <div style="text-align: center; font-size: 12px; color: #777; margin-top: 20px;">
                    This is an automated message. Please do not reply directly to this email.<br>
                    QuickBites © %d
                </div>
            </div>
            """.formatted(userName, reason, java.time.Year.now().getValue());
    }
    
    private String emailTemplateBuilderAgentReapply(String userName, String reason, String reapplyUrl) {
        return """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 620px; margin: 25px auto; padding: 30px; border: 1px solid #e0e0e0; border-radius: 10px; background-color: #ffffff;">
                    <h2 style="text-align: center; color: #ffc107;">Update on Your QuickBites Application</h2>
                    <p>Dear %s,</p>
                    <p>Thank you for your interest in becoming a Delivery Agent with <strong>QuickBites</strong>. After reviewing your application, we found some information that needs to be updated.</p>
                    <p><strong>Reason provided:</strong></p>
                    <blockquote style="background-color: #fff3cd; border-left: 5px solid #ffc107; padding: 12px; font-style: italic; margin: 20px 0;">
                        %s
                    </blockquote>
                    <p>The good news is, we are giving you an opportunity to correct your application and resubmit it.</p>
                    <p style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #007bff; color: white; padding: 15px 25px; text-decoration: none; border-radius: 5px; font-size: 16px;">Re-apply Now</a>
                    </p>
                    <p>Please note that this link is valid for <strong>24 hours</strong> only and can only be used once.</p>
                    <p>We appreciate your cooperation and look forward to your updated application.</p>
                    <p style="margin-top: 30px;">Best regards,<br><strong>The QuickBites Team</strong></p>
                </div>
                <div style="text-align: center; font-size: 12px; color: #777; margin-top: 20px;">
                    This is an automated message. Please do not reply directly to this email.<br>
                    QuickBites © %d
                </div>
            </div>
            """.formatted(userName, reason, reapplyUrl, java.time.Year.now().getValue());
    }

    
}