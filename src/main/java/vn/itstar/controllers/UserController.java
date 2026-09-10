package vn.itstar.controllers;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.itstar.entity.User;
import vn.itstar.models.UserModel;
import vn.itstar.services.impl.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword, Model model) {
        model.addAttribute("users", service.search(keyword));
        model.addAttribute("keyword", keyword.trim());
        return "admin/users/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("user", new UserModel());
        return "admin/users/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        User user = service.findById(id);
        UserModel form = new UserModel();
        form.setUsername(user.getUsername());
        form.setFullName(user.getFullName());
        form.setEmail(user.getEmail());
        form.setRole(user.getRole());
        form.setStatus(user.getStatus());
        model.addAttribute("user", form);
        model.addAttribute("userId", id);
        return "admin/users/form";
    }

    @PostMapping("/save")
    public String create(@Valid @ModelAttribute("user") UserModel form, BindingResult result,
                         Model model, RedirectAttributes redirect, Principal principal) {
        return save(null, form, result, model, redirect, principal);
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("user") UserModel form,
                         BindingResult result, Model model, RedirectAttributes redirect, Principal principal) {
        return save(id, form, result, model, redirect, principal);
    }

    private String save(Integer id, UserModel form, BindingResult result, Model model,
                        RedirectAttributes redirect, Principal principal) {
        if (id != null) {
            User current = service.findById(id);
            model.addAttribute("userId", id);
            if (current.getUsername().equalsIgnoreCase(principal.getName()) &&
                    (!current.getUsername().equals(form.getUsername()) || !"ADMIN".equals(form.getRole()) || form.getStatus() != 1)) {
                result.reject("self", "Không thể đổi tên đăng nhập, hạ quyền hoặc khóa tài khoản đang sử dụng.");
            }
        }
        String password = form.getPassword();
        if ((id == null && (password == null || password.isBlank())) ||
                (password != null && !password.isBlank() &&
                        (password.length() < 8 || password.getBytes(StandardCharsets.UTF_8).length > 72))) {
            result.rejectValue("password", "invalid", "Mật khẩu cần ít nhất 8 ký tự và tối đa 72 byte UTF-8.");
        }
        if (!result.hasFieldErrors("username") && service.usernameTaken(form.getUsername(), id)) {
            result.rejectValue("username", "duplicate", "Tên đăng nhập đã tồn tại.");
        }
        if (!result.hasFieldErrors("email") && service.emailTaken(form.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "Email đã tồn tại.");
        }
        if (result.hasErrors()) return "admin/users/form";
        try {
            service.save(id, form);
        } catch (DataIntegrityViolationException ex) {
            result.reject("duplicate", "Tên đăng nhập hoặc email đã tồn tại. Vui lòng kiểm tra lại.");
            return "admin/users/form";
        }
        redirect.addFlashAttribute("message", id == null ? "Thêm người dùng thành công!" : "Cập nhật người dùng thành công!");
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Principal principal, RedirectAttributes redirect) {
        User user = service.findById(id);
        if (user.getUsername().equalsIgnoreCase(principal.getName())) {
            redirect.addFlashAttribute("message", "Không thể xóa tài khoản đang sử dụng.");
        } else {
            try {
                service.delete(id);
                redirect.addFlashAttribute("message", "Xóa người dùng thành công!");
            } catch (DataIntegrityViolationException ex) {
                redirect.addFlashAttribute("message", "Không thể xóa người dùng đang được dữ liệu khác tham chiếu.");
            }
        }
        return "redirect:/admin/users";
    }
}

