package com.sunjet.backend.utils.activiti.listener.report;


import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.modules.asms.repository.asm.QualityReportRepository;
import com.sunjet.backend.system.service.ProcessService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务站准入申请审批完成监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("qualityCompleteListener")
public class QualityCompleteListener implements ExecutionListener {
    @Autowired
    private ProcessService processService;
    @Autowired
    private QualityReportRepository qualityReportRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(execution.getProcessInstanceId());
        // 2. 获取业务对象DealerAdmitRequestEntity
        QualityReportEntity reportEntity = qualityReportRepository.findOne(businessId);
        // 3. 获取业务对象的某个属性
        reportEntity.setEnabled(true);
        qualityReportRepository.save(reportEntity);

        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);

        System.out.println("设置速报Enabled状态为:" + reportEntity.getEnabled());
    }
}
