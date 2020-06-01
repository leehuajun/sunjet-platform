package com.sunjet.backend.system.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by lhj on 16/6/18.
 */
public interface BaseService {
    JpaSpecificationExecutor getJpaSpecificationExecutor();

    JpaRepository getRepository();
//    void setJpaSpecificationExecutor(JpaSpecificationExecutor specificationExecutor);


}
