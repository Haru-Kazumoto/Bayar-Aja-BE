package project.bayaraja.application.services.students;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import project.bayaraja.application.services.class_student.ClassStudentEntity;
import project.bayaraja.application.services.payments.PaymentEntity;
import project.bayaraja.application.services.spp.SppEntity;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.utils.DateSerializer;
import project.bayaraja.application.utils.Timestamps;
import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.Date;
import java.util.List;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
@Entity @Table(name = "students")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class StudentEntity extends Timestamps {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", columnDefinition = "TEXT", nullable = true)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "profile_picture", nullable = true)
    private String profile_picture;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "major", nullable = false)
    private String major;

    @Column(name = "join_at", nullable = false)
    @JsonSerialize(using = DateSerializer.class)
    @Builder.Default
    private Date join_at = new Date();

    @Column(name = "year_period", nullable = true)
    private String year_period; //this year + 3

    @Column(name = "is_graduate", nullable = false)
    @Builder.Default
    private Boolean is_graduate = false;

    @Column(name = "class_id", nullable = true)
    private Integer class_id; //for class student id

    //-----------RELATIONS

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_student_id")
    private ClassStudentEntity class_student;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "student"
    )
    private List<PaymentEntity> payments;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "student"
    )
    private List<SppEntity> sppBooks;

    @PrePersist
    public void setup(){
        Year thisYear = Year.now();

        this.year_period = String.valueOf(thisYear.getValue() + 3);
    }
}