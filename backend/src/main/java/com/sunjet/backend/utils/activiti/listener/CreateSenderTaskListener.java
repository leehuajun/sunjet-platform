package com.sunjet.backend.utils.activiti.listener;

import com.sunjet.backend.mq.Sender;
import com.sunjet.backend.utils.common.QueueNameHelper;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 推送消息
 */
@Component("createSenderTaskListener")
public class CreateSenderTaskListener implements ExecutionListener {

    @Autowired
    private Sender sender;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        // 1. 获取业务ID
        String businesskey = delegateExecution.getProcessBusinessKey();
        if (StringUtils.isNotBlank(businesskey)) {
            String[] business = businesskey.split("\\.");
            String routingKey = QueueNameHelper.mapQueue.get(business[0]);
            System.out.println(routingKey);
            if (StringUtils.isNotBlank(business[0])) {
                sender.send(business[1], "topicExchange", routingKey);
                System.out.println("消息发送成功");
            }
        }


    }


}
