package project.bayaraja.application.services.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.bayaraja.application.services.user.interfaces.UserService;
import project.bayaraja.application.services.user.request.UserCreateDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override @Transactional
    public UserEntity createUser(UserCreateDto bodyDto) {
        Optional<UserEntity> userPhoneNumber = this.userRepository.findByUsername(
                bodyDto.getUsername()
        );

        if(userPhoneNumber.isPresent()) throw new IllegalArgumentException("Phone number already in used");

        bodyDto.setPassword(this.passwordEncoder.encode(
                bodyDto.getPassword()
        ));

        UserEntity savingData = this.modelMapper.map(bodyDto, UserEntity.class);

        return this.userRepository.save(savingData);
    }

    @Override
    public Page<UserEntity> getAllUser(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    public UserEntity getUserById(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override @Transactional
    public Map<String, String> softDeleteUser(Integer userId) {
        Map<String, String> res = new HashMap<>();

        this.userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Id not found"));
        this.userRepository.deleteById(userId);

        res.put("message", "user success to delete");

        return res;
    }

    @Override @Transactional
    public Map<String, String> hardDeleteUser(Integer userId) {
        return null;
    }
}
