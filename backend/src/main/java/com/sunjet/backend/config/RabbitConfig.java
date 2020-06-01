package com.sunjet.backend.config;

import com.sunjet.backend.utils.common.QueueNameHelper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: lhj
 * @create: 2017-11-27 16:25
 * @description: 说明
 */
@Configuration
public class RabbitConfig {
    /**
     * 推送首保结算
     *
     * @return
     */
    @Bean
    public Queue firstToSettlementQueue() {
        return new Queue(QueueNameHelper.FIRST_TO_SETTLEMENT, true, false, false);
    }

    /**
     * 首保结算延迟队列
     *
     * @return
     */
    @Bean
    public Queue firstToSettlementDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topicExchange");
        arguments.put("x-dead-letter-routing-key", "key." + QueueNameHelper.FIRST_TO_SETTLEMENT);
        Queue queue = new Queue(QueueNameHelper.FIRST_SETTLEMENT_DELAY, true, false, false, arguments);
        System.out.println("arguments :" + queue.getArguments());
        return queue;
    }

    /**
     * 推送运费结算
     *
     * @return
     */
    @Bean
    public Queue freightToSettlementQueue() {
        return new Queue(QueueNameHelper.FREIGHT_TO_SETTLEMENT, true, false, false);
    }

    /**
     * 运费结算延迟队列
     *
     * @return
     */
    @Bean
    public Queue freightToSettlementDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topicExchange");
        arguments.put("x-dead-letter-routing-key", "key." + QueueNameHelper.FREIGHT_TO_SETTLEMENT);
        Queue queue = new Queue(QueueNameHelper.FREIGHT_SETTLEMENT_DELAY, true, false, false, arguments);
        System.out.println("arguments :" + queue.getArguments());
        return queue;
    }

    /**
     * 推送活动结算
     *
     * @return
     */
    @Bean
    public Queue activityToSettlementQueue() {
        return new Queue(QueueNameHelper.ACTIVITY_TO_SETTLEMENT, true, false, false);
    }

    /**
     * 活动结算延迟队列
     *
     * @return
     */
    @Bean
    public Queue activityToSettlementDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topicExchange");
        arguments.put("x-dead-letter-routing-key", "key." + QueueNameHelper.ACTIVITY_TO_SETTLEMENT);
        Queue queue = new Queue(QueueNameHelper.ACTIVITY_SETTLEMENT_DELAY, true, false, false, arguments);
        System.out.println("arguments :" + queue.getArguments());
        return queue;
    }

    /**
     * 推送三包结算
     *
     * @return
     */
    @Bean
    public Queue warrantyToSettlementQueue() {
        return new Queue(QueueNameHelper.WARRANTY_TO_SETTLEMENT, true, false, false);
    }


    /**
     * 三包结算延迟队列
     *
     * @return
     */
    @Bean
    public Queue warrantyToSettlementDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topicExchange");
        arguments.put("x-dead-letter-routing-key", "key." + QueueNameHelper.WARRANTY_TO_SETTLEMENT);
        Queue queue = new Queue(QueueNameHelper.WARRANTY_SETTLEMENT_DELAY, true, false, false, arguments);
        System.out.println("arguments :" + queue.getArguments());
        return queue;
    }

    /**
     * 推送配件结算
     *
     * @return
     */
    @Bean
    public Queue partToSettlementQueue() {
        return new Queue(QueueNameHelper.PART_TO_SETTLEMENT, true, false, false);
    }


    /**
     * 配件结算延迟队列
     *
     * @return
     */
    @Bean
    public Queue partToSettlementDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topicExchange");
        arguments.put("x-dead-letter-routing-key", "key." + QueueNameHelper.PART_TO_SETTLEMENT);
        Queue queue = new Queue(QueueNameHelper.PART_SETTLEMENT_DELAY, true, false, false, arguments);
        System.out.println("arguments :" + queue.getArguments());
        return queue;
    }


    /**
     * 检查服务单结算状态
     *
     * @return
     */
    @Bean
    public Queue checkWarrantyBillStatusQueue() {
        return new Queue(QueueNameHelper.CHECK_WARRANTY_STATUS, true, false, false);
    }

    /**
     * 检查服务单结算状态延迟队列
     *
     * @return
     */
    @Bean
    public Queue checkWarrantyBillStatusDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topicExchange");
        arguments.put("x-dead-letter-routing-key", "key." + QueueNameHelper.CHECK_WARRANTY_STATUS);
        Queue queue = new Queue(QueueNameHelper.CHECK_WARRANTY_STATUS_DELAY, true, false, false, arguments);
        return queue;
    }

    /**
     * 检查配件结算状态
     *
     * @return
     */
    @Bean
    public Queue checkPartBillStatusQueue() {
        return new Queue(QueueNameHelper.CHECK_PART_STATUS, true, false, false);
    }

    /**
     * 检查配件结算状态延迟队列
     *
     * @return
     */
    @Bean
    public Queue checkPartBillStatusDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topicExchange");
        arguments.put("x-dead-letter-routing-key", "key." + QueueNameHelper.CHECK_PART_STATUS);
        Queue queue = new Queue(QueueNameHelper.CHECK_PART_STATUS_DELAY, true, false, false, arguments);
        System.out.println("arguments :" + queue.getArguments());
        return queue;
    }

    /**
     * 检查运费结算状态
     *
     * @return
     */
    @Bean
    public Queue checkFreightBillStatusQueue() {
        return new Queue(QueueNameHelper.CHECK_FREIGHT_STATUS, true, false, false);
    }

    /**
     * 检查运费结算状态延迟队列
     *
     * @return
     */
    @Bean
    public Queue checkFreightBillStatusDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "topicExchange");
        arguments.put("x-dead-letter-routing-key", "key." + QueueNameHelper.CHECK_FREIGHT_STATUS);
        Queue queue = new Queue(QueueNameHelper.CHECK_FREIGHT_STATUS_DELAY, true, false, false, arguments);
        System.out.println("arguments :" + queue.getArguments());
        return queue;
    }

    //声明交互器
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange", true, false);
    }

    //绑定首保结算
    @Bean
    public Binding bindingFirstToSettlement() {
        return BindingBuilder.bind(firstToSettlementQueue()).to(topicExchange()).with("key." + QueueNameHelper.FIRST_TO_SETTLEMENT);
    }

    //绑定运费结算
    @Bean
    public Binding bindingFreightToSettlement() {
        return BindingBuilder.bind(freightToSettlementQueue()).to(topicExchange()).with("key." + QueueNameHelper.FREIGHT_TO_SETTLEMENT);
    }

    //绑定活动结算
    @Bean
    public Binding bindingActivityToSettlement() {
        return BindingBuilder.bind(activityToSettlementQueue()).to(topicExchange()).with("key." + QueueNameHelper.ACTIVITY_TO_SETTLEMENT);
    }

    //绑定三包结算
    @Bean
    public Binding bindingWarrantyToSettlement() {
        return BindingBuilder.bind(warrantyToSettlementQueue()).to(topicExchange()).with("key." + QueueNameHelper.WARRANTY_TO_SETTLEMENT);
    }

    //绑定配件结算
    @Bean
    public Binding bindingPartToSettlement() {
        return BindingBuilder.bind(partToSettlementQueue()).to(topicExchange()).with("key." + QueueNameHelper.PART_TO_SETTLEMENT);
    }


    //绑定服务结算单据状态
    @Bean
    public Binding bindingCheckWarrantyBillStatusSettlement() {
        return BindingBuilder.bind(checkWarrantyBillStatusQueue()).to(topicExchange()).with("key." + QueueNameHelper.CHECK_WARRANTY_STATUS);
    }

    //绑定配件结算单据状态
    @Bean
    public Binding bindingCheckPartBillStatusSettlement() {
        return BindingBuilder.bind(checkPartBillStatusQueue()).to(topicExchange()).with("key." + QueueNameHelper.CHECK_PART_STATUS);
    }

    //绑定运费结算单据状态
    @Bean
    public Binding bindingCheckFreightBillStatusSettlement() {
        return BindingBuilder.bind(checkFreightBillStatusQueue()).to(topicExchange()).with("key." + QueueNameHelper.CHECK_FREIGHT_STATUS);
    }

    //绑定首保结算延迟队列
    @Bean
    public Binding bindingFirstToSettlementDelay() {
        return BindingBuilder.bind(firstToSettlementDelayQueue()).to(topicExchange()).with("key." + QueueNameHelper.FIRST_SETTLEMENT_DELAY);
    }

    //绑定运费结算延迟队列
    @Bean
    public Binding bindingFreightToSettlementDelay() {
        return BindingBuilder.bind(freightToSettlementDelayQueue()).to(topicExchange()).with("key." + QueueNameHelper.FREIGHT_SETTLEMENT_DELAY);
    }

    //绑定活动结算延迟队列
    @Bean
    public Binding bindingActivityToSettlementDelay() {
        return BindingBuilder.bind(activityToSettlementDelayQueue()).to(topicExchange()).with("key." + QueueNameHelper.ACTIVITY_SETTLEMENT_DELAY);
    }

    //绑定三包结算延迟队列
    @Bean
    public Binding bindingWarrantyToSettlementDelay() {
        return BindingBuilder.bind(warrantyToSettlementDelayQueue()).to(topicExchange()).with("key." + QueueNameHelper.WARRANTY_SETTLEMENT_DELAY);
    }

    //绑定配件结算延迟队列
    @Bean
    public Binding bindingPartToSettlementDelay() {
        return BindingBuilder.bind(partToSettlementDelayQueue()).to(topicExchange()).with("key." + QueueNameHelper.PART_SETTLEMENT_DELAY);
    }


    //绑定检查服务单结算状态结算延迟队列
    @Bean
    public Binding bindingCheckWarrantyStatusDelay() {
        return BindingBuilder.bind(checkWarrantyBillStatusDelayQueue()).to(topicExchange()).with("key." + QueueNameHelper.CHECK_WARRANTY_STATUS_DELAY);
    }

    //绑定检查配件结算状态结算延迟队列
    @Bean
    public Binding bindingCheckPartStatusDelay() {
        return BindingBuilder.bind(checkPartBillStatusDelayQueue()).to(topicExchange()).with("key." + QueueNameHelper.CHECK_PART_STATUS_DELAY);
    }

    //绑定检查运费结算状态结算延迟队列
    @Bean
    public Binding bindingCheckFreightStatusDelay() {
        return BindingBuilder.bind(checkFreightBillStatusDelayQueue()).to(topicExchange()).with("key." + QueueNameHelper.CHECK_FREIGHT_STATUS_DELAY);
    }


}
