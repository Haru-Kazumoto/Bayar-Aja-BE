package project.bayaraja.application.services.students;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.bayaraja.application.services.students.interfaces.StudentService;
import project.bayaraja.application.services.students.request.StudentCreateDto;

import java.time.Year;
import java.util.Date;

@Service @RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override @Transactional
    public StudentEntity createStudent(StudentCreateDto requestDto) {
        StudentEntity student = this.modelMapper.map(requestDto, StudentEntity.class);

        return this.studentRepository.save(student);
    }
}
