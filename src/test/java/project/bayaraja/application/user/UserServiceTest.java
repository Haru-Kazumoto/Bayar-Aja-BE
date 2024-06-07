package project.bayaraja.application.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.bayaraja.application.enums.Roles;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.UserRepository;
import project.bayaraja.application.services.user.UserServiceImpl;
import project.bayaraja.application.services.user.request.UserCreateDto;

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
        userCreateDto = new UserCreateDto();
        userCreateDto.setPhone_number("0987");
        userCreateDto.setPassword("password");

        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setPhone_number("0987");
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(Roles.USER);

        users = List.of(
                UserEntity.builder().phone_number("0987").password("encode").role(Roles.USER).build(),
                UserEntity.builder().phone_number("0986").password("encode").role(Roles.USER).build(),
                UserEntity.builder().phone_number("0985").password("encode").role(Roles.USER).build()
        );
    }

    @Test
    void shouldLoadApplicationContext() {
        assertNotNull(userRepository);
        assertNotNull(userService);
    }

    @Test
    void shouldCreateUserSuccessfullyWhenDataIsValid() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(modelMapper.map(any(UserCreateDto.class), eq(UserEntity.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity createdUser = userService.createUser(userCreateDto);

        verify(userRepository).findByPhoneNumber("0987");
        verify(passwordEncoder).encode("password");
        verify(modelMapper).map(userCreateDto, UserEntity.class);
        verify(userRepository).save(userEntity);

        assertNotNull(createdUser);
        assertEquals("0987",createdUser.getPhone_number());
    }

    @Test
    void shouldErrorWhenPhoneNumberIsDuplicate() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(userEntity));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userCreateDto);
        });

        verify(userRepository).findByPhoneNumber("0987");
        verify(passwordEncoder, never()).encode(anyString());
        verify(modelMapper, never()).map(any(UserCreateDto.class), eq(UserEntity.class));
        verify(userRepository, never()).save(any(UserEntity.class));
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
        assertEquals("0987", result.getPhone_number());
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
