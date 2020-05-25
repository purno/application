package com.application.dao.entities;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    @CreationTimestamp
    @Column(name = "CREATED_ON", updatable = false,nullable = false)
    public LocalDateTime createdAt;


    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "UPDATED_ON", updatable = false, nullable = false)
    public LocalDateTime updatedAt;

}
