package vn.itstar.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserModel {

    @NotBlank(message = "Vui lòng nhập tên đăng nhập")
    @Pattern(regexp = "[a-zA-Z0-9_.-]{3,50}", message = "Tên đăng nhập gồm 3–50 ký tự: chữ, số, dấu _, . hoặc -")
    private String username;

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email tối đa 255 ký tự")
    private String email;

    @Size(max = 72, message = "Mật khẩu tối đa 72 ký tự")
    private String password;

    @NotNull(message = "Vui lòng chọn quyền")
    @Pattern(regexp = "ADMIN|USER", message = "Quyền không hợp lệ")
    private String role = "USER";

    @Min(0)
    @Max(1)
    private int status = 1;

    public UserModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
