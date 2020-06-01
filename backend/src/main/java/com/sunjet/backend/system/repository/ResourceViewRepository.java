package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.view.ResourceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: wfb
 * @create: 2018-08-04
 * @description: 说明
 */
public interface ResourceViewRepository extends JpaRepository<ResourceView, String>, JpaSpecificationExecutor<ResourceView> {
}
