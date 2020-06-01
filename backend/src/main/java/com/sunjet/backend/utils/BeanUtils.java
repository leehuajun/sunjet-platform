package com.sunjet.backend.utils;

import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityNoticeEntity;
import com.sunjet.backend.modules.asms.entity.agency.AgencyAdmitRequestEntity;
import com.sunjet.backend.modules.asms.entity.agency.AgencyAlterRequestEntity;
import com.sunjet.backend.modules.asms.entity.agency.AgencyQuitRequestEntity;
import com.sunjet.backend.modules.asms.entity.asm.ExpenseReportEntity;
import com.sunjet.backend.modules.asms.entity.asm.FirstMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerAdmitRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerAlterRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerQuitRequestEntity;
import com.sunjet.backend.modules.asms.entity.flow.LeaveBill;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeEntity;
import com.sunjet.backend.modules.asms.entity.settlement.AgencySettlementEntity;
import com.sunjet.backend.modules.asms.entity.settlement.DealerSettlementEntity;
import com.sunjet.backend.modules.asms.entity.settlement.FreightSettlementEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeEntity;
import com.sunjet.backend.quartz.*;
import com.sunjet.backend.utils.common.CommonHelper;
import org.apache.commons.lang.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @desc: 反射工具
 * @version: v1.0
 */
public class BeanUtils {

    /**
     * 相同属性值拷贝，不相同的属性保留原值
     *
     * @param <S>
     * @param <D>
     * @param s   源对象
     * @param d   目标对象
     * @return D 目标对象
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <S, D> D copyPropertys(S s, D d) {
        if (s == null || d == null) {
            return d;
        }

        //向上转型
        for (Class<?> scls = s.getClass(), dcls = d.getClass();
             scls != Object.class && dcls != Object.class;
             scls = scls.getSuperclass(), dcls = dcls.getSuperclass()) {
            try {
                Field[] sfields = scls.getDeclaredFields();
                Field[] dfields = dcls.getDeclaredFields();
//                Method[] methods = scls.getDeclaredMethods();
//                Method[] methods2 = dcls.getDeclaredMethods();


                for (Field sfield : sfields) {
                    String sName = sfield.getName();
                    Class sType = sfield.getType();
                    String sfieldName = sName.substring(0, 1).toUpperCase() + sName.substring(1);
                    Method sGetMethod = null;
                    try {
                        sGetMethod = scls.getMethod("get" + sfieldName);
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                    Object value = sGetMethod.invoke(s);
                    for (Field dfield : dfields) {
                        String dName = dfield.getName();
                        Class dType = dfield.getType();
                        if (dName.equals(sName) && sType.toString().equals(dType.toString())) {
                            Method dSetMethod = dcls.getMethod("set" + sfieldName, sType);
                            dSetMethod.invoke(d, value);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了

            }
        }

//        try {
//            Method dSetMethod222 = d.getClass().getMethod("setObjId");
//            dSetMethod222.invoke(d, "TEST");
//            System.out.println(d);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

        return d;
    }


    /**
     * map转bean
     *
     * @param map
     * @param beanClass class路径
     * @return object 实体
     * @throws Exception
     */
    public static Object mapToObject(LinkedHashMap<String, Object> map, String beanClass) throws Exception {
        Object Object = null;
        if (map == null)
            return null;
        Class onwClass = Class.forName(beanClass);
        Object = onwClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(Object.getClass());

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Class<?> propertyType = property.getPropertyType();
            String type = propertyType.getTypeName();
            Method setter = property.getWriteMethod();
            if (setter != null) {
                //处理时间类型
                if ("java.util.Date".equals(type)) {
                    //object 转为long类型
                    if (map.get(property.getName()) != null) {
                        Long aLong = Long.valueOf(map.get(property.getName()).toString());
                        Date date = new Date(aLong);
                        setter.invoke(Object, date);
                    }
                } else {
                    setter.invoke(Object, map.get(property.getName()));
                }
            }
        }
        return Object;
    }

    /**
     * 通过单据编号获取单据类型
     *
     * @param docNo
     * @return
     */
    public static String getDocTypeByDocNo(String docNo) {
        Map<String, String> getDocTypeByDocNo = new HashMap<>();
        getDocTypeByDocNo.put("HZSA", AgencyAdmitRequestEntity.class.getName());          // 合作商准入申请
        getDocTypeByDocNo.put("HZSB", AgencyAlterRequestEntity.class.getName());          // 合作商变更申请
        getDocTypeByDocNo.put("HZSQ", AgencyQuitRequestEntity.class.getName());       // 合作商退出申请
        getDocTypeByDocNo.put("FWZA", DealerAdmitRequestEntity.class.getName());      // 服务站准入申请
        getDocTypeByDocNo.put("FWZB", DealerAlterRequestEntity.class.getName());      // 服务站变更申请
        getDocTypeByDocNo.put("FWZD", DealerLevelChangeRequestEntity.class.getName()); // 服务站等级变更申请
        getDocTypeByDocNo.put("FWZT", DealerQuitRequestEntity.class.getName());       // 服务站退出申请
        getDocTypeByDocNo.put("ADFB", ActivityDistributionEntity.class.getName());    // 活动发布单
        getDocTypeByDocNo.put("AMFW", ActivityMaintenanceEntity.class.getName());     // 活动服务单
        getDocTypeByDocNo.put("HDTZ", ActivityNoticeEntity.class.getName());              // 活动通知单
        getDocTypeByDocNo.put("AEJS", AgencySettlementEntity.class.getName());            // 合作商费用结算单
        getDocTypeByDocNo.put("DEJS", DealerSettlementEntity.class.getName());        // 服务站费用结算单
        getDocTypeByDocNo.put("FEJS", FreightSettlementEntity.class.getName());        // 运费结算单
        getDocTypeByDocNo.put("FYSB", ExpenseReportEntity.class.getName());           // 费用速报
        getDocTypeByDocNo.put("FMFW", FirstMaintenanceEntity.class.getName());        // 首保服务单
        getDocTypeByDocNo.put("ZLSB", QualityReportEntity.class.getName());           // 质量速报
        getDocTypeByDocNo.put("GZFH", RecycleEntity.class.getName());                 // 故障件返回单
        getDocTypeByDocNo.put("FHTZ", RecycleNoticeEntity.class.getName());           // 故障件返回通知单
        getDocTypeByDocNo.put("PJGH", SupplyEntity.class.getName());                  // 供货单
        getDocTypeByDocNo.put("GHTZ", SupplyNoticeEntity.class.getName());             // 供货通知单
        getDocTypeByDocNo.put("WMFW", WarrantyMaintenanceEntity.class.getName());     // 三包服务单
        getDocTypeByDocNo.put("LEAV", LeaveBill.class.getName());   // 请假申请
        docNo = StringUtils.substring(docNo, 0, 4);
        return getDocTypeByDocNo.get(docNo);
    }

    ;


    /**
     * 通过单据编号 获取
     */
    public static String getServiceByDocNo(String docNo) {
        Map<String, String> getServiceByDocNo = new HashMap<>();
        getServiceByDocNo.put("HZSA", "agencyAdmitService");          // 合作商准入申请
        getServiceByDocNo.put("HZSB", "agencyAlterService");          // 合作商变更申请
        getServiceByDocNo.put("HZSQ", "agencyQuitService");       // 合作商退出申请
        getServiceByDocNo.put("FWZA", "dealerAdmitService");      // 服务站准入申请
        getServiceByDocNo.put("FWZB", "dealerAlterService");      // 服务站变更申请
        getServiceByDocNo.put("FWZD", "dealerLevelChangeService"); // 服务站等级变更申请
        getServiceByDocNo.put("FWZT", "dealerQuitService");       // 服务站退出申请
        getServiceByDocNo.put("ADFB", "activityDistributionService");    // 活动发布单
        getServiceByDocNo.put("AMFW", "activityMaintenanceService");     // 活动服务单
        getServiceByDocNo.put("HDTZ", "activityNoticeService");              // 活动通知单
        getServiceByDocNo.put("AEJS", "agencySettlementService");            // 合作商费用结算单
        getServiceByDocNo.put("DEJS", "dealerSettlementService");        // 服务站费用结算单
        getServiceByDocNo.put("FEJS", "freightSettlementService");        // 运费结算单
        getServiceByDocNo.put("FYSB", "expenseReportService");           // 费用速报
        getServiceByDocNo.put("FMFW", "firstMaintenanceService");        // 首保服务单
        getServiceByDocNo.put("ZLSB", "qualityReportService");           // 质量速报
        getServiceByDocNo.put("GZFH", "recycleService");                 // 故障件返回单
        getServiceByDocNo.put("FHTZ", "recycleNoticeItemService");           // 故障件返回通知单
        getServiceByDocNo.put("PJGH", "supplyService");                  // 供货单
        getServiceByDocNo.put("GHTZ", "supplyNoticeService");             // 供货通知单
        getServiceByDocNo.put("WMFW", "warrantyMaintenanceService");     // 三包服务单

        docNo = StringUtils.substring(docNo, 0, 4);
        return getServiceByDocNo.get(docNo);
    }


    public static String getClassTypeByClassName(String className) {
        Map<String, String> getClassTypeByClassName = new HashMap<>();
        getClassTypeByClassName.put("ActivityMaintenanceSettlementHandleJob", ActivityMaintenanceSettlementHandleJob.class.getName());
        getClassTypeByClassName.put("CheckBillStatusJob", CheckBillStatusJob.class.getName());
        getClassTypeByClassName.put("FirstMaintenanceSettlementHandleJob", FirstMaintenanceSettlementHandleJob.class.getName());
        getClassTypeByClassName.put("FreightSettlementHandleJob", FreightSettlementHandleJob.class.getName());
        getClassTypeByClassName.put("PartSettlementHandleJob", PartSettlementHandleJob.class.getName());
        getClassTypeByClassName.put("WarrantyCheckStatusJob", WarrantyCheckStatusJob.class.getName());
        getClassTypeByClassName.put("WarrantyMaintenanceSettlementHandleJob", WarrantyMaintenanceSettlementHandleJob.class.getName());
        getClassTypeByClassName.put("CheckActivityVehicleDistributeAndRepairStatusHandleJob", CheckActivityVehicleDistributeAndRepairStatusHandleJob.class.getName());
        getClassTypeByClassName.put("TaskMailJob", TaskMailJob.class.getName());
        return getClassTypeByClassName.get(className);
    }


    public static String getServiceByProcessDefinitionId(String ProcessDefinitionId) {
        if (StringUtils.isNotBlank(ProcessDefinitionId)) {
            String[] items = ProcessDefinitionId.split(":");
            if (items.length > 0) {
                String item = items[0];
                String docNo = CommonHelper.mapDocumentNo.get(item).toString();
                return getServiceByDocNo(docNo);
            }
        }

        return "";
    }


}