package com.sunjet.backend.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.system.entity.MessageEntity;
import com.sunjet.backend.system.repository.MessageRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.system.admin.MessageInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<MessageInfo> findAllUnRead(String orgId) {
        List<MessageEntity> messageEntities = messageRepository.findAllUnRead(orgId);
        List<MessageInfo> messageInfos = new ArrayList<>();
        for (MessageEntity entity : messageEntities) {
            messageInfos.add(BeanUtils.copyPropertys(entity, new MessageInfo()));
        }
        return messageInfos;
    }

    @Override
    public PageResult<MessageEntity> getPageList(PageParam<MessageInfo> pageParam) {
        //1.查询条件
        MessageInfo messageInfo = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<MessageEntity> specification = null;
        if (messageInfo != null) {
            specification = Specifications.<MessageEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(messageInfo.getTitle()), "title", messageInfo.getTitle())
                    .eq(StringUtils.isNotBlank(messageInfo.getOrgId()), "orgId", messageInfo.getOrgId())
                    .eq(StringUtils.isNotBlank(messageInfo.getLogId()), "logId", messageInfo.getLogId())
                    //.eq(!(messageInfo.getIsRead() == null), "isRead", messageInfo.getIsRead())
                    .build();
        }

        //3.执行查询
        Page<MessageEntity> pages = messageRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        //List<MessageEntity> entities = pages.getContent();
        //List<MessageInfo> rows = new ArrayList<>();
        //for (MessageEntity entity : entities) {
        //    rows.add(BeanUtils.copyPropertys(entity, new MessageInfo()));
        //}


        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 查找所有消息
     *
     * @return
     */
    @Override
    public List<MessageInfo> findAll() {
        List<MessageEntity> messageEntityList = messageRepository.findAll();
        List<MessageInfo> messageInfos = new ArrayList<>();
        for (MessageEntity messageEntity : messageEntityList) {
            messageInfos.add(BeanUtils.copyPropertys(messageEntity, new MessageInfo()));
        }
        return messageInfos;
    }

    /**
     * 保存一个消息实体
     *
     * @param messageInfo
     * @return
     */
    @Override
    public MessageInfo save(MessageInfo messageInfo) {
        try {
            MessageEntity messageEntity = messageRepository.save(BeanUtils.copyPropertys(messageInfo, new MessageEntity()));
            messageInfo = BeanUtils.copyPropertys(messageEntity, new MessageInfo());
            return messageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据消息id查一个消息实体
     *
     * @param objId
     * @return
     */
    @Override
    public MessageEntity findOne(String objId) {
        try {
            MessageEntity messageEntity = messageRepository.findOne(objId);
            //MessageInfo messageInfo = BeanUtils.copyPropertys(messageEntity, new MessageInfo());
            return messageEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过用户logId查未读信息
     *
     * @param logId
     * @return
     */
    @Override
    public List<MessageEntity> findAllUnReadByLogId(String logId) {
        List<MessageEntity> messageEntities = messageRepository.findAllUnReadByLogId(logId);
        return messageEntities;
    }
}