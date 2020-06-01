package com.sunjet.backend.controller.settlement;

import com.sunjet.backend.modules.asms.entity.settlement.view.FreightSettlementView;
import com.sunjet.backend.modules.asms.service.settlement.FreightExpenseListService;
import com.sunjet.backend.modules.asms.service.settlement.FreightSettlementService;
import com.sunjet.dto.asms.settlement.FreightExpenseInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementInfo;
import com.sunjet.dto.asms.settlement.FreightSettlementItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 运费结算单
 */
@RestController
@RequestMapping("/freightSettlement")
public class FreightSettlementController {

    @Autowired
    private FreightSettlementService freightSettlementService;
    @Autowired
    private FreightExpenseListService freightExpenseListService;

    /**
     * 新增
     *
     * @param freightSettlementInfo
     * @return
     */
    @PostMapping("/save")
    public FreightSettlementInfo save(@RequestBody FreightSettlementInfo freightSettlementInfo) {
        return freightSettlementService.save(freightSettlementInfo);
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String ObjId) {
        return freightSettlementService.delete(ObjId);
    }

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<FreightSettlementView> getPageList(@RequestBody PageParam<FreightSettlementItem> pageParam) {
        return freightSettlementService.getPageList(pageParam);
    }

    /**
     * 查找一个实体
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOneById")
    public FreightSettlementInfo findOneById(@RequestBody String objId) {
        return freightSettlementService.findOne(objId);
    }


    /**
     * 通过运费结算id查找运费子行列表
     *
     * @param objId
     * @return
     */
    @PostMapping("/findByFreightSettlementId")
    public List<FreightExpenseInfo> findByFreightSettlementId(@RequestBody String objId) {
        return freightExpenseListService.findByFreightSettlementId(objId);
    }


    /**
     * 删除配件运费结算的同时把运费结算子行一同删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/deleteByFreightSettlementId")
    public boolean deleteByFreightSettlementId(@RequestBody String ObjId) {
        return freightExpenseListService.deleteByFreightSettlementId(ObjId);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return freightSettlementService.startProcess(variables);
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return freightSettlementService.desertTask(objId);
    }

}
