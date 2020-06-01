package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.asm.ReportVehicleEntity;
import com.sunjet.backend.modules.asms.service.asm.ReportVehicleService;
import com.sunjet.dto.asms.asm.ReportVehicleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/4.
 * 车辆子行
 */
@Slf4j
@RestController
@RequestMapping("/reportVehicle")
public class ReportVehicleController {

    @Autowired
    private ReportVehicleService reportVehicleService;

    @PostMapping("/findByQrId")
    public List<ReportVehicleEntity> findByQrId(@RequestBody String objId) {
        return reportVehicleService.findByQrId(objId);
    }

    @PostMapping("/findByCrId")
    public List<ReportVehicleEntity> findByCrId(@RequestBody String objId) {
        return reportVehicleService.findByCrId(objId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody ReportVehicleInfo reportVehicleInfo) {
        return reportVehicleService.delete(reportVehicleInfo);
    }


    /**
     * 保存配件速报实体
     *
     * @param reportPartEntity
     * @return 配件速报实体
     */
    @PostMapping("/save")
    public ReportVehicleEntity save(@RequestBody ReportVehicleEntity reportPartEntity) {
        return reportVehicleService.save(reportPartEntity);
    }


}
