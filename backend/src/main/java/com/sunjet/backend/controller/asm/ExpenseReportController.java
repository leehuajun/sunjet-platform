package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.asm.ExpenseReportEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.ExpenseReportView;
import com.sunjet.backend.modules.asms.service.asm.ExpenseReportService;
import com.sunjet.dto.asms.asm.ExpenseReportInfo;
import com.sunjet.dto.asms.asm.ExpenseReprotItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/8/2.
 * 售后费用速报
 */
@Slf4j
@RestController
@RequestMapping("/expenseReport")
public class ExpenseReportController {

    @Autowired
    private ExpenseReportService expenseReportService;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<ExpenseReportView> getPageList(@RequestBody PageParam<ExpenseReprotItem> pageParam) {
        PageResult<ExpenseReportView> pageResult = expenseReportService.getPageList(pageParam);
        return pageResult;
    }

    @PostMapping("/findOneById")
    public ExpenseReportInfo findOneById(@RequestBody String objId) {
        return expenseReportService.findOneById(objId);
    }

    @PostMapping("/save")
    public ExpenseReportEntity save(@RequestBody ExpenseReportEntity expenseReportEntity) {
        return expenseReportService.save(expenseReportEntity);
    }

    @DeleteMapping("/delete")
    public boolean deleteEntity(@RequestBody String objId) {
        return expenseReportService.delete(objId);
    }


    /**
     * 通过关键字搜索费用速报
     *
     * @param map
     * @return
     */
    @PostMapping("findAllByKeywordAndDealerCode")
    public List<ExpenseReportInfo> findAllByKeywordAndDealerCode(@RequestBody Map<String, String> map) {
        String keyword = map.get("keyword");
        String dealerCode = map.get("dealerCode");
        return expenseReportService.findAllByKeywordAndDealerCode(keyword, dealerCode);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return expenseReportService.startProcess(variables);
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return expenseReportService.desertTask(objId);
    }

}
