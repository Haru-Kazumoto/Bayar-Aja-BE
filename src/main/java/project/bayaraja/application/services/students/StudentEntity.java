package project.bayaraja.application.services.students;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import project.bayaraja.application.services.payments.PaymentEntity;
import project.bayaraja.application.services.spp.SppEntity;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.utils.DateSerializer;
import project.bayaraja.application.utils.Timestamps;

import java.time.Year;
import java.util.Date;
import java.util.List;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
@Entity @Table(name = "students")
public class StudentEntity extends Timestamps {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", columnDefinition = "TEXT", nullable = true)
    private String address;

    @Column(name = "join_at", nullable = false)
    @JsonSerialize(using = DateSerializer.class)
    @Builder.Default
    private Date join_at = new Date();

    @Column(name = "year_period", nullable = true)
    private String year_period; //this year + 3

    @Column(name = "is_graduate", nullable = false) @Builder.Default
    private Boolean is_graduate = false;

    //-----------RELATIONS

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private UserEntity user;

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