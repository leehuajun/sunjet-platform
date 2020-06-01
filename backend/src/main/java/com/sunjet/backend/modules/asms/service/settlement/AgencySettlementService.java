package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.view.AgencySettlementView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.settlement.AgencySettlementInfo;
import com.sunjet.dto.asms.settlement.AgencySettlementItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface AgencySettlementService extends BaseService {
    //AgencySettlementEntity findOneById(String objId);
    //
    //List<PartExpenseListEntity> findPartExpenseListById(String objId);

    AgencySettlementInfo save(AgencySettlementInfo agencySettlementInfo);

    AgencySettlementInfo findOne(String objId);

    boolean delete(AgencySettlementInfo agencySettlementInfo);

    boolean delete(String objId);

    /**
     * 分页
     *
     * @return
     */
    public PageResult<AgencySettlementView> getPageList(PageParam<AgencySettlementItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);
}
