package com.sunjet.backend.modules.asms.service.asm;

import com.sunjet.backend.modules.asms.entity.asm.ReportVehicleEntity;
import com.sunjet.dto.asms.asm.ReportVehicleInfo;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/4.
 */
public interface ReportVehicleService {

    List<ReportVehicleEntity> findByQrId(String objId);

    List<ReportVehicleEntity> findByCrId(String objId);

    ReportVehicleEntity save(ReportVehicleEntity reportVehicleEntity);

    boolean delete(ReportVehicleInfo reportVehicleInfo);

    boolean deleteByExpenseReprotObId(String crId);

    boolean deleteByQualityReportObjId(String qrId);
}
