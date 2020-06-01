package com.sunjet.backend.controller.settlement;

import com.sunjet.backend.modules.asms.entity.settlement.view.AgencySettlementView;
import com.sunjet.backend.modules.asms.service.settlement.AgencySettlementService;
import com.sunjet.backend.modules.asms.service.settlement.PartExpenseListService;
import com.sunjet.dto.asms.settlement.AgencySettlementInfo;
import com.sunjet.dto.asms.settlement.AgencySettlementItem;
import com.sunjet.dto.asms.settlement.PartExpenseItemsInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 配件结算单
 */
@RestController
@RequestMapping("/agencySettlement")
public class AgencySettlementController {

    @Autowired
    private AgencySettlementService agencySettlementService;
    @Autowired
    private PartExpenseListService partExpenseListService;

    /**
     * 新增
     *
     * @param agencySettlementInfo
     * @return
     */
    @PostMapping("/save")
    public AgencySettlementInfo save(@RequestBody AgencySettlementInfo agencySettlementInfo) {
        return agencySettlementService.save(agencySettlementInfo);
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String ObjId) {
        return agencySettlementService.delete(ObjId);
    }

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<AgencySettlementView> getPageList(@RequestBody PageParam<AgencySettlementItem> pageParam) {
        return agencySettlementService.getPageList(pageParam);
    }

    /**
     * 通过objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOneById")
    public AgencySettlementInfo findOneById(@RequestBody String objId) {
        return agencySettlementService.findOne(objId);
    }


    /**
     * 通过配件结算单id查找配件费用子行列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/findByAgencySettlementId")
    public List<PartExpenseItemsInfo> findByAgencySettlementId(@RequestBody String objId) {
        return partExpenseListService.findByAgencySettlementId(objId);
    }

    /**
     * 通过配件结算单id删除同时配件结算子行
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/deleteByAgencySettlementId")
    public boolean deleteByAgencySettlementId(@RequestBody String ObjId) {
        return partExpenseListService.deleteByAgencySettlementId(ObjId);
    }

    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return agencySettlementService.startProcess(variables);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return agencySettlementService.desertTask(objId);
    }
}
