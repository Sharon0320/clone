package com.gachon.ReAction_bank_server.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity{
   @CreatedDate
   protected LocalDateTime createdDate;

   protected void setCreatedDateForTest(LocalDateTime createdDate){
      this.createdDate = createdDate;
   }
}
