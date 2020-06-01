package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.view.DealerSettlementView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.settlement.DealerSettlementInfo;
import com.sunjet.dto.asms.settlement.DealerSettlementItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 * 三包费用结算
 */
public interface DealerSettlementService extends BaseService {
    //DealerSettlementEntity findOneById(String objId);
    //
    //List<ExpenseListEntity> findExpenseListById(String objId);

    DealerSettlementInfo save(DealerSettlementInfo dealerSettlementInfo);

    DealerSettlementInfo findOne(String objId);

//    boolean delete(DealerSettlementInfo dealerSettlementInfo);

    boolean delete(String objId);


    /**
     * 分页
     *
     * @return
     */
    public PageResult<DealerSettlementView> getPageList(PageParam<DealerSettlementItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);
}
