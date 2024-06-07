package project.bayaraja.application.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@MappedSuperclass
@Data
@AllArgsConstructor
public class Timestamps {

    @JsonSerialize(using = DateSerializer.class)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @JsonIgnore
    private Date createdAt;

    @JsonSerialize(using = DateSerializer.class)
    @UpdateTimestamp
    @Column(nullable = false)
    @JsonIgnore
    private Date updatedAt;

    @JsonSerialize(using = DateSerializer.class)
    @JsonIgnore
    private Date deletedAt;

    public Timestamps() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    private void onUpdate(){
        this.updatedAt = new Date();
    }

}
