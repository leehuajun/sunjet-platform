package com.sunjet.backend.utils.activiti.listener.report;

import com.sunjet.backend.modules.asms.entity.asm.ExpenseReportEntity;
import com.sunjet.backend.modules.asms.repository.asm.ExpenseReportRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("expenseEnableTypeStatusListener")
public class ExpenseEnableTypeStatusListener implements TaskListener {
    @Autowired
    private ExpenseReportRepository expenseReportRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        ExpenseReportEntity reportEntity = expenseReportRepository.findOne(businessId);
        reportEntity.setCanEditType(true);
        ExpenseReportEntity save = expenseReportRepository.save(reportEntity);
        //delegateTask.setVariable("type", reportEntity.getCostType());
        System.out.println("切换服务站编号可编辑状态为:" + reportEntity.getCanEditType());
    }
}
