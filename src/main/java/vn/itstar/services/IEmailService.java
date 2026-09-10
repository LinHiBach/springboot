package vn.itstar.services;

public interface IEmailService {
    void sendOtpEmail(String toEmail, String otp, int expiryMinutes);
}
