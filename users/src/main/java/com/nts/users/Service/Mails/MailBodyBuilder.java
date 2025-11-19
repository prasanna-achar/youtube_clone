package com.nts.users.Service.Mails;

public class MailBodyBuilder {

    public static String buildVerificationEmail(String username, String otp) {
        // HTML email for OTP
        String htmlBody = "<!DOCTYPE html>" +
                "<html>" +
                "<body>" +
                "<h2>Hello " + username + ",</h2>" +
                "<p>Thank you for registering on NoteSpring!</p>" +
                "<p>Your One-Time Password (OTP) for email verification is:</p>" +
                "<h3 style='color: #2E86C1; font-size: 24px;'>" + otp + "</h3>" +
                "<p>This OTP will expire in 20 minutes.</p>" +
                "<br>" +
                "<p>If you did not register, please ignore this email.</p>" +
                "</body>" +
                "</html>";

        return htmlBody;
    }

    public static String buildResetPasswordEmail(String username, String token) {
        // Backend endpoint with token as path variable
        String resetLink = "http://localhost:3000/auth/reset-password/" + token;

        return "<!DOCTYPE html>" +
                "<html>" +
                "<body>" +
                "<h2>Hello " + username + ",</h2>" +
                "<p>We received a request to reset your NoteSpring account password.</p>" +
                "<p>Click the button below to reset your password:</p>" +
                "<a href='" + resetLink + "' " +
                "style='display:inline-block; background-color:#C0392B; color:#ffffff; padding:10px 20px; " +
                "text-decoration:none; border-radius:5px; font-size:16px;'>Reset Password</a>" +
                "<p>This link will expire in 20 minutes.</p>" +
                "<br>" +
                "<p>If you did not request a password reset, please ignore this email.</p>" +
                "</body>" +
                "</html>";
    }

}

