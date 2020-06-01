package com.sunjet.frontend.service.asm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.asm.ReportPartInfo;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.frontend.service.basic.PartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/4.
 * 车辆子行
 */
@Slf4j
@Service("reportPartService")
public class ReportPartService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private PartService partService;

    /**
     * 通过objId查找一个质量速报配件
     *
     * @param objId
     * @return
     */

    public List<ReportPartInfo> findByQrId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            List<ReportPartInfo> reportPartInfoList = restClient.findAll("/reportPart/findByQrId", requestEntity, new ParameterizedTypeReference<List<ReportPartInfo>>() {
            });
            for (ReportPartInfo reportPartInfo : reportPartInfoList) {
                PartInfo partInfo = partService.findOne(reportPartInfo.getPart_id());
                if (partInfo != null) {
                    reportPartInfo.setPart(partInfo);
                }
            }
            log.info("ReportPartServiceImpl:findByQrId:success");
            return reportPartInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportPartServiceImpl:findByQrId:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 objId 查找一个费用速报
     *
     * @param objId
     * @return
     */

    public List<ReportPartInfo> findByCrId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            List<ReportPartInfo> reportPartInfoList = restClient.findAll("/reportPart/findByCrId", requestEntity, new ParameterizedTypeReference<List<ReportPartInfo>>() {
            });
            for (ReportPartInfo reportPartInfo : reportPartInfoList) {
                PartInfo partInfo = partService.findOne(reportPartInfo.getPart_id());
                if (partInfo != null) {
                    reportPartInfo.setPart(partInfo);
                }
            }

            log.info("ReportPartServiceImpl:findByCrId:success");
            return reportPartInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportPartServiceImpl:findByCrId:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除 reportPartInfo 实体
     *
     * @param reportPartInfo
     * @return
     */

    public boolean delete(ReportPartInfo reportPartInfo) {
        try {
            HttpEntity<ReportPartInfo> requestEntity = new HttpEntity<>(reportPartInfo, null);
            ResponseEntity<Boolean> responseEntity = restClient.delete("/reportPart/delete", requestEntity, Boolean.class);
            log.info("ReportPartServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportPartServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 保存速报配件
     *
     * @param reportPart
     */
    public ReportPartInfo save(ReportPartInfo reportPart) {
        try {
            HttpEntity<ReportPartInfo> requesEntity = new HttpEntity<>(reportPart, null);
            ResponseEntity<ReportPartInfo> responseEntity = restClient.post("/reportPart/save", requesEntity, ReportPartInfo.class);
            ReportPartInfo reportPartInfo = responseEntity.getBody();
            PartInfo partInfo = partService.findOne(reportPartInfo.getPart_id());
            if (partInfo != null) {
                reportPartInfo.setPart(partInfo);
            }
            log.info("QualityReportServiceImpl:save:success");
            return reportPartInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("QualityReportServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }
}
