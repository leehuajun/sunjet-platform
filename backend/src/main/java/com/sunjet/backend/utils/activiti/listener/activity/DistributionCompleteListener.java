package com.sunjet.backend.utils.activiti.listener.activity;

import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityDistributionRepository;
import com.sunjet.backend.system.entity.MessageEntity;
import com.sunjet.backend.system.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/21.
 */
@Slf4j
@Component("distributionCompleteListener")
public class DistributionCompleteListener implements ExecutionListener {
    @Autowired
    private ActivityDistributionRepository activityDistributionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey().split("\\.")[1];
        ActivityDistributionEntity distributionEntity = activityDistributionRepository.findOne(businessId);

        distributionEntity.setStatus(DocStatus.CLOSED.getIndex());
        activityDistributionRepository.save(distributionEntity);

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setOrgId(distributionEntity.getDealer());
        messageEntity.setRefId(distributionEntity.getObjId());
        messageEntity.setTitle("活动分配单");
        messageEntity.setContent(String.format("贵司有新的活动服务分配单，单号为：[%s],双击查看单据详细内容", distributionEntity.getDocNo()));
        messageEntity.setUrl("/views/asm/activity_distribution_form.zul");

        messageRepository.save(messageEntity);


        log.info("保存实体对象状态为：" + DocStatus.CLOSED.getName());
//        System.out.println("CustomServiceTask");
    }
}
