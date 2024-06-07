package project.bayaraja.application.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import project.bayaraja.application.enums.Roles;
import project.bayaraja.application.services.auth.AuthController;
import project.bayaraja.application.services.auth.AuthServiceImpl;
import project.bayaraja.application.services.auth.request.LoginRequest;
import project.bayaraja.application.services.auth.request.RegisterRequest;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.students.StudentServiceImpl;
import project.bayaraja.application.services.students.request.StudentCreateDto;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.UserRepository;

import java.util.Date;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthServiceImpl authService;

    @MockBean
    private StudentServiceImpl studentService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SecurityContext context;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContextRepository securityContextRepository;

    @MockBean
    private SecurityContextHolderStrategy securityContextHolderStrategy;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private StudentCreateDto studentCreateDto;
    private UserEntity user;
    private StudentEntity student;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .phone_number("123")
                .password("321")
                .build();

        registerRequest = RegisterRequest.builder()
                .phone_number("0987")
                .password("123")
                .role(Roles.USER)
                .student(studentCreateDto)
                .build();

        studentCreateDto = StudentCreateDto.builder()
                .name("Navasa Salsabila Putri")
                .address("Surakata")
                .build();

        user = UserEntity.builder()
                .id(1)
                .phone_number("123")
                .password("321")
                .role(Roles.ADMIN)
                .build();

        student = StudentEntity.builder()
                .name("Navasa Salsabila putri")
                .address("Surakarta")
                .build();
    }

    @Test
    void shouldLoadApplicationContext() {
        assertNotNull(authService);
        assertNotNull(modelMapper);
        assertNotNull(authenticationManager);
        assertNotNull(securityContextRepository);
        assertNotNull(securityContextRepository);
    }

    @Test
    void shouldSuccessLoginWhenDataIsValid() throws Exception {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.getPhone_number(),
                loginRequest.getPassword()
        );
//
        Authentication authentication = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);

        given(authenticationManager.authenticate(token)).willReturn(authentication);
        given(securityContextHolderStrategy.createEmptyContext()).willReturn(context);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(loginRequest);

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                ).andExpect(status().isOk());

        verify(context).setAuthentication(authentication);
        verify(securityContextHolderStrategy).setContext(context);
        verify(securityContextRepository).saveContext(context, any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @Test
    void shouldSuccessRegistrationWhenDataIsValid() throws Exception {
        // Arrange
        given(userRepository.findByPhoneNumber(any(String.class))).willReturn(Optional.empty());
        given(authService.registerUser(any(RegisterRequest.class))).willAnswer(invocation -> {
            RegisterRequest request = invocation.getArgument(0);
            UserEntity userEntity = new UserEntity();
            userEntity.setPhone_number(request.getPhone_number());
            userEntity.setPassword(request.getPassword());
            if (request.getStudent() != null) {
                StudentEntity studentEntity = new StudentEntity();
                studentEntity.setJoin_at(new Date());
                studentEntity.setYear_period("2027");  // Example value
                userEntity.setStudent(studentEntity);
            }
            return userEntity;
        });

        given(studentService.createStudent(any())).willAnswer(invocation -> {
            StudentCreateDto dto = invocation.getArgument(0);
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setJoin_at(new Date());
            studentEntity.setYear_period("2027");  // Example value
            return studentEntity;
        });

        String phoneNumber = "1234567890";
        String password = "password";
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPhone_number(phoneNumber);
        registerRequest.setPassword(password);
        registerRequest.setStudent(new StudentCreateDto());

        // Act
        ResultActions response = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        );

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.phone_number").exists())
                .andExpect(jsonPath("$.payload.student").exists())
                .andDo(MockMvcResultHandlers.print());

        verify(studentService).createStudent(any(StudentCreateDto.class));
        verify(userRepository).save(any(UserEntity.class));
    }
}
