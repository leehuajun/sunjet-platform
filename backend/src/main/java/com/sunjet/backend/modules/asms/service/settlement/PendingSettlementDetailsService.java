package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.settlement.view.PendingSettlementDetailsViews;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailItems;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 2016/11/14.
 * 待结算清单(配件／服务／运费)
 */
public interface PendingSettlementDetailsService {

    List<PendingSettlementDetailInfo> getAgencySelttlements(String agencyCode, Date startDate, Date endDate);

    PendingSettlementDetailsEntity getOneBySrcDocID(String srcDocID);

    List<PendingSettlementDetailInfo> getPendingsBySettlementID(String objId);

    List<PendingSettlementDetailInfo> getDealerSelttlements(String code, Date startDate, Date endDate);

    //
    List<PendingSettlementDetailInfo> getFreightSelttlements(String code, Date startDate, Date endDate);

    PendingSettlementDetailsEntity save(PendingSettlementDetailsEntity pendingSettlementDetailsEntity);

    PendingSettlementDetailInfo findOne(String objId);

    boolean delete(PendingSettlementDetailInfo pendingSettlementDetailInfo);

    boolean delete(String objId);

    /**
     * 分页
     *
     * @return
     */
    public PageResult<PendingSettlementDetailsViews> getPageList(PageParam<PendingSettlementDetailItems> pageParam);
}
