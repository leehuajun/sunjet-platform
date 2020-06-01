package com.sunjet.backend.system.service;

import com.sunjet.backend.system.entity.MessageEntity;
import com.sunjet.dto.system.admin.MessageInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by lhj on 16/6/18.
 */
public interface MessageService {

    List<MessageInfo> findAllUnRead(String orgId);

    PageResult<MessageEntity> getPageList(PageParam<MessageInfo> pageParam);

    List<MessageInfo> findAll();

    MessageInfo save(MessageInfo messageInfo);

    MessageEntity findOne(String objId);

    List<MessageEntity> findAllUnReadByLogId(String logId);
}
