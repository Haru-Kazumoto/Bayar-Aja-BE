package project.bayaraja.application.services.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bayaraja.application.services.auth.interfaces.AuthService;
import project.bayaraja.application.services.auth.request.LoginRequest;
import project.bayaraja.application.services.auth.request.RegisterRequest;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.utils.BaseResponse;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping(path = "/register")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterRequest.class)
                            )
                    },
                    description = "OK RESPONSE"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RegisterRequest.class)
                            )
                    },
                    description = "BAD REQUEST RESPONSE"
            )
    })
    @Operation(summary = "Registration Account", description = "Registration new account for user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse<UserEntity>> registerUser(@RequestBody @Valid RegisterRequest requestBody){
        var registerData = this.authService.registerUser(requestBody);

        BaseResponse<UserEntity> response = new BaseResponse<>(
                Collections.singletonList("OK"),
                registerData
        );

        return ResponseEntity.ok().body(response);
    }
    
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class)
                            )
                    },
                    description = "ACCEPTED RESPONSE"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RegisterRequest.class)
                            )
                    },
                    description = "BAD REQUEST RESPONSE"
            )
    })
    @Operation(summary = "Login user", description = "User verified credential")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        this.authService.login(loginRequest, request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(){
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/decode-session")
    public ResponseEntity<?> decodeSession(){
        return ResponseEntity.ok().body(this.authService.decodeSession());
    }
}