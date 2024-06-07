package project.bayaraja.application.services.students.interfaces;

import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.students.request.StudentCreateDto;

public interface StudentService {
    StudentEntity createStudent(StudentCreateDto requestDto);
}
