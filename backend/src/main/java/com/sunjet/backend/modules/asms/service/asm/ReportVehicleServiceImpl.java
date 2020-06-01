package com.sunjet.backend.modules.asms.service.asm;

import com.sunjet.backend.modules.asms.entity.asm.ReportVehicleEntity;
import com.sunjet.backend.modules.asms.repository.asm.ReportVehicleRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.asm.ReportVehicleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/4.
 * 速报车辆子行
 */
@Slf4j
@Service("reportVehicleService")
public class ReportVehicleServiceImpl implements ReportVehicleService {

    @Autowired
    private ReportVehicleRepository reportVehicleRepository;

    /**
     * 通过 objId 查找一个速报车辆
     *
     * @param objId
     * @return
     */
    @Override
    public List<ReportVehicleEntity> findByQrId(String objId) {
        try {
            List<ReportVehicleEntity> entityList = reportVehicleRepository.findByQrId(objId);

            return entityList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过 objId 查找一个速报费用
     *
     * @param objId
     * @return
     */
    @Override
    public List<ReportVehicleEntity> findByCrId(String objId) {
        try {
            List<ReportVehicleEntity> entityList = reportVehicleRepository.findByCrId(objId);
            return entityList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 保存实体
     *
     * @param reportVehicleEntity
     * @return
     */
    @Override
    public ReportVehicleEntity save(ReportVehicleEntity reportVehicleEntity) {
        try {
            ReportVehicleEntity entity = reportVehicleRepository.save(reportVehicleEntity);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过 reportVehicleInfo 删除
     *
     * @param reportVehicleInfo
     * @return
     */
    @Override
    public boolean delete(ReportVehicleInfo reportVehicleInfo) {
        try {
            reportVehicleRepository.delete(reportVehicleInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除 与 crId(费用速报) 相关的速报
     *
     * @param crId
     * @return
     */
    @Override
    public boolean deleteByExpenseReprotObId(String crId) {
        try {
            reportVehicleRepository.deleteByExpenseReprotObId(crId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过 qrId(质量速报id) 删除与 质量速报相关速报
     *
     * @param qrId
     * @return
     */
    @Override
    public boolean deleteByQualityReportObjId(String qrId) {
        try {
            reportVehicleRepository.deleteByQualityReportObjId(qrId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
