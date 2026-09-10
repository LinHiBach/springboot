package vn.itstar.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import vn.itstar.services.IEmailService;

@Service
public class EmailService implements IEmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@itstar.vn}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String toEmail, String otp, int expiryMinutes) {
        // Luôn in nổi bật ra Console để phục vụ kiểm thử nhanh (kể cả khi không có kết nối SMTP)
        System.out.println("================================================================");
        System.out.println(">>> [DEV TEST CONSOLE OTP]");
        System.out.println(">>> Gửi tới Email : " + toEmail);
        System.out.println(">>> MÃ XÁC MINH OTP: [" + otp + "]");
        System.out.println(">>> Thời hạn hiệu lực: " + expiryMinutes + " phút");
        System.out.println("================================================================");

        log.info(">>> Đang gửi OTP [{}] tới email: {}, hạn {} phút", otp, toEmail, expiryMinutes);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("[Xác thực tài khoản] Mã xác nhận OTP của bạn");
            message.setText(
                "Xin chào,\n\n"
                + "Mã xác minh OTP để kích hoạt tài khoản của bạn là:\n\n"
                + "    " + otp + "\n\n"
                + "Mã này có hiệu lực trong vòng " + expiryMinutes + " phút.\n"
                + "Vui lòng không chia sẻ mã này cho bất kỳ ai.\n\n"
                + "Nếu bạn không thực hiện yêu cầu này, xin vui lòng bỏ qua email.\n\n"
                + "Trân trọng,\n"
                + "Ban Quản Trị Hệ Thống"
            );

            mailSender.send(message);
            log.info(">>> Đã gửi email OTP thực tế thành công tới: {}", toEmail);
        } catch (Exception ex) {
            // Không làm gián đoạn luồng đăng ký nếu gửi mail gặp lỗi, OTP đã in ra console để dev test
            log.warn(">>> Không thể gửi email thật tới {} ({}), nhưng mã OTP [{}] đã được in ra console để test.",
                    toEmail, ex.getMessage(), otp);
        }
    }
}
