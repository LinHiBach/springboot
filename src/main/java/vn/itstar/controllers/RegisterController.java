package vn.itstar.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.itstar.models.RegisterModel;
import vn.itstar.services.IUserRegisterService;

@Controller
public class RegisterController {

    private final IUserRegisterService registerService;

    public RegisterController(IUserRegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new RegisterModel());
        }
        return "web/register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("user") RegisterModel form,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        // Kiểm tra mật khẩu khớp
        if (form.getPassword() != null && form.getConfirmPassword() != null
                && !form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Mật khẩu xác nhận không khớp.");
        }

        // Kiểm tra trùng username
        if (!result.hasFieldErrors("username") && registerService.isUsernameTaken(form.getUsername())) {
            result.rejectValue("username", "duplicate", "Tên đăng nhập đã tồn tại.");
        }

        // Kiểm tra trùng email
        if (!result.hasFieldErrors("email") && registerService.isEmailTaken(form.getEmail())) {
            result.rejectValue("email", "duplicate", "Email này đã được đăng ký.");
        }

        if (result.hasErrors()) {
            return "web/register";
        }

        try {
            registerService.register(form);
            redirectAttributes.addFlashAttribute("message", "Đăng ký thành công! Mã OTP đã được gửi tới email của bạn.");
            return "redirect:/verify-otp?email=" + form.getEmail().trim();
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "web/register";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình đăng ký: " + ex.getMessage());
            return "web/register";
        }
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpForm(@RequestParam(value = "email", required = false) String email,
                                    Model model) {
        if (email == null || email.isBlank()) {
            return "redirect:/register";
        }
        model.addAttribute("email", email.trim());
        return "web/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String handleVerifyOtp(@RequestParam("email") String email,
                                  @RequestParam("otp") String otp,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (email == null || email.isBlank()) {
            return "redirect:/register";
        }

        if (otp == null || otp.trim().isBlank()) {
            model.addAttribute("email", email.trim());
            model.addAttribute("errorMessage", "Vui lòng nhập mã OTP gồm 6 chữ số.");
            return "web/verify-otp";
        }

        try {
            registerService.verifyOtp(email.trim(), otp.trim());
            redirectAttributes.addFlashAttribute("successMessage", "Tài khoản của bạn đã được kích hoạt thành công! Hãy đăng nhập để tiếp tục.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("email", email.trim());
            model.addAttribute("errorMessage", ex.getMessage());
            return "web/verify-otp";
        } catch (Exception ex) {
            model.addAttribute("email", email.trim());
            model.addAttribute("errorMessage", "Không thể xác thực mã OTP: " + ex.getMessage());
            return "web/verify-otp";
        }
    }

    @PostMapping("/resend-otp")
    public String handleResendOtp(@RequestParam("email") String email,
                                  RedirectAttributes redirectAttributes) {
        if (email == null || email.isBlank()) {
            return "redirect:/register";
        }

        try {
            registerService.resendOtp(email.trim());
            redirectAttributes.addFlashAttribute("message", "Mã OTP mới đã được gửi tới email " + email.trim() + "!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/verify-otp?email=" + email.trim();
    }
}
