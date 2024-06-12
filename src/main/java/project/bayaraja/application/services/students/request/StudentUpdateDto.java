package project.bayaraja.application.services.students.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateDto {

    private String name;
    private String address;
    private String phone;
    private MultipartFile profile_picture;
}