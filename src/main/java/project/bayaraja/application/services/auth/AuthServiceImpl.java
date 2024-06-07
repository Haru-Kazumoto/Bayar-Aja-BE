package project.bayaraja.application.services.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.bayaraja.application.exceptions.BadRequestException;
import project.bayaraja.application.exceptions.DataNotFoundException;
import project.bayaraja.application.services.auth.interfaces.AuthService;
import project.bayaraja.application.services.auth.request.RegisterRequest;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.students.StudentRepository;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.UserRepository;
import project.bayaraja.application.services.user.interfaces.UserService;
import project.bayaraja.application.services.user.request.UserCreateDto;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserEntity registerUser(RegisterRequest bodyDto) {
        Optional<UserEntity> userPhoneNumber = this.userRepository.findByPhoneNumber(
                bodyDto.getPhone_number()
        );

        this.checkPhoneNumber(userPhoneNumber);

        bodyDto.setPassword(this.passwordEncoder.encode(bodyDto.getPassword()));

        UserEntity savingData = this.modelMapper.map(bodyDto, UserEntity.class);

        return this.userRepository.save(savingData);
    }

    @Override @Transactional
    public UserEntity verifiedUser(Integer userId) {
        UserEntity userData = this.userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User id not found")
        );

        this.acceptUser(userData);

        return userData;
    }

    @Override
    public UserEntity decodeSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        var userData = (User)authentication.getPrincipal();

        return userRepository.findByPhoneNumber(userData.getUsername()).orElseThrow(
                () -> new BadCredentialsException("Username is not found.")
        );
    }

    //----------- UTILS
    private void acceptUser(UserEntity dataUser){
        if(dataUser.getIs_verified()) throw new BadRequestException(String.format("User has verified at %s", dataUser.getVerified_at()));

        dataUser.setIs_verified(true);
        dataUser.setVerified_at(new Date());
    }

    private void checkPhoneNumber(Optional<UserEntity> dataUser){
        if(dataUser.isPresent()) throw new IllegalArgumentException("Phone number already in used");
    }
}
