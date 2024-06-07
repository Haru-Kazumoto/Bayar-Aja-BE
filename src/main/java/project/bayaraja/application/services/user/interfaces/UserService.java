package project.bayaraja.application.services.user.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.request.UserCreateDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserEntity createUser(UserCreateDto bodyDto);
    Page<UserEntity> getAllUser(Pageable pageable);
    UserEntity getUserById(Integer userId);
//    UserEntity updateUser(Integer userId, );
    Map<String, String> softDeleteUser(Integer userId);
    Map<String, String> hardDeleteUser(Integer userId);

}
