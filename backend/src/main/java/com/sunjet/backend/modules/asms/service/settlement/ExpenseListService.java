package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.dto.asms.settlement.ExpenseItemInfo;

import java.util.List;

/**
 * 服务站结算费用子行
 * Created by Administrator on 2016/10/26.
 */
public interface ExpenseListService {

    ExpenseItemInfo save(ExpenseItemInfo expenseItemInfo);

    ExpenseItemInfo findOne(String objId);

    boolean delete(ExpenseItemInfo expenseItemInfo);

    boolean delete(String objId);

    List<ExpenseItemInfo> findByDealerSettlementId(String objId);


    boolean deleteByDealerSettlementId(String objId);

    public List<String> findAllDealerSettlementObjIdBySrcDocNo(String srcDocNo);

    public List<String> findAllDealerSettlementObjIdByVin(String vin);
}
