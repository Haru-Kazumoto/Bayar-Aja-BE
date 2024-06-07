package project.bayaraja.application.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.bayaraja.application.enums.Roles;
import project.bayaraja.application.services.auth.AuthServiceImpl;
import project.bayaraja.application.services.auth.request.RegisterRequest;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.students.StudentRepository;
import project.bayaraja.application.services.students.request.StudentCreateDto;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.UserRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private ModelMapper modelMapper;

    private UserEntity user;
    private StudentEntity student;
    private RegisterRequest registerRequest;
    private StudentCreateDto studentCreateDto;

    @BeforeEach
    void setUp() {

        student = StudentEntity.builder()
            .id(1)
            .name("Navasa Salsabila Putri")
            .address("Surakarta")
            .join_at(new Date())
            .is_graduate(false)
            .year_period("2024")
            .user(user)
            .build();

        user = UserEntity.builder()
                .id(1)
                .phone_number("0987")
                .password("123")
                .role(Roles.USER)
                .is_verified(false)
                .verified_at(null)
                .student(student)
                .build();

        studentCreateDto = StudentCreateDto.builder()
                .name("Navasa Salsabila Putri")
                .address("Surakata")
                .build();

        registerRequest = RegisterRequest.builder()
                .phone_number("0987")
                .password("123")
                .role(Roles.USER)
                .student(studentCreateDto)
                .build();
    }

    @Test
    void shouldLoadApplicationContext() {
        assertNotNull(authService);
        assertNotNull(user);
        assertNotNull(student);
    }

    @Test
    void shouldRegisterAdminSuccessfullyWhenDataIsValid() {
        when(passwordEncoder.encode(anyString())).thenReturn("123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        when(modelMapper.map(any(RegisterRequest.class), eq(UserEntity.class))).thenReturn(user);

        UserEntity result = authService.registerUser(registerRequest);

        verify(passwordEncoder).encode("123");
        verify(userRepository).save(user);

        assertNotNull(result);
        assertEquals("123", result.getPassword());
    }

    @Test
    void shouldDecodeUserWhenUserIsLoggedIn() {
        UserDetails userDetails = User.withUsername("0987").password("123").roles("USER").build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByPhoneNumber("0987")).thenReturn(Optional.of(user));

        UserEntity result = authService.decodeSession();

        verify(userRepository).findByPhoneNumber("0987");

        assertNotNull(result);
        assertEquals("0987", result.getPhone_number());
    }

    @Test
    void shouldChangeStatusVerifiedWhenUserNotVerifiedYet() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        UserEntity result = authService.verifiedUser(1);

        assertNotNull(result);
        assertEquals(true, result.getIs_verified());
        assertNotNull(result.getVerified_at());
    }

    @Test
    void shouldErrorWhenPhoneNumberIsExist() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            authService.registerUser(registerRequest);
        });
    }
}
