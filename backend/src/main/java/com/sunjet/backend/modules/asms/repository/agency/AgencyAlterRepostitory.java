package com.sunjet.backend.modules.asms.repository.agency;


import com.sunjet.backend.modules.asms.entity.agency.AgencyAlterRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 * 合作商资质变更申请单 dao
 */
public interface AgencyAlterRepostitory extends JpaRepository<AgencyAlterRequestEntity, String>, JpaSpecificationExecutor<AgencyAlterRequestEntity> {
}
