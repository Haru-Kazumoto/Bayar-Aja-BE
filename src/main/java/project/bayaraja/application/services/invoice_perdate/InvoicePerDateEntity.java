package project.bayaraja.application.services.invoice_perdate;

import jakarta.persistence.*;
import lombok.*;
import project.bayaraja.application.services.spp.SppEntity;
import project.bayaraja.application.utils.Timestamps;

import java.math.BigInteger;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "invoice_per_date")
public class InvoicePerDateEntity extends Timestamps {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_name", nullable = false)
    private String date_name;

    @Column(name = "payment_image", nullable = false)
    private String payment_image;

    @Column(name = "value", nullable = false)
    private BigInteger value;

    @Column(name = "paid_value", nullable = true)
    private BigInteger paid_value;


    @Column(name = "remaining_payment", nullable = true)
    private BigInteger remaining_payment;

    @Column(name = "has_paid", nullable = false)
    @Builder.Default
    private Boolean has_paid = false; //has paid will true if the paid value is equals to payment value

    //------------ RELATIONS

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spp_id")
    private SppEntity spp_book;
}
