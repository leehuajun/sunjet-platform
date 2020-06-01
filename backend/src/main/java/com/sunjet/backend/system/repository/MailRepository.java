package com.sunjet.backend.system.repository;

import com.sunjet.backend.system.entity.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MailRepository extends JpaRepository<MailEntity, String>, JpaSpecificationExecutor<MailEntity> {
}
