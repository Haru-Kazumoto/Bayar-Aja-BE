package project.bayaraja.application.services.auth.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.bayaraja.application.services.auth.request.LoginRequest;
import project.bayaraja.application.services.auth.request.RegisterRequest;
import project.bayaraja.application.services.user.UserEntity;

public interface AuthService {

    UserEntity registerUser(RegisterRequest requestBody);

    void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);

    UserEntity verifiedUser(Integer userId);

    UserEntity decodeSession();

}
