package com.sunjet.backend.utils.activiti.listener.maintenance;

import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 启用生成故障件返回单按钮
 * Created by lhj on 16/11/23.
 */
@Component("warrantyEnableRecycleStatusListener")
public class WarrantyEnableRecycleStatusListener implements TaskListener {
    @Autowired
    private WarrantyMaintenanceRepository maintenanceRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        WarrantyMaintenanceEntity maintenanceEntity = maintenanceRepository.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        maintenanceEntity.setCanEditRecycle(true);
        maintenanceRepository.save(maintenanceEntity);
        System.out.println("切换三包服务单可编辑返回单状态为:" + maintenanceEntity.getCanEditRecycle());

    }
}
