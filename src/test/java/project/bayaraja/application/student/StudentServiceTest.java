package project.bayaraja.application.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.students.StudentRepository;
import project.bayaraja.application.services.students.StudentServiceImpl;
import project.bayaraja.application.services.students.request.StudentCreateDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @Test
    void shouldCreateStudentWhenDataIsValid() {
        when(studentRepository.save(any(StudentEntity.class))).thenReturn(student);
        when(modelMapper.map(any(StudentCreateDto.class), eq(StudentEntity.class))).thenReturn(student);

        var result = studentService.createStudent(studentDto);

        verify(studentRepository).save(student);

        System.out.println(result.getYear_period());

        assertNotNull(result);
        assertNotNull(result.getYear_period());
        assertNotNull(result.getJoin_at());
        assertEquals("2027", result.getYear_period());
    }
}
