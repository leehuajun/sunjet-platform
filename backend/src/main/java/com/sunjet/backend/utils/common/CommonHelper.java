package com.sunjet.backend.utils.common;


import java.util.HashMap;
import java.util.Map;

//import org.apache.shiro.crypto.hash.Md5Hash;


/**
 * @author lhj
 * @create 2015-12-21 上午9:45
 */
public class CommonHelper {


    public static Map<String, Object> mapDocumentNo = new HashMap<>();

    static {
        mapDocumentNo.put("AgencyAdmitRequestEntity", "HZSA");    // 合作商准入申请
        mapDocumentNo.put("AgencyAlterRequestEntity", "HZSB");    // 合作商变更申请
        mapDocumentNo.put("AgencyQuitRequestEntity", "HZSQ");     // 合作商退出申请
        mapDocumentNo.put("DealerAdmitRequestEntity", "FWZA");    // 服务站准入申请
        mapDocumentNo.put("DealerAlterRequestEntity", "FWZB");    // 服务站变更申请
        mapDocumentNo.put("DealerLevelChangeRequestEntity", "FWZD"); // 服务站等级变更申请
        mapDocumentNo.put("DealerQuitRequestEntity", "FWZT");     // 服务站退出申请
        mapDocumentNo.put("ActivityDistributionEntity", "ADFB");  // 活动发布单
        mapDocumentNo.put("ActivityMaintenanceEntity", "AMFW");   // 活动服务单
        mapDocumentNo.put("ActivityNoticeEntity", "HDTZ");        // 活动通知单
        mapDocumentNo.put("AgencySettlementEntity", "AEJS");      // 合作商费用结算单
        mapDocumentNo.put("DealerSettlementEntity", "DEJS");      // 服务站费用结算单
        mapDocumentNo.put("FreightSettlementEntity", "FEJS");      // 运费结算单
        mapDocumentNo.put("ExpenseReportEntity", "FYSB");         // 费用速报
        mapDocumentNo.put("FirstMaintenanceEntity", "FMFW");      // 首保服务单
        mapDocumentNo.put("QualityReportEntity", "ZLSB");         // 质量速报
        mapDocumentNo.put("RecycleEntity", "GZFH");               // 故障件返回单
        mapDocumentNo.put("RecycleNoticeEntity", "FHTZ");         // 故障件返回通知单
        mapDocumentNo.put("ServiceProxyEntity", "FWWT");          // 服务委托单
        mapDocumentNo.put("SupplyEntity", "PJGH");                // 供货单
        mapDocumentNo.put("SupplyNoticeEntity", "GHTZ");          // 供货通知单
        mapDocumentNo.put("WarrantyMaintenanceEntity", "WMFW");   // 三包服务单
        mapDocumentNo.put("LeaveBill", "LEAV");   // 请假申请
    }

    /**
     * 流程审核完成，是否自动关闭？ true:自动关闭  false:不自动关闭（由事件出发关闭）
     */
    public static Map<String, Boolean> mapAutoClose = new HashMap<>();

    static {
        mapAutoClose.put("AgencyAdmitRequestEntity", true);          // 合作商准入申请
        mapAutoClose.put("AgencyAlterRequestEntity", true);          // 合作商变更申请
        mapAutoClose.put("AgencyQuitRequestEntity", true);       // 合作商退出申请
        mapAutoClose.put("DealerAdmitRequestEntity", true);      // 服务站准入申请
        mapAutoClose.put("DealerAlterRequestEntity", true);      // 服务站变更申请
        mapAutoClose.put("DealerLevelChangeRequestEntity", true); // 服务站等级变更申请
        mapAutoClose.put("DealerQuitRequestEntity", true);       // 服务站退出申请
        mapAutoClose.put("ActivityDistributionEntity", true);    // 活动发布单
        mapAutoClose.put("ActivityMaintenanceEntity", true);     // 活动服务单
        mapAutoClose.put("ActivityNoticeEntity", true);              // 活动通知单
        mapAutoClose.put("AgencySettlementEntity", true);            // 合作商费用结算单
        mapAutoClose.put("DealerSettlementEntity", true);        // 服务站费用结算单
        mapAutoClose.put("FreightSettlementEntity", true);        // 运费结算单
        mapAutoClose.put("ExpenseReportEntity", true);           // 费用速报
        mapAutoClose.put("FirstMaintenanceEntity", true);        // 首保服务单
        mapAutoClose.put("QualityReportEntity", true);           // 质量速报
        mapAutoClose.put("RecycleEntity", true);                 // 故障件返回单
        mapAutoClose.put("RecycleNoticeEntity", true);           // 故障件返回通知单
        mapAutoClose.put("ServiceProxyEntity", true);            // 服务委托单
        mapAutoClose.put("SupplyEntity", true);                  // 供货单
        mapAutoClose.put("SupplyNoticeEntity", true);             // 供货通知单
        mapAutoClose.put("WarrantyMaintenanceEntity", false);     // 三包服务单
        mapAutoClose.put("LeaveBill", true);   // 请假申请
    }


    /**
     * 生成固定长度的流水号，不足长度，前面补0
     *
     * @param sn          数字
     * @param fixedLength 长度
     * @return
     */
    public static String genFixedStringBySn(Integer sn, Integer fixedLength) {
        // 0 代表前面补充0
        // 10代表长度为10
        // d 代表参数为正数型
        String format = "%0" + fixedLength + "d";
        String fixedString = String.format(format, sn);
//        logger.info(fixedString);
        return fixedString;
    }
}
