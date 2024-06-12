package project.bayaraja.application.services.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project.bayaraja.application.services.students.request.StudentUpdateDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto {
    private String username;
    private String password;
    private MultipartFile profile_picture;
    private StudentUpdateDto student;
}
