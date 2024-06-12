package project.bayaraja.application.services.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.bayaraja.application.exceptions.DataNotFoundException;
import project.bayaraja.application.exceptions.DuplicateDataException;
import project.bayaraja.application.services.auth.interfaces.AuthService;
import project.bayaraja.application.services.user.interfaces.UserRepository;
import project.bayaraja.application.services.user.interfaces.UserService;
import project.bayaraja.application.services.user.request.ChangePasswordDto;
import project.bayaraja.application.services.user.request.UserUpdateDto;

import java.util.HashMap;
import java.util.Map;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserDetailsService userDetailsService;

    /**
     * TEST: [PASSED]
     * Get all user with pagination
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<UserEntity> getAllUser(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    /**
     * TEST: [PASSED]
     * Get single user by id user
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity getUserById(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("Not found"));
    }

    /**
     * TEST: [NOT TESTED]
     * Change password user student
     *
     * @param userId
     * @param passwordDto
     */
    @Override @Transactional
    public void changePassword(Integer userId, ChangePasswordDto passwordDto) {
        var findUserFirst = this.userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User not found")
        );

        findUserFirst.setPassword(this.passwordEncoder.encode(passwordDto.getPassword()));

        this.userRepository.save(findUserFirst);
    }

    /**
     * TEST: [NOT TESTED]
     * Only change password for sesion
     *
     * @param passwordDto
     */
    @Override
    public void changePasswordSession(ChangePasswordDto passwordDto) {
        try {
            var user = this.authService.decodeSession();

            user.setPassword(this.passwordEncoder.encode(passwordDto.getPassword()));

            userRepository.save(user);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());

            UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(newToken);
        } catch(Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Test : [NOT TESTED]
     * Update user object with user id
     *
     * @param userId
     * @param updateDto
     * @return UserUpdateDto
     */
    @Override @Transactional
    public UserEntity updateUser(Integer userId, UserUpdateDto updateDto) {
        var findUser = this.userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));
        var findUsername = this.userRepository.findByUsername(updateDto.getUsername());

        if(findUsername.isPresent()) throw new DuplicateDataException("Phone number already in use");
        if(updateDto.getUsername().equals(findUser.getUsername())) throw new DuplicateDataException("Phone number cannot same as old");

        var mappedUser = this.modelMapper.map(findUser, UserEntity.class);

        mappedUser.setUsername(updateDto.getUsername());
        mappedUser.setPassword(this.passwordEncoder.encode(updateDto.getPassword()));
        mappedUser.setProfile_picture(updateDto.getProfile_picture().getOriginalFilename());

        return this.userRepository.save(mappedUser);
    }

    /**
     * TEST: [PASSED]
     * Deleting user by user id with soft delete method
     *
     * @param userId
     * @return
     */
    @Override @Transactional
    public Map<String, String> softDeleteUser(Integer userId) {
        Map<String, String> res = new HashMap<>();

        this.userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Id not found"));
        this.userRepository.deleteById(userId);

        res.put("message", "user success to delete");

        return res;
    }
}
