package vn.itstar.services;

import vn.itstar.models.RegisterModel;

public interface IUserRegisterService {
    void register(RegisterModel form);
    boolean verifyOtp(String email, String otp);
    void resendOtp(String email);
    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
}
