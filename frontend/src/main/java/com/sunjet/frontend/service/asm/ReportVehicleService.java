package com.sunjet.frontend.service.asm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.asm.ReportVehicleInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.frontend.service.basic.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/4.
 * 速报车辆子行
 */
@Slf4j
@Service("reportVehicleService")
public class ReportVehicleService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private VehicleService vehicleService;

    /**
     * 通过 objId 查找一个质量速报
     *
     * @param objId
     * @return
     */

    public List<ReportVehicleInfo> findByQrId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            List<ReportVehicleInfo> reportVehicleInfoList = restClient.findAll("/reportVehicle/findByQrId", requestEntity, new ParameterizedTypeReference<List<ReportVehicleInfo>>() {
            });

            for (ReportVehicleInfo reportVehicleInfo : reportVehicleInfoList) {
                VehicleInfo vehicleInfo = vehicleService.findOne(reportVehicleInfo.getVehicle_id());
                if (vehicleInfo != null) {
                    reportVehicleInfo.setVehicle(vehicleInfo);
                }
            }

            log.info("ReportVehicleServiceImpl:findByQrId:success");
            return reportVehicleInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportVehicleServiceImpl:findByQrId:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 objId 查找一个费用速报
     *
     * @param objId
     * @return
     */

    public List<ReportVehicleInfo> findByCrId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            //ResponseEntity<String> responseEntity = restClient.post("/reportVehicle/findByCrId", requestEntity, String.class);
            //
            //List<ReportVehicleInfo> body = (List<ReportVehicleInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            //List<ReportVehicleInfo> reportVehicleInfoList = new ArrayList<>();
            //for (Object o : body) {
            //    ReportVehicleInfo reportVehicleInfo = JsonHelper.map2Bean(o, ReportVehicleInfo.class);
            //    reportVehicleInfoList.add(reportVehicleInfo);
            //}
            List<ReportVehicleInfo> reportVehicleInfoList = restClient.findAll("/reportVehicle/findByCrId", requestEntity, new ParameterizedTypeReference<List<ReportVehicleInfo>>() {
            });

            for (ReportVehicleInfo reportVehicleInfo : reportVehicleInfoList) {
                VehicleInfo vehicleInfo = vehicleService.findOne(reportVehicleInfo.getVehicle_id());
                if (vehicleInfo != null) {
                    reportVehicleInfo.setVehicle(vehicleInfo);
                }
            }
            log.info("ReportVehicleServiceImpl:findByCrId:success");
            return reportVehicleInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportVehicleServiceImpl:findByCrId:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除
     *
     * @param reportVehicleInfo
     * @return
     */

    public boolean delete(ReportVehicleInfo reportVehicleInfo) {
        try {
            HttpEntity<ReportVehicleInfo> requestEntity = new HttpEntity<>(reportVehicleInfo, null);
            ResponseEntity<Boolean> responseEntity = restClient.delete("/reportVehicle/delete", requestEntity, Boolean.class);
            log.info("ReportVehicleServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ReportVehicleServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 保存速报车辆信息
     *
     * @param reportVehicleInfo
     * @return 速报车辆信息
     */
    public ReportVehicleInfo save(ReportVehicleInfo reportVehicleInfo) {
        try {
            HttpEntity<ReportVehicleInfo> requesEntity = new HttpEntity<>(reportVehicleInfo, null);
            ResponseEntity<ReportVehicleInfo> responseEntity = restClient.post("/reportVehicle/save", requesEntity, ReportVehicleInfo.class);
            reportVehicleInfo = responseEntity.getBody();
            VehicleInfo vehicleInfo = vehicleService.findOne(reportVehicleInfo.getVehicle_id());
            if (vehicleInfo != null) {
                reportVehicleInfo.setVehicle(vehicleInfo);
            }
            return reportVehicleInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
