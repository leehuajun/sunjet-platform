package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.view.FreightSettlementView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.settlement.FreightSettlementItem;
import com.sunjet.dto.asms.settlement.FreightSettlementInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface FreightSettlementService extends BaseService {

    //FreightSettlementEntity findOneById(String objId);
    //
    //List<FreightExpenseEntity> findFreightExpenseById(String objId);

    FreightSettlementInfo save(FreightSettlementInfo freightSettlementInfo);

    FreightSettlementInfo findOne(String objId);

//    boolean delete(FreightSettlementInfo freightSettlementInfo);

    boolean delete(String objId);


    /**
     * 分页
     *
     * @return
     */
    public PageResult<FreightSettlementView> getPageList(PageParam<FreightSettlementItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);
}
