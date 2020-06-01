package com.sunjet.frontend.service.system;

import com.sunjet.dto.system.admin.MessageInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: lhj
 * @create: 2017-10-18 10:32
 * @description: 说明
 */
@Slf4j
@Service("messageService")
public class MessageService {
    @Autowired
    private RestClient restClient;

    /**
     * 查找所有消息
     *
     * @return
     */

    public List<MessageInfo> findAll() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<MessageInfo> list = restClient.findAll("/message/findAll", requestEntity, new ParameterizedTypeReference<List<MessageInfo>>() {
            });
            log.info("MessageServiceImpl:findAll:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MessageServiceImpl:findAll:error:" + e.getMessage());
            return null;
        }
    }


    public List<MessageInfo> findAllUnRead(String orgId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(orgId, null);
            return restClient.findAll("/message/findAllUnRead", requestEntity, new ParameterizedTypeReference<List<MessageInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MessageServiceImpl:findAllUnRead:error:" + e.getMessage());
            return null;
        }
    }


    public PageResult<MessageInfo> getPageList(PageParam<MessageInfo> pageParam) {
        try {
            return restClient.getPageList("/message/getPageList", pageParam, new ParameterizedTypeReference<PageResult<MessageInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MessageServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存一个消息
     *
     * @param messageInfo
     * @return
     */
    public MessageInfo save(MessageInfo messageInfo) {
        try {
            HttpEntity<MessageInfo> requestEntity = new HttpEntity<>(messageInfo, null);
            ResponseEntity<MessageInfo> ResponseEntity = restClient.post("/message/save", requestEntity, MessageInfo.class);
            log.info("MessageServiceImpl:save:success");
            return ResponseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MessageServiceImpl:save:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据引用的对象id查一个消息实体
     *
     * @param objId
     * @return
     */
    public MessageInfo findOne(String objId) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<MessageInfo> ResponseEntity = restClient.get("/message/findOne", requestEntity, MessageInfo.class);
            MessageInfo messageInfo = ResponseEntity.getBody();
            log.info("MessageServiceImpl:findOne:success");
            return messageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MessageServiceImpl:findOne:error" + e.getMessage());
            return null;
        }
    }

    public List<MessageInfo> findAllUnReadByLogId(String logId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(logId, null);
            return restClient.findAll("/message/findAllUnReadByLogId", requestEntity, new ParameterizedTypeReference<List<MessageInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
