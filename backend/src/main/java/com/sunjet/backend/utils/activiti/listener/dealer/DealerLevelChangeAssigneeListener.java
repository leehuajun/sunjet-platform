package com.sunjet.backend.utils.activiti.listener.dealer;

import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerLevelChangeRepostitory;
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
@Component("dealerLevelChangeAssigneeListener")
public class DealerLevelChangeAssigneeListener implements TaskListener {
    @Autowired
    private DealerLevelChangeRepostitory dealerLevelChangeRepostitory;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        DealerLevelChangeRequestEntity dealerLevelChangeRequestEntity = dealerLevelChangeRepostitory.findOne(businessId);
        DealerEntity dealerEntity = dealerRepository.findOne(dealerLevelChangeRequestEntity.getDealer());
        UserEntity serviceManager = userRepository.findOne(dealerEntity.getServiceManagerId());
        delegateTask.setAssignee(serviceManager.getLogId());
        System.out.println("分配任务给:" + serviceManager.getLogId());
    }
}
