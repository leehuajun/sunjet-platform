package com.sunjet.backend.utils.activiti.listener.activity;

import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityNoticeEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityNoticeRepository;
import com.sunjet.backend.system.entity.MessageEntity;
import com.sunjet.backend.system.entity.RoleEntity;
import com.sunjet.backend.system.repository.MessageRepository;
import com.sunjet.backend.system.service.RoleService;
import com.sunjet.backend.system.service.UserService;
import com.sunjet.dto.system.admin.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/11/21.
 * 活动通知
 */
@Slf4j
@Component("noticeCompleteListener")
public class NoticeCompleteListener implements ExecutionListener {
    @Autowired
    private ActivityNoticeRepository activityNoticeRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey().split("\\.")[1];
        ActivityNoticeEntity noticeEntity = activityNoticeRepository.findOne(businessId);

        noticeEntity.setStatus(DocStatus.CLOSED.getIndex());
        activityNoticeRepository.save(noticeEntity);
        log.info("保存实体对象状态为：" + DocStatus.CLOSED.getName());
        RoleEntity roleEntity = roleService.findOneByRoleId("role000");
        List<UserInfo> userInfoList = userService.findAllByRoleId(roleEntity.getObjId());
        List<MessageEntity> messageEntityList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setRefId(noticeEntity.getObjId());
            messageEntity.setTitle("活动通知单");
            messageEntity.setLogId(userInfo.getLogId());
            messageEntity.setContent(String.format("有新的活动通知单待分配，单号为：[%s],双击查看单据详细内容", noticeEntity.getDocNo()));
            messageEntity.setUrl("/views/asm/activity_notice_form.zul");
            messageEntityList.add(messageEntity);
        }
        messageRepository.save(messageEntityList);




    }
}
