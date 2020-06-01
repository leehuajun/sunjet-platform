package com.sunjet.backend.utils.activiti.listener.dealer;

import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerAdmitRequestEntity;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerAdmitRepostitory;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("dealerAdmitAssigneeListener")
public class DealerAdmitAssigneeListener implements TaskListener {
    @Autowired
    private DealerAdmitRepostitory dealerAdmitRepostitory;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private UserRepository userRepository;
    //@Autowired
    //private CacheManager customCacheManager;

    @Override
    public void notify(DelegateTask delegateTask) {
        DealerAdmitRequestEntity dealerAdmitRequest = dealerAdmitRepostitory.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        DealerEntity dealerEntity = dealerRepository.findOne(dealerAdmitRequest.getDealer());
        UserEntity serviceManager = userRepository.findOne(dealerEntity.getServiceManagerId());

        delegateTask.setAssignee(serviceManager.getLogId());

        // 重新更新dealer列表
//        customCacheManager.initDealerList();
        System.out.println("分配任务给:" + serviceManager.getLogId());

    }
}
