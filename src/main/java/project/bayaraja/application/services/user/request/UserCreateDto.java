package project.bayaraja.application.services.user.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.bayaraja.application.enums.Roles;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class UserCreateDto {
    @NotEmpty() @NotNull()
    private String phone_number;

    @NotEmpty() @NotNull()
    private String password;

    @NotNull()
    private Roles role;
}
