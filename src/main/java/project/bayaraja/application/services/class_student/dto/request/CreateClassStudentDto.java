package project.bayaraja.application.services.class_student.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateClassStudentDto {
    @NotNull @NotEmpty
    private String class_name;
    @NotNull @NotEmpty
    private String class_major;
}
