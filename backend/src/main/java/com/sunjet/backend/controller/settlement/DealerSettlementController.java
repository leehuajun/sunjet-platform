package com.sunjet.backend.controller.settlement;

import com.sunjet.backend.modules.asms.entity.settlement.view.DealerSettlementView;
import com.sunjet.backend.modules.asms.service.settlement.DealerSettlementService;
import com.sunjet.backend.modules.asms.service.settlement.ExpenseListService;
import com.sunjet.backend.modules.asms.service.settlement.PendingSettlementDetailsService;
import com.sunjet.dto.asms.settlement.DealerSettlementInfo;
import com.sunjet.dto.asms.settlement.DealerSettlementItem;
import com.sunjet.dto.asms.settlement.ExpenseItemInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 三包费用结算
 */
@RestController
@RequestMapping("/dealerSettlement")
public class DealerSettlementController {

    @Autowired
    private DealerSettlementService dealerSettlementService;
    @Autowired
    private ExpenseListService expenseListService;
    @Autowired
    private PendingSettlementDetailsService pendingSettlementDetailsService;

    /**
     * 新增
     *
     * @param dealerSettlementInfo
     * @return
     */
    @PostMapping("/save")
    public DealerSettlementInfo save(@RequestBody DealerSettlementInfo dealerSettlementInfo) {
        return dealerSettlementService.save(dealerSettlementInfo);
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String ObjId) {
        return dealerSettlementService.delete(ObjId);
    }

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<DealerSettlementView> getPageList(@RequestBody PageParam<DealerSettlementItem> pageParam) {
        return dealerSettlementService.getPageList(pageParam);
    }

    /**
     * 查询一个实体
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOneById")
    public DealerSettlementInfo findOneById(@RequestBody String objId) {
        return dealerSettlementService.findOne(objId);
    }

    /**
     * 通过费用id查找费用需求列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/findByDealerSettlementId")
    public List<ExpenseItemInfo> findByDealerSettlementId(@RequestBody String objId) {
        return expenseListService.findByDealerSettlementId(objId);
    }


    /**
     * 通过费用删除的同时删除与此关联的费用子行
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/deleteByDealerSettlementId")
    public boolean deleteByDealerSettlementId(@RequestBody String ObjId) {
        return expenseListService.deleteByDealerSettlementId(ObjId);
    }


    public List<PendingSettlementDetailInfo> getDealerSelttlements(@RequestBody Map<String, PendingSettlementDetailInfo> map) {
        PendingSettlementDetailInfo pendingSettlementDetailInfo = map.get("pendingSettlementDetailInfo");
        return pendingSettlementDetailsService.getDealerSelttlements(pendingSettlementDetailInfo.getDealerCode(), pendingSettlementDetailInfo.getBusinessDate(), pendingSettlementDetailInfo.getBusinessDate());
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return dealerSettlementService.startProcess(variables);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return dealerSettlementService.desertTask(objId);
    }
}
