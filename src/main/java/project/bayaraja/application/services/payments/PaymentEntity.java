package project.bayaraja.application.services.payments;

import jakarta.persistence.*;
import lombok.*;
import project.bayaraja.application.services.invoices.InvoiceEntity;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.utils.Timestamps;

import java.util.Date;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "payment")
public class PaymentEntity extends Timestamps {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "paid_date", nullable = true)
    private Date paid_date;

    @Column(name = "payment_status", nullable = false)
    @Builder.Default
    private Boolean payment_status = false;

    @Column(name = "payment_image", nullable = false)
    private String payment_image;

    //----------------------- RELATIONS

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

}
