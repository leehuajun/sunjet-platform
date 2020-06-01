package com.sunjet.backend.controller.asm;


import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.settlement.view.PendingSettlementDetailsViews;
import com.sunjet.backend.modules.asms.service.settlement.PendingSettlementDetailsService;
import com.sunjet.dto.asms.settlement.*;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 待结算清单(配件／服务／运费)
 */
@RestController
@RequestMapping("/pendingSettlementDetails")
public class PendingSettlementDetailsController {

    @Autowired
    private PendingSettlementDetailsService pendingSettlementDetailsService;

    /**
     * 新增
     *
     * @param pendingSettlementDetailsEntity
     * @return
     */
    @PostMapping("/save")
    public PendingSettlementDetailsEntity save(@RequestBody PendingSettlementDetailsEntity pendingSettlementDetailsEntity) {
        return pendingSettlementDetailsService.save(pendingSettlementDetailsEntity);
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String ObjId) {
        return pendingSettlementDetailsService.delete(ObjId);
    }

    /**
     * 查一个vo实体
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOneById")
    public PendingSettlementDetailInfo findOneById(@RequestBody String objId) {
        return pendingSettlementDetailsService.findOne(objId);
    }

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<PendingSettlementDetailsViews> getPageList(@RequestBody PageParam<PendingSettlementDetailItems> pageParam) {
        return pendingSettlementDetailsService.getPageList(pageParam);
    }

    /**
     * 根据单据编号获取一个实体对象
     *
     * @param srcDocID
     * @return
     */
    @PostMapping("/getOneBySrcDocID")
    public PendingSettlementDetailsEntity getOneBySrcDocID(@RequestBody String srcDocID) {
        return pendingSettlementDetailsService.getOneBySrcDocID(srcDocID);
    }

    /**
     * 根据结算单ID 获取待结算单列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/getPendingsBySettlementID")
    public List<PendingSettlementDetailInfo> getPendingsBySettlementID(@RequestBody String objId) {
        return pendingSettlementDetailsService.getPendingsBySettlementID(objId);
    }


    /**
     * 获取服务结算单
     *
     * @param map
     * @return
     */
    @PostMapping("/getDealerSelttlements")
    public List<PendingSettlementDetailInfo> getDealerSelttlements(@RequestBody Map<String, DealerSettlementInfo> map) {
        DealerSettlementInfo dealerSettlementInfo = map.get("dealerSettlementInfo");
        return pendingSettlementDetailsService.getDealerSelttlements(dealerSettlementInfo.getDealerCode(), dealerSettlementInfo.getStartDate(), dealerSettlementInfo.getEndDate());
    }

    /**
     * 获取运费结算单
     *
     * @param map
     * @return
     */
    @PostMapping("/getFreightSelttlements")
    public List<PendingSettlementDetailInfo> getFreightSelttlements(@RequestBody Map<String, FreightExpenseInfo> map) {
        FreightExpenseInfo freightExpenseInfo = map.get("freightExpenseInfo");
        return pendingSettlementDetailsService.getFreightSelttlements(freightExpenseInfo.getDealerCode(), freightExpenseInfo.getStartDate(), freightExpenseInfo.getEndDate());
    }

    /**
     * 获取运费结算单
     *
     * @param map
     * @return
     */
    @PostMapping("/getAgencySelttlements")
    public List<PendingSettlementDetailInfo> getAgencySelttlements(@RequestBody Map<String, PartExpenseItemsInfo> map) {
        PartExpenseItemsInfo partExpenseItemsInfo = map.get("partExpenseItemsInfo");
        return pendingSettlementDetailsService.getAgencySelttlements(partExpenseItemsInfo.getAgencyCode(), partExpenseItemsInfo.getStartDate(), partExpenseItemsInfo.getEndDate());
    }
}
