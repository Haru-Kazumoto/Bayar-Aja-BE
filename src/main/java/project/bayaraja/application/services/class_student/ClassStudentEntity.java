package project.bayaraja.application.services.class_student;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.utils.DateSerializer;
import project.bayaraja.application.utils.Timestamps;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "class_student") @Builder
public class ClassStudentEntity extends Timestamps {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_name", nullable = false)
    private String class_name;

    @Column(name = "class_major", nullable = false)
    private String class_major; //find major in lookup with type MAJOR_CLASS

    @Column(name = "added_date", nullable = false)
    @JsonSerialize(using = DateSerializer.class)
    @Builder.Default
    private Date added_date = new Date();

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "class_student"
    )
    private List<StudentEntity> students;
}
