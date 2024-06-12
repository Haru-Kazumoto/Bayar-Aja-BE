package project.bayaraja.application.services.students;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bayaraja.application.services.students.interfaces.StudentRepository;
import project.bayaraja.application.services.students.interfaces.StudentService;
import project.bayaraja.application.services.students.request.StudentUpdateDto;

import java.util.List;

@Service @RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public StudentEntity getDetailStudent(Integer studentId) {
        return null;
    }

    @Override
    public Page<StudentEntity> getStudents(Pageable pageable) {
        return null;
    }

    @Override
    public List<StudentEntity> getAllStudentByMajor(String major) {
        return this.studentRepository.findStudentsByMajorClass(major);
    }

    @Override
    public StudentEntity updateStudent(Integer studentId, StudentUpdateDto updateDto) {
        return null;
    }
}
