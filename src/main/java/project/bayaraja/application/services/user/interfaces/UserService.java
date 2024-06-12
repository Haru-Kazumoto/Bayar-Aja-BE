package project.bayaraja.application.services.user.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.request.ChangePasswordDto;
import project.bayaraja.application.services.user.request.UserCreateDto;
import project.bayaraja.application.services.user.request.UserUpdateDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    Page<UserEntity> getAllUser(Pageable pageable);
    void changePassword(Integer userId, ChangePasswordDto passwordDto);
    void changePasswordSession(ChangePasswordDto passwordDto);
    UserEntity getUserById(Integer userId);
    UserEntity updateUser(Integer userId, UserUpdateDto updateDto);
    Map<String, String> softDeleteUser(Integer userId);
}
