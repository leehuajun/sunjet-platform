package com.sunjet.backend.utils.activiti.listener.dealer;


import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerQuitRequestEntity;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerQuitRepostitory;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务站退出申请审批完成监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("dealerQuitCompleteListener")
public class DealerQuitCompleteListener implements ExecutionListener {
    @Autowired
    private ProcessService processService;
    @Autowired
    private DealerQuitRepostitory dealerQuitRepostitory;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private UserRepository userRepository;

//    @Override
//    public void notify(DelegateTask delegateTask) {
//        System.out.println("关闭业务对象！！");
//        // 1. 获取业务ID
//        String businessId = processService.findBusinessIdByProcessInstanceId(delegateTask.getProcessInstanceId());
//        // 2. 获取业务对象
//        DealerAdmitRequestEntity admitRequest = dealerAdmitService.findOneById(businessId);
//        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
//        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
////        delegateTask.getProcessInstanceId().
//    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.out.println("服务站退出申请审批完成监听器--关闭业务对象？？");
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(execution.getProcessInstanceId());
        // 2. 获取业务对象DealerAdmitRequestEntity
        DealerQuitRequestEntity quitRequest = dealerQuitRepostitory.findOne(businessId);
        // 3. 获取业务对象的某个属性
        DealerEntity dealerEntity = dealerRepository.findOne(quitRequest.getDealer());
        // 赋值
//        BeanUtils.copyProperties(alterRequest,dealerEntity,BeanHelper.getNullPropertyNames(alterRequest));

        List<UserEntity> users = userRepository.findAllByDealerId(dealerEntity.getObjId());
        for (UserEntity user : users) {
            user.setEnabled(false);   // 禁用用户
            userRepository.save(user);
        }

        dealerEntity.setEnabled(false);
        dealerRepository.save(dealerEntity);
        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
    }
}
