package com.sunjet.backend.utils.activiti.listener.report;

import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.modules.asms.repository.asm.QualityReportRepository;
import com.sunjet.backend.modules.asms.service.asm.QualityReportService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("qualityEnableTypeStatusListener")
public class QualityEnableTypeStatusListener implements TaskListener {
    @Autowired
    private QualityReportRepository qualityReportRepository;
    @Autowired
    private QualityReportService qualityReportSrevice;

    @Override
    public void notify(DelegateTask delegateTask) {
        try {
            TaskEntity taskEntity = (TaskEntity) delegateTask;
            String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

            QualityReportEntity reportEntity = qualityReportRepository.findOne(businessId);
            reportEntity.setCanEditType(true);

            QualityReportEntity entity = qualityReportRepository.save(reportEntity);
            //QualityReportEntity entity = qualityReportSrevice.save(reportEntity);
            //delegateTask.setVariable("type", entity.getReportType());

            System.out.println("切换服务站编号可编辑状态为:" + entity.getCanEditType());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
