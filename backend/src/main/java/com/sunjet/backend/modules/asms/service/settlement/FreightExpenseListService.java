package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.dto.asms.settlement.FreightExpenseInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface FreightExpenseListService {

    FreightExpenseInfo save(FreightExpenseInfo freightExpenseInfo);

    FreightExpenseInfo findOne(String objId);

    boolean delete(FreightExpenseInfo freightExpenseInfo);

    boolean delete(String objId);


    List<FreightExpenseInfo> findByFreightSettlementId(String objId);


    boolean deleteByFreightSettlementId(String objId);

}
