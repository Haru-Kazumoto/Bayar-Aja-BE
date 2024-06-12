package project.bayaraja.application.services.students.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class StudentCreateDto {

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String address;

    @NotEmpty
    @NotNull
    private String phone;

    @NotEmpty
    @NotNull
    private String grade;

    @NotEmpty
    @NotNull
    private String major;

    @Nullable
    private MultipartFile profile_picture;
}
