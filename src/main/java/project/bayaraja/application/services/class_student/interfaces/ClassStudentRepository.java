package project.bayaraja.application.services.class_student.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.bayaraja.application.services.class_student.ClassStudentEntity;

import java.util.Optional;

public interface ClassStudentRepository extends JpaRepository<ClassStudentEntity, Integer> {

    @Query("SELECT c FROM ClassStudentEntity c " +
            "LEFT JOIN FETCH c.students s " +
            "WHERE c.id = :classId"
    )
    Optional<ClassStudentEntity> findClassStudentDetailById(@Param("classId") Integer classId);
}
