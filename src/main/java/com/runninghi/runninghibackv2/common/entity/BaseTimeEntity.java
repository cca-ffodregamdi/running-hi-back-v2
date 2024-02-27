package com.runninghi.runninghibackv2.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(name = "create_date", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime updateDate;

    @Column(name = "delete_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime deleteDate;


    @PrePersist
    public void prePersist() {
        createDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }

    @PreRemove
    public void preRemove() {
        deleteDate = LocalDateTime.now();
    }
}