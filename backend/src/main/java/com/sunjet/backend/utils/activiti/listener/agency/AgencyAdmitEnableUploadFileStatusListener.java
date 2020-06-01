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
@Component("agencyAdmitEnableUploadFileStatusListener")
public class AgencyAdmitEnableUploadFileStatusListener implements TaskListener {
    @Autowired
    private AgencyAdmitRepostitory agencyAdmitRepostitory;

    @Override
    public void notify(DelegateTask delegateTask) {
        AgencyAdmitRequestEntity admitRequest = agencyAdmitRepostitory.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        admitRequest.setCanUpload(true);  // 切换状态
        agencyAdmitRepostitory.save(admitRequest);
        System.out.println("切换上传文件可编辑状态为:" + admitRequest.getCanUpload());
    }
}
