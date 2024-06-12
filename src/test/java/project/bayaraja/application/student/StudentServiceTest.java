package project.bayaraja.application.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.students.interfaces.StudentRepository;
import project.bayaraja.application.services.students.StudentServiceImpl;
import project.bayaraja.application.services.students.request.StudentCreateDto;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentServiceImpl studentService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ModelMapper modelMapper;

    private StudentEntity student;
    private StudentCreateDto studentDto;

    @BeforeEach
    void setUp() {
        student = StudentEntity.builder()
                .id(1)
                .name("Navasa Salsabila Putri")
                .address("Surakarta")
                .build();

        studentDto = StudentCreateDto.builder()
                .name("Navasa Salsabila Putri")
                .address("Surakarta")
                .build();
    }

    @Test
    void shouldLoadApplicationContext() {
        assertNotNull(studentService);
        assertNotNull(studentRepository);
    }

}