package project.bayaraja.application.services.invoices;

import jakarta.persistence.*;
import lombok.*;
import project.bayaraja.application.services.payments.PaymentEntity;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.utils.Timestamps;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "invoice")
public class InvoiceEntity extends Timestamps {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "invoice_name", nullable = false)
    private String invoice_name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "paid_value", nullable = false)
    private BigInteger paid_value;

    @Column(name = "added_date", nullable = false)
    private Date added_date;

    @Column(name = "payment_id", nullable = true)
    private Integer payment_id;

    //----------- RELATIONS

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "invoice"
    )
    private List<PaymentEntity> payments;

}
