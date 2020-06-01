package com.sunjet.backend.utils.activiti.listener.recycle;

import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeEntity;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/21.
 */
@Slf4j
@Component("recycleNoticeCompleteListener")
public class RecycleNoticeCompleteListener implements ExecutionListener {
    @Autowired
    private RecycleNoticeRepository noticeRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey().split("\\.")[1];
        RecycleNoticeEntity noticeEntity = (RecycleNoticeEntity) noticeRepository.findOne(businessId);

        noticeEntity.setStatus(DocStatus.CLOSED.getIndex());
        noticeRepository.save(noticeEntity);
        log.info("保存实体对象状态为：" + DocStatus.CLOSED.getName());
//        System.out.println("CustomServiceTask");
    }
}
