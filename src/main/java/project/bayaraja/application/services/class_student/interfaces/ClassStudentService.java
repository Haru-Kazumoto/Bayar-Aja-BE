package project.bayaraja.application.services.class_student.interfaces;

import project.bayaraja.application.services.class_student.ClassStudentEntity;
import project.bayaraja.application.services.class_student.dto.request.CreateClassStudentDto;

import java.util.List;

public interface ClassStudentService {
    ClassStudentEntity createSingleClass(CreateClassStudentDto createDto);
    List<ClassStudentEntity> createMultiClass(List<CreateClassStudentDto> createMultiDto);
//    List<ClassStudentEntity> addStudentsToClass(Integer classId, List<Integer> studentIds);
    ClassStudentEntity addStudentsToClass(Integer classId, List<Integer> studentIds);
    ClassStudentEntity getDetailClass(Integer idClass);
//    ClassStudentEntity updateClass(Integer idClass, UpdateClassCreateInfoDto updateDto);
//    void deleteClass(Integer idClass);
//    void validateMajor(String major);
}
