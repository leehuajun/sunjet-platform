package com.sunjet.backend.controller.supply;

import com.sunjet.backend.modules.asms.entity.supply.view.SupplyView;
import com.sunjet.backend.modules.asms.service.supply.SupplyItemService;
import com.sunjet.backend.modules.asms.service.supply.SupplyService;
import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.asms.supply.SupplyItem;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 调拨供货单
 */
@RestController
@RequestMapping("/supply")
public class SupplyController {
    @Autowired
    private SupplyService supplyService;
    @Autowired
    private SupplyItemService supplyItemService;


    /**
     * 新增
     *
     * @param supplyInfo
     * @return
     */
    @PostMapping("/save")
    public SupplyInfo save(@RequestBody SupplyInfo supplyInfo) {
        return supplyService.save(supplyInfo);
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String ObjId) {
        return supplyService.delete(ObjId);
    }

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<SupplyView> getPageList(@RequestBody PageParam<SupplyItem> pageParam) {
        return supplyService.getPageList(pageParam);
    }


    /**
     * 查询一个对象实体
     *
     * @param ObjId
     * @return
     */
    @PostMapping("/findSupplyWithPartsById")
    public SupplyInfo findSupplyWithPartsById(@RequestBody String ObjId) {
        return supplyService.findOne(ObjId);
    }


    /**
     * 根据供货单id查供货单子行
     *
     * @param supplyId
     * @return
     */
    @PostMapping("/findBySupplyId")
    public List<SupplyItemInfo> findBySupplyId(@RequestBody String supplyId) {
        return supplyItemService.findBySupplyId(supplyId);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return supplyService.startProcess(variables);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return supplyService.desertTask(objId);
    }
}
