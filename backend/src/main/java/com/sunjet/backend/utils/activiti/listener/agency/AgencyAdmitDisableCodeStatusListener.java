package com.sunjet.backend.utils.activiti.listener.agency;

import com.sunjet.backend.modules.asms.entity.agency.AgencyAdmitRequestEntity;
import com.sunjet.backend.modules.asms.repository.agency.AgencyAdmitRepostitory;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("agencyAdmitDisableCodeStatusListener")
public class AgencyAdmitDisableCodeStatusListener implements TaskListener {
    @Autowired
    private AgencyAdmitRepostitory agencyAdmitRepostitory;

    //@Autowired
    //private CacheManager customCacheManager;

    @Override
    public void notify(DelegateTask delegateTask) {
        AgencyAdmitRequestEntity admitRequest = agencyAdmitRepostitory.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        admitRequest.setCanEditCode(false);  // 切换状态
        agencyAdmitRepostitory.save(admitRequest);
//        customCacheManager.initAgencyList();
        System.out.println("切换服务站编号可编辑状态为:" + admitRequest.getCanEditCode());

    }
}
