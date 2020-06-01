package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.view.FirstMaintenanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 首保 dao
 * Created by wfb on 2017/8/11.
 */
public interface FirstMaintenanceViewRepository extends JpaRepository<FirstMaintenanceView, String>, JpaSpecificationExecutor<FirstMaintenanceView> {

}

