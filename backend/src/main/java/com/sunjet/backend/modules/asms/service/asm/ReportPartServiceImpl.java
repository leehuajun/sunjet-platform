package com.sunjet.backend.modules.asms.service.asm;

import com.sunjet.backend.modules.asms.entity.asm.ReportPartEntity;
import com.sunjet.backend.modules.asms.repository.asm.ReportPartRepository;
import com.sunjet.dto.asms.asm.ReportPartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 速报配件子行
 */
@Transactional
@Service("reportPartService")
public class ReportPartServiceImpl implements ReportPartService {
    @Autowired
    private ReportPartRepository reportPartRepository;

    /**
     * 通过 objId 查找一个质量速报配件
     *
     * @param objId
     * @return
     */
    @Override
    public List<ReportPartEntity> findByQrId(String objId) {
        try {
            List<ReportPartEntity> entityList = reportPartRepository.findByQrId(objId);

            return entityList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过 objId 查找一个费用速报
     *
     * @param objId
     * @return
     */
    @Override
    public List<ReportPartEntity> findByCrId(String objId) {
        try {
            List<ReportPartEntity> entityList = reportPartRepository.findByCrId(objId);
            return entityList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存
     *
     * @param reportPartEntity
     * @return
     */
    @Override
    public ReportPartEntity save(ReportPartEntity reportPartEntity) {
        try {
            ReportPartEntity entity = reportPartRepository.save(reportPartEntity);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 crId(费用速报) 相关的速报
     *
     * @param crId
     * @return
     */
    @Override
    public boolean deleteByExpenseReportObjId(String crId) {
        try {
            reportPartRepository.deleteByExpenseReportObjId(crId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过 qrId(质量速报id) 删除 与 质量速报相关的速报
     *
     * @param qrId
     * @return
     */
    @Override
    public boolean deleteByQualityReportObjId(String qrId) {
        try {
            reportPartRepository.deleteByQualityReportObjId(qrId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 reportPartInfo 实体
     *
     * @param reportPartInfo
     * @return
     */
    @Override
    public boolean delete(ReportPartInfo reportPartInfo) {
        try {
            reportPartRepository.delete(reportPartInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
