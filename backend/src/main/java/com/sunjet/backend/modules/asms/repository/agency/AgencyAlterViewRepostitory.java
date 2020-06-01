package com.sunjet.backend.modules.asms.repository.agency;


import com.sunjet.backend.modules.asms.entity.agency.view.AgencyAlterRequestView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 * 合作商准入申请单 dao
 */
public interface AgencyAlterViewRepostitory extends JpaRepository<AgencyAlterRequestView, String>, JpaSpecificationExecutor<AgencyAlterRequestView> {

}
