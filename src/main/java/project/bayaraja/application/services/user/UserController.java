package project.bayaraja.application.services.user;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bayaraja.application.services.user.interfaces.UserService;
import project.bayaraja.application.services.user.request.UserCreateDto;
import project.bayaraja.application.utils.BaseResponse;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping(path = "/create-user")
    public ResponseEntity<BaseResponse<UserEntity>> createUser(@RequestBody @Valid UserCreateDto dto) {
        UserEntity savedUser = this.userService.createUser(dto);

        BaseResponse<UserEntity> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                savedUser
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/index-users")
    public ResponseEntity<BaseResponse<Page<UserEntity>>> getUserWithPaged(@ParameterObject Pageable pageable){
        Page<UserEntity> indexPagedUser = this.userService.getAllUser(pageable);

        BaseResponse<Page<UserEntity>> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                indexPagedUser
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/index-user")
    public ResponseEntity<BaseResponse<UserEntity>> getUserById(
            @Parameter(name = "userId", example = "1", required = true)
            @RequestParam("userId")
            Integer userId
    ){
        UserEntity userGet = this.userService.getUserById(userId);

        BaseResponse<UserEntity> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                userGet
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/soft-delete-user")
    public ResponseEntity<BaseResponse<Map<String, String>>> softDeleteUserById(
            @Parameter(name = "userId", example = "1", required = true)
            @RequestParam("userId")
            Integer userId
    ) {
        var deletedUser = this.userService.softDeleteUser(userId);

        BaseResponse<Map<String, String>> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                deletedUser
        );

        return ResponseEntity.ok(response);
    }
}
