package project.bayaraja.application.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.bayaraja.application.enums.Roles;
import project.bayaraja.application.services.user.UserController;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.UserServiceImpl;
import project.bayaraja.application.services.user.request.UserCreateDto;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity user;
    private UserCreateDto userDto;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1)
                .username("0987")
                .password("123")
                .role(Roles.USER)
                .build();

        userDto = UserCreateDto.builder()
                .username("0987")
                .password("123")
                .role(Roles.USER)
                .build();
    }

    @Test
    void shouldLoadApplicationContext() {
        assertNotNull(userService);
    }

    @Test
    void shouldReturnSingleUserWhenDataExist() throws Exception {
        int idUser = 1;
        given(userService.getUserById(ArgumentMatchers.anyInt())).willReturn(user);

        ResultActions response = mockMvc.perform(get("/api/user/index-user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(idUser))
                .content(this.objectMapper.writeValueAsString(user))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.payload.id").exists())
                .andExpect(jsonPath("$.payload.phone_number").exists())
                .andExpect(jsonPath("$.payload.id", CoreMatchers.is(user.getId())))
                .andExpect(jsonPath("$.payload.phone_number", CoreMatchers.is(user.getUsername())))
                .andDo(MockMvcResultHandlers.print());
    }
}
