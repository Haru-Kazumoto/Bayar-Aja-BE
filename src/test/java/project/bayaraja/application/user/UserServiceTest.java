package project.bayaraja.application.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import project.bayaraja.application.enums.Roles;
import project.bayaraja.application.exceptions.DataNotFoundException;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.interfaces.UserRepository;
import project.bayaraja.application.services.user.UserServiceImpl;
import project.bayaraja.application.services.user.request.UserCreateDto;
import project.bayaraja.application.services.user.request.UserUpdateDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    private UserCreateDto userCreateDto;
    private UserEntity userEntity;
    private List<UserEntity> users;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("0987");
        userCreateDto.setPassword("password");

        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("0987");
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(Roles.USER);

        users = List.of(
                UserEntity.builder().username("0987").password("encode").role(Roles.USER).build(),
                UserEntity.builder().username("0986").password("encode").role(Roles.USER).build(),
                UserEntity.builder().username("0985").password("encode").role(Roles.USER).build()
        );
    }

    @Test
    void shouldLoadApplicationContext() {
        assertNotNull(userRepository);
        assertNotNull(userService);
    }

    @Test
    void shouldErrorUpdateUserWhenDataIsNotFound() {
        lenient().when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            userService.updateUser(1, new UserUpdateDto());
        });
    }

    @Test
    void shouldSuccessUpdateWhenUserDataIsValid() {
        UserEntity existingUser = new UserEntity();
        existingUser.setId(1);
        existingUser.setUsername("oldUsername");

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setUsername("newUsername");
        updateDto.setPassword("newPassword");
        updateDto.setProfile_picture(mock(MultipartFile.class));
        when(updateDto.getProfile_picture().getOriginalFilename()).thenReturn("newProfilePic.jpg");

        UserEntity mappedUser = new UserEntity();
        mappedUser.setUsername("oldUsername");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(existingUser, UserEntity.class)).thenReturn(mappedUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(mappedUser);

        UserEntity updatedUser = userService.updateUser(1, updateDto);

        assertEquals("newUsername", updatedUser.getUsername());
        assertEquals("encodedPassword", updatedUser.getPassword());
        assertEquals("newProfilePic.jpg", updatedUser.getProfile_picture());

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findByUsername("newUsername");
        verify(modelMapper, times(1)).map(existingUser, UserEntity.class);
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(mappedUser);
    }

    @Test
    void shouldReturnPagedUsersWhenGetByPagination() {
        Page<UserEntity> pagedUsers = new PageImpl<>(users);

        int pageNumber = 0;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by("created_at").ascending());

        when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedUsers);

        Page<UserEntity> result = userService.getAllUser(pageable);

        verify(userRepository).findAll(pageable);

        assertNotNull(result);
        assertEquals(3, result.getNumberOfElements());
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(users, result.getContent());
    }

    @Test
    void shouldReturnUserByIdWhenFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.getUserById(1);

        verify(userRepository).findById(1);

        assertNotNull(result);
        assertEquals("0987", result.getUsername());
    }

    @Test
    void shouldSoftDeleteUserWhenDeleteUserById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));

        Map<String, String> result = userService.softDeleteUser(1);

        verify(userRepository).findById(1);
        verify(userRepository).deleteById(1);

        assertEquals("user success to delete", result.get("message"));
    }
}
