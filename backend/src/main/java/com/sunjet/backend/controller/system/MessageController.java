package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.entity.MessageEntity;
import com.sunjet.backend.system.service.MessageService;
import com.sunjet.dto.system.admin.MessageInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/9/12.
 * 消息通知
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    /**
     * 获取所有消息
     *
     * @return
     */
    @ApiOperation(value = "查找所有未读的消息")
    @PostMapping("/findAllUnRead")
    public List<MessageInfo> findAllUnRead(@RequestBody String orgId) {
        return messageService.findAllUnRead(orgId);
    }


    /**
     * 获取所有消息
     *
     * @return
     */
    @ApiOperation(value = "通过用户lodId查找所有未读的消息")
    @PostMapping("/findAllUnReadByLogId")
    public List<MessageEntity> findAllUnReadByLogId(@RequestBody String logId) {
        return messageService.findAllUnReadByLogId(logId);
    }

    /**
     * 获取分页
     *
     * @param pageParam
     * @return
     */
    @ApiOperation(value = "获取消息通知分页")
    @PostMapping("/getPageList")
    public PageResult<MessageEntity> getPageList(@RequestBody PageParam<MessageInfo> pageParam) {
        return messageService.getPageList(pageParam);
    }

    @ApiOperation(value = "查找所有消息")
    @PostMapping("/findAll")
    public List<MessageInfo> findAll() {
        return messageService.findAll();
    }

    @ApiOperation(value = "保存一个消息实体")
    @PostMapping("/save")
    public MessageInfo save(@RequestBody MessageInfo messageInfo) {
        return messageService.save(messageInfo);
    }

    @ApiOperation(value = "根据引用的对象id查一个消息实体")
    @PostMapping("/findOne")
    public MessageEntity findOne(@RequestBody String objId) {
        return messageService.findOne(objId);
    }
}
