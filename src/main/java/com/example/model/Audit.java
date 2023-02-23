package com.example.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@Embeddable
class Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    void prePersist(){
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preMerge(){
        updatedOn = LocalDateTime.now();
    }
}
