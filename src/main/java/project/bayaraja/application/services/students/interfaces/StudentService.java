package project.bayaraja.application.services.students.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.students.request.StudentCreateDto;
import project.bayaraja.application.services.students.request.StudentUpdateDto;

import java.util.List;

public interface StudentService {
    StudentEntity getDetailStudent(Integer studentId);
    Page<StudentEntity> getStudents(Pageable pageable);
    List<StudentEntity> getAllStudentByMajor(String major);
    StudentEntity updateStudent(Integer studentId, StudentUpdateDto updateDto);
}
