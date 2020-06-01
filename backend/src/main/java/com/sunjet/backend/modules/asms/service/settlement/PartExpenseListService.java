package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.dto.asms.settlement.PartExpenseItemsInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface PartExpenseListService {
    PartExpenseItemsInfo save(PartExpenseItemsInfo partExpenseItemsInfo);

    PartExpenseItemsInfo findOne(String objId);

    boolean delete(PartExpenseItemsInfo partExpenseItemsInfo);

    boolean delete(String objId);

    List<PartExpenseItemsInfo> findByAgencySettlementId(String objId);


    boolean deleteByAgencySettlementId(String objId);

    List<String> findAllAgencySettlementObjIdBySrcDocNo(String srcDocNo);

    List<String> findAllAgencySettlementObjIdByVin(String vin);
}
