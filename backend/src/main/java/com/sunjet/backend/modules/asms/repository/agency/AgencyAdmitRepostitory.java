package com.sunjet.backend.modules.asms.repository.agency;


import com.sunjet.backend.modules.asms.entity.agency.AgencyAdmitRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2016/9/5.
 * 合作商准入申请单 dao
 */
public interface AgencyAdmitRepostitory extends JpaRepository<AgencyAdmitRequestEntity, String>, JpaSpecificationExecutor<AgencyAdmitRequestEntity> {

    @Query("select aa from AgencyAdmitRequestEntity aa where aa.processInstanceId=?1")
    AgencyAdmitRequestEntity findOneByProcessInstanceId(String processInstanceId);
    //List<AgencyAdmitRequestEntity> findAll();
}
