package project.bayaraja.application.services.auth.interfaces;

import project.bayaraja.application.services.auth.request.LoginRequest;
import project.bayaraja.application.services.auth.request.RegisterRequest;
import project.bayaraja.application.services.user.UserEntity;

public interface AuthService {

    UserEntity registerUser(RegisterRequest requestBody);

    UserEntity verifiedUser(Integer userId);

    UserEntity decodeSession();

}
