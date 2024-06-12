package project.bayaraja.application.services.class_student.dto.response;

import project.bayaraja.application.services.students.response.DetailStudentResponse;

import java.util.List;

public record DetailClassStudentResponse(
        String class_name,
        String class_major,
        List<DetailStudentResponse> students
){}
