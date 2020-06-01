package com.sunjet.backend.utils.common;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ消息队列名
 */
public class QueueNameHelper {


    /**
     * queuesName
     */
    public static final String FIRST_TO_SETTLEMENT = "first_to_settlement";  // 首保生成结算队列
    public static final String FIRST_SETTLEMENT_DELAY = "first_settlement_delay";  // 首保生成结算延迟队列
    public static final String ACTIVITY_TO_SETTLEMENT = "activity_to_settlement";  // 活动生成结算延迟队列
    public static final String ACTIVITY_SETTLEMENT_DELAY = "activity_settlement_delay";  // 活动生成结算延迟队列
    public static final String WARRANTY_TO_SETTLEMENT = "warranty_to_settlement";  // 三包生成结算队列
    public static final String WARRANTY_SETTLEMENT_DELAY = "warranty_settlement_delay";  // 三包生成结算延迟队列
    public static final String FREIGHT_TO_SETTLEMENT = "freight_to_settlement";  // 运费生成结算队列
    public static final String FREIGHT_SETTLEMENT_DELAY = "freight_settlement_delay";  // 运费生成结算延迟队列
    public static final String PART_TO_SETTLEMENT = "part_to_settlement";  // 配件生成结算延迟队列
    public static final String PART_SETTLEMENT_DELAY = "part_settlement_delay";  // 配件生成结算延迟队列
    public static final String CHECK_WARRANTY_STATUS = "check_warranty_status";  // 检查三包状态队列
    public static final String CHECK_WARRANTY_STATUS_DELAY = "check_warranty_status_delay";  // 延迟检查三包结算状态队列
    public static final String CHECK_PART_STATUS = "check_part_status";  // 检查配件结算状态队列
    public static final String CHECK_PART_STATUS_DELAY = "check_part_status_delay";  // 延迟检查配件结算状态队列
    public static final String CHECK_FREIGHT_STATUS = "check_freight_status";  // 检查运费结算状态队列
    public static final String CHECK_FREIGHT_STATUS_DELAY = "check_freight_status_delay";  // 延迟检查运费结算状态队列


    public static Map<String, String> mapQueue = new HashMap<>();

    static {
        mapQueue.put("ActivityMaintenanceEntity", "key." + ACTIVITY_SETTLEMENT_DELAY);     // 活动服务单
        mapQueue.put("FirstMaintenanceEntity", "key." + FIRST_SETTLEMENT_DELAY);        // 首保服务单
        mapQueue.put("WarrantyMaintenanceEntity", "key." + WARRANTY_SETTLEMENT_DELAY);     // 三包服务单
        mapQueue.put("RecycleEntity", "key." + FREIGHT_SETTLEMENT_DELAY);                 // 故障件返回单
        mapQueue.put("SupplyEntity", "key." + PART_SETTLEMENT_DELAY);                  // 供货单
        mapQueue.put("DealerSettlementEntity", "key." + CHECK_WARRANTY_STATUS_DELAY);                  // 服务结算单
        mapQueue.put("AgencySettlementEntity", "key." + CHECK_PART_STATUS_DELAY);                  // 配件结算单
        mapQueue.put("FreightSettlementEntity", "key." + CHECK_FREIGHT_STATUS_DELAY);                  // 运费结算单
    }
}
