package project.bayaraja.application.services.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.bayaraja.application.enums.Roles;
import project.bayaraja.application.services.invoices.InvoiceEntity;
import project.bayaraja.application.services.students.StudentEntity;
import project.bayaraja.application.utils.DateSerializer;
import project.bayaraja.application.utils.Timestamps;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity @Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction(value = "deleted_at=CURRENT_TIMESTAMP")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(
        name = "deleted_at",
        type = Date.class
))
@Filter(name = "deletedUserFilter", condition = "deleted_at = :isDeleted")
public class UserEntity extends Timestamps implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "profile_picture", nullable = true)
    private String profile_picture;

    @Column(name = "is_verified", nullable = true)
    @Builder.Default
    private Boolean is_verified = false;

    @JsonSerialize(using = DateSerializer.class)
    @Column(name = "verified_at")
    private Date verified_at;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(name = "invoice_id", nullable = true)
    private Integer invoice_id;

    //-----------------RELATIONS
    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "user"
    )
    private StudentEntity student;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    @JsonIgnore
    private List<InvoiceEntity> invoices;

    //-----------------SECURITY INFO

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return is_verified;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> role.name());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
