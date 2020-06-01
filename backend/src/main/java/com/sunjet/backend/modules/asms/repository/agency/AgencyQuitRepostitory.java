package com.sunjet.backend.modules.asms.repository.agency;


import com.sunjet.backend.modules.asms.entity.agency.AgencyQuitRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 * 合作商退出申请单 dao
 */
public interface AgencyQuitRepostitory extends JpaRepository<AgencyQuitRequestEntity, String>, JpaSpecificationExecutor<AgencyQuitRequestEntity> {
}
