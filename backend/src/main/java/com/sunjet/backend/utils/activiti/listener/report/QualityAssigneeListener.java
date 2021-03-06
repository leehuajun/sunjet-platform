package com.sunjet.backend.utils.activiti.listener.report;

import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.repository.asm.QualityReportRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("qualityAssigneeListener")
public class QualityAssigneeListener implements TaskListener {
    @Autowired
    private QualityReportRepository qualityReportRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        QualityReportEntity reportEntity = qualityReportRepository.findOne(businessId);
        DealerEntity dealer = dealerRepository.findOneByCode(reportEntity.getDealerCode());
        UserEntity userEntity = userRepository.findOne(dealer.getServiceManagerId());
        delegateTask.setAssignee(userEntity.getLogId());
        System.out.println("分配任务给:" + userEntity.getLogId());

    }
}
