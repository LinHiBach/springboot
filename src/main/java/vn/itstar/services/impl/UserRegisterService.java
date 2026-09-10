package vn.itstar.services.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.itstar.entity.User;
import vn.itstar.models.RegisterModel;
import vn.itstar.repositories.IUserRepository;
import vn.itstar.services.IEmailService;
import vn.itstar.services.IUserRegisterService;

@Service
@Transactional
public class UserRegisterService implements IUserRegisterService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;
    private final SecureRandom random = new SecureRandom();

    @Value("${app.otp.expiry-minutes:5}")
    private int expiryMinutes;

    public UserRegisterService(IUserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               IEmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameTaken(String username) {
        Optional<User> existing = userRepository.findByUsernameIgnoreCase(username.trim());
        // Nếu đã tồn tại tài khoản đang hoạt động
        return existing.isPresent() && existing.get().getStatus() == 1;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        Optional<User> existing = userRepository.findByEmailIgnoreCase(email.trim());
        // Nếu đã tồn tại tài khoản đang hoạt động
        return existing.isPresent() && existing.get().getStatus() == 1;
    }

    @Override
    public void register(RegisterModel form) {
        String trimmedUsername = form.getUsername().trim();
        String trimmedEmail = form.getEmail().trim();

        // Kiểm tra xem email đã tồn tại nhưng chưa kích hoạt không
        Optional<User> optUser = userRepository.findByEmailIgnoreCase(trimmedEmail);
        User user;
        if (optUser.isPresent()) {
            user = optUser.get();
            if (user.getStatus() == 1) {
                throw new IllegalArgumentException("Email này đã được sử dụng bởi một tài khoản đang hoạt động.");
            }
        } else {
            // Kiểm tra username nếu tạo mới
            Optional<User> userByUsername = userRepository.findByUsernameIgnoreCase(trimmedUsername);
            if (userByUsername.isPresent()) {
                if (userByUsername.get().getStatus() == 1) {
                    throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
                }
                user = userByUsername.get();
            } else {
                user = new User();
            }
        }

        user.setUsername(trimmedUsername);
        user.setFullName(form.getFullName().trim());
        user.setEmail(trimmedEmail);
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole("USER");
        user.setStatus(0); // Chưa kích hoạt

        // Sinh mã OTP 6 số
        String otp = String.format("%06d", random.nextInt(1000000));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(expiryMinutes));

        userRepository.saveAndFlush(user);

        // Gửi email OTP
        emailService.sendOtpEmail(user.getEmail(), otp, expiryMinutes);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        if (email == null || otp == null) {
            return false;
        }

        Optional<User> optUser = userRepository.findByEmailIgnoreCase(email.trim());
        if (optUser.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy thông tin đăng ký của email này.");
        }

        User user = optUser.get();

        if (user.getStatus() == 1) {
            return true; // Đã kích hoạt trước đó
        }

        if (user.getOtp() == null || !user.getOtp().equals(otp.trim())) {
            throw new IllegalArgumentException("Mã OTP không chính xác.");
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Mã OTP đã hết hạn. Vui lòng bấm 'Gửi lại mã OTP'.");
        }

        // Kích hoạt tài khoản
        user.setStatus(1);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.saveAndFlush(user);

        return true;
    }

    @Override
    public void resendOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }

        Optional<User> optUser = userRepository.findByEmailIgnoreCase(email.trim());
        if (optUser.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy thông tin đăng ký của email này.");
        }

        User user = optUser.get();
        if (user.getStatus() == 1) {
            throw new IllegalArgumentException("Tài khoản này đã được kích hoạt trước đó. Bạn có thể đăng nhập ngay.");
        }

        String newOtp = String.format("%06d", random.nextInt(1000000));
        user.setOtp(newOtp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(expiryMinutes));
        userRepository.saveAndFlush(user);

        emailService.sendOtpEmail(user.getEmail(), newOtp, expiryMinutes);
    }
}
