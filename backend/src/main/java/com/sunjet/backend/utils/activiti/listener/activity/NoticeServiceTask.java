package com.sunjet.backend.utils.activiti.listener.activity;

import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityNoticeEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityNoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/21.
 */
@Slf4j
@Component("noticeServiceTask")
public class NoticeServiceTask implements JavaDelegate {
    @Autowired
    private ActivityNoticeRepository activityNoticeRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey().split("\\.")[1];
        ActivityNoticeEntity noticeEntity = activityNoticeRepository.findOne(businessId);

        noticeEntity.setStatus(DocStatus.CLOSED.getIndex());
        activityNoticeRepository.save(noticeEntity);
        log.info("保存实体对象状态为：" + DocStatus.CLOSED.getName());
//        System.out.println("CustomServiceTask");
    }
}
