package project.bayaraja.application.services.lookups;

import jakarta.persistence.*;
import lombok.*;
import project.bayaraja.application.utils.Timestamps;

@Builder @Data @EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "lookup")
public class LookupEntity extends Timestamps {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //It used when you want to get data key or value by using the type
    @Column(name = "type", nullable = false)
    private String type;

    //It used for storing key for accessing value
    @Column(name = "key", nullable = false)
    private String key;

    //It used for value of the key
    @Column(name = "value", nullable = false)
    private String value;

    //It used for describe the value or how to use this lookup
    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;
}
