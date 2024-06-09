package project.bayaraja.application.services.auth.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull()
    @NotEmpty()
    private String username;

    @NotNull()
    @NotEmpty()
    private String password;
}
