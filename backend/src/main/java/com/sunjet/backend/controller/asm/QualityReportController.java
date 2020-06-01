package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.modules.asms.service.asm.QualityReportService;
import com.sunjet.dto.asms.asm.QualityReportInfo;
import com.sunjet.dto.asms.asm.QualityReprotItem;
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
 * 售后质量速报
 */
@Slf4j
@RestController
@RequestMapping("/qualityReport")
public class QualityReportController {

    @Autowired
    QualityReportService qualityReportService;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<QualityReprotItem> getPageList(@RequestBody PageParam<QualityReprotItem> pageParam) {
        PageResult<QualityReprotItem> pageResult = qualityReportService.getPageList(pageParam);
        return pageResult;
    }

    @PostMapping("/findOneById")
    public QualityReportInfo findOneById(@RequestBody String objId) {
        return qualityReportService.findOne(objId);
    }


    @PostMapping("/save")
    public QualityReportEntity save(@RequestBody QualityReportEntity qualityReportEntity) {
        return qualityReportService.save(qualityReportEntity);
    }

    @DeleteMapping("/delete")
    public boolean deleteEntity(@RequestBody String objId) {
        return qualityReportService.delete(objId);
    }

    /**
     * 通过关键字搜索质量速报
     *
     * @param map
     * @return
     */
    @PostMapping("findAllByKeywordAndDealerCode")
    public List<QualityReportInfo> findAllByKeywordAndDealerCode(@RequestBody Map<String, String> map) {
        String keyword = map.get("keyword");
        String dealerCode = map.get("dealerCode");
        return qualityReportService.findAllByKeywordAndDealerCode(keyword, dealerCode);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return qualityReportService.startProcess(variables);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return qualityReportService.desertTask(objId);
    }

}
