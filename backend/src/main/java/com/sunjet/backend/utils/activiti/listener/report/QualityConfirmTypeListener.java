package com.sunjet.backend.utils.activiti.listener.report;

import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.modules.asms.repository.asm.QualityReportRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("qualityConfirmTypeListener")
public class QualityConfirmTypeListener implements TaskListener {
    @Autowired
    private QualityReportRepository qualityReportRepository;


    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        QualityReportEntity reportEntity = qualityReportRepository.findOne(businessId);
        ((TaskEntity) delegateTask).getProcessInstance().setVariable("type", reportEntity.getReportType());
        reportEntity.setCanEditType(false);
        qualityReportRepository.save(reportEntity);
        System.out.println("设置速报类型:" + reportEntity.getReportType());

    }
}
