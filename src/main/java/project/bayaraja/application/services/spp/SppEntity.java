package project.bayaraja.application.services.spp;

import jakarta.persistence.*;
import lombok.*;
import project.bayaraja.application.services.invoice_perdate.InvoicePerDateEntity;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.utils.Timestamps;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "spp")
public class SppEntity extends Timestamps {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "year_period", nullable = false)
    private String year_period;

    @Column(name = "is_paid_off", nullable = false)
    @Builder.Default
    private Boolean is_paid_off = false;

    //------------- RELATIONS

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "spp_book"
    )
    private List<InvoicePerDateEntity> invoicePerDates;


}
