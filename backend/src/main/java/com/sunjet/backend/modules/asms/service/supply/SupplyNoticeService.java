package com.sunjet.backend.modules.asms.service.supply;


import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeEntity;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyNoticeView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 供货/发货  通知单
 * Created by lhj on 16/9/17.
 */
public interface SupplyNoticeService extends BaseService {

    SupplyNoticeInfo save(SupplyNoticeInfo supplyNoticeInfo);

    boolean delete(SupplyNoticeInfo supplyNoticeInfo);

    boolean delete(String objId);

    SupplyNoticeInfo findOne(String objId);

    /**
     * 分页
     *
     * @return
     */
    public PageResult<SupplyNoticeView> getPageList(PageParam<SupplyNoticeItem> pageParam);


    /**
     * 通过调拨通知单id查一个调拨子行
     *
     * @param supplyNoticeId
     * @return
     */
    List<SupplyNoticeItemInfo> findByNoticeId(String supplyNoticeId);


    boolean deleteBySupplyNotices(String supplyNoticeId);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean checkSupplyReceiveState(String objId);

    List<SupplyItemInfo> findSupplyItemIdsBySrcDocNo(String srcDocNo);

    List<SupplyItemInfo> findSupplyItemIdsByVin(String vin);

    List<String> findAllObjIdByActivityNoticeDocNo(String activityNoticeDocNo);

    List<String> findSupplyNoticeIdsByVin(String vin);

    SupplyNoticeInfo findOneByDocNo(String docNo);

    Boolean desertTask(String objId);

    SupplyNoticeEntity findOneBySrcDocId(String objId);


    //SupplyNoticeEntity getSupplyNoticeByID(String id);
    //
    //SupplyNoticeEntity save(SupplyNoticeEntity supplyNotice);
    //
    //boolean audit(SupplyNoticeEntity supplyNotice);
    //
    //boolean delete(SupplyNoticeEntity supplyNotice);
    //
    //boolean close(SupplyNoticeEntity supplyNotice);

}
