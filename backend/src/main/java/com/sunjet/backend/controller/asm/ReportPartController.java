package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.asm.ReportPartEntity;
import com.sunjet.backend.modules.asms.service.asm.ReportPartService;
import com.sunjet.dto.asms.asm.ReportPartInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/7.
 */
@Slf4j
@RestController
@RequestMapping("/reportPart")
public class ReportPartController {

    @Autowired
    private ReportPartService reportPartService;

    @PostMapping("/findByQrId")
    public List<ReportPartEntity> findByQrId(@RequestBody String objId) {
        return reportPartService.findByQrId(objId);
    }

    @PostMapping("/findByCrId")
    public List<ReportPartEntity> findByCrId(@RequestBody String objId) {
        return reportPartService.findByCrId(objId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody ReportPartInfo reportPartInfo) {
        return reportPartService.delete(reportPartInfo);
    }

    /**
     * 保存配件速报实体
     *
     * @param reportPartEntity
     * @return 配件速报实体
     */
    @PostMapping("/save")
    public ReportPartEntity save(@RequestBody ReportPartEntity reportPartEntity) {
        return reportPartService.save(reportPartEntity);
    }

}
