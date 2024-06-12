package project.bayaraja.application.services.class_student;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.bayaraja.application.exceptions.DataNotFoundException;
import project.bayaraja.application.services.class_student.dto.request.CreateClassStudentDto;
import project.bayaraja.application.services.class_student.interfaces.ClassStudentRepository;
import project.bayaraja.application.services.class_student.interfaces.ClassStudentService;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.students.interfaces.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ClassStudentServiceImpl implements ClassStudentService {

    private final ClassStudentRepository classStudentRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public ClassStudentEntity createSingleClass(CreateClassStudentDto createDto) {
        var mappedToEntity = this.modelMapper.map(createDto, ClassStudentEntity.class);

        return this.classStudentRepository.save(mappedToEntity);
    }

    @Override
    public List<ClassStudentEntity> createMultiClass(List<CreateClassStudentDto> createMultiDto) {
        List<ClassStudentEntity> createdClasses = createMultiDto
                .stream()
                .map(createDto -> this.modelMapper.map(createDto, ClassStudentEntity.class))
                .collect(Collectors.toList());

        return this.classStudentRepository.saveAll(createdClasses);
    }

    @Override
    public ClassStudentEntity getDetailClass(Integer idClass) {
        return this.classStudentRepository
                .findClassStudentDetailById(idClass)
                .orElseThrow(() -> new DataNotFoundException("Id class student not found"));
    }

    @Override
    public ClassStudentEntity addStudentsToClass(Integer classId, List<Integer> studentIds) {
        // Temukan ClassStudentEntity berdasarkan classId
        ClassStudentEntity classStudent = classStudentRepository
                .findById(classId)
                .orElseThrow(() -> new DataNotFoundException("Class with id " + classId + " not found"));

        // Temukan semua StudentEntity berdasarkan studentIds
        List<StudentEntity> studentsList = studentRepository.findAllById(studentIds);

        // Perbarui setiap StudentEntity dengan class_id dan class_student yang baru
        studentsList.forEach(student -> {
            this.studentRepository.findById(student.getId()).orElseThrow(() -> new DataNotFoundException("Student id not found "+student.getId()));

            student.setClass_id(classStudent.getId());
            student.setClass_student(classStudent);
        });

        // Simpan semua StudentEntity yang diperbarui
        List<StudentEntity> savedStudents = studentRepository.saveAll(studentsList);

        // Set siswa yang diperbarui ke ClassStudentEntity
        classStudent.setStudents(savedStudents);

        // Simpan dan kembalikan ClassStudentEntity yang diperbarui
        return classStudentRepository.save(classStudent);
    }
}
