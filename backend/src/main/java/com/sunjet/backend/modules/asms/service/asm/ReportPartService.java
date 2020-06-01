package com.sunjet.backend.modules.asms.service.asm;

import com.sunjet.backend.modules.asms.entity.asm.ReportPartEntity;
import com.sunjet.dto.asms.asm.ReportPartInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface ReportPartService {
    //List<ReportPartEntity> filter(String keyword);

    List<ReportPartEntity> findByQrId(String objId);


    List<ReportPartEntity> findByCrId(String objId);

    ReportPartEntity save(ReportPartEntity reportPartEntity);

    boolean deleteByExpenseReportObjId(String crId);

    boolean deleteByQualityReportObjId(String qrId);

    boolean delete(ReportPartInfo reportPartInfo);
}
