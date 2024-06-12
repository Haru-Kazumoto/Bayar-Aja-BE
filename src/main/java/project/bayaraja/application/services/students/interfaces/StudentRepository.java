package project.bayaraja.application.services.students.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.bayaraja.application.services.students.StudentEntity;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    @Query("SELECT s FROM StudentEntity s WHERE s.major = :majorClass ORDER BY s.name ASC")
    List<StudentEntity> findStudentsByMajorClass(@Param("majorClass") String majorClass);

    @Query("SELECT s FROM StudentEntity s WHERE s.grade = :grade AND s.major = :major ORDER BY s.name ASC")
    Page<StudentEntity> findStudentsByGradeAndMajor(String grade, String major, Pageable pageable);

    @Query("SELECT s FROM StudentEntity s WHERE s.id = :id")
    List<StudentEntity> findAllStudentById(@Param("id") Integer id);
}
