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
@Component("expenseConfirmTypeListener")
public class ExpenseConfirmTypeListener implements TaskListener {
    @Autowired
    private ExpenseReportRepository expenseReportRepository;


    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        ExpenseReportEntity reportEntity = expenseReportRepository.findOne(businessId);
        ((TaskEntity) delegateTask).getProcessInstance().setVariable("type", reportEntity.getCostType());
        ((TaskEntity) delegateTask).getProcessInstance().setVariable("amount", reportEntity.getEstimatedCost());
        reportEntity.setCanEditType(false);
        expenseReportRepository.save(reportEntity);
        System.out.println("设置速报类型:" + reportEntity.getCostType());

    }
}
