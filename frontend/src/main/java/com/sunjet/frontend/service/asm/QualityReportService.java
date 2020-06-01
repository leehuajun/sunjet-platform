package com.sunjet.frontend.service.asm;

import com.sunjet.dto.asms.asm.QualityReportInfo;
import com.sunjet.dto.asms.asm.QualityReprotItem;
import com.sunjet.dto.asms.asm.ReportPartInfo;
import com.sunjet.dto.asms.asm.ReportVehicleInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/8/2.
 */
@Slf4j
@Service("qualityReportService")
public class QualityReportService {

    @Autowired
    private RestClient restClient;

    /**
     * 速报配件
     */
    @Autowired
    private ReportPartService reportPartService;
    @Autowired
    private ReportVehicleService reportVehicleService;
    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */

    public PageResult<QualityReprotItem> getPageList(PageParam<QualityReprotItem> pageParam) {
        try {
            return restClient.getPageList("/qualityReport/getPageList", pageParam, new ParameterizedTypeReference<PageResult<QualityReprotItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("QualityReportServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId 查找实体
     *
     * @param objId
     * @return
     */

    public QualityReportInfo findOneById(String objId) {
        ResponseEntity<QualityReportInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/qualityReport/findOneById", requestEntity, QualityReportInfo.class);
            //获取速报实体
            QualityReportInfo qualityReportInfo = responseEntity.getBody();
            //获取配件和车辆
            qualityReportInfo = getVehicleAndPartByQr(qualityReportInfo);
            log.info("QualityReportServiceImpl:findOneById:success");
            return getVehicleAndPartByQr(qualityReportInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("QualityReportServiceImpl:findOneById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 新增/ 保存
     *
     * @param qualityReportInfo
     * @return
     */

    public QualityReportInfo save(QualityReportInfo qualityReportInfo) {
        try {
            HttpEntity<QualityReportInfo> requesEntity = new HttpEntity<>(qualityReportInfo, null);
            //保存质量速报
            ResponseEntity<QualityReportInfo> responseEntity = restClient.post("/qualityReport/save", requesEntity, QualityReportInfo.class);
            //保存速报配件
            for (ReportPartInfo reportPartInfo : qualityReportInfo.getReportPartInfos()) {
                reportPartService.save(reportPartInfo);

            }
            //保存速报车辆
            for (ReportVehicleInfo reportVehicleInfo : qualityReportInfo.getReportVehicleInfos()) {
                reportVehicleService.save(reportVehicleInfo);
            }
            qualityReportInfo = getVehicleAndPartByQr(responseEntity.getBody());
            return qualityReportInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> request = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/qualityReport/delete", request, Boolean.class);
            log.info("QualityReportServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("QualityReportServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 关键字搜索质量速报
     *
     * @param keyword
     * @param dealerCode
     * @return
     */

    public List<QualityReportInfo> findAllByKeywordAndDealerCode(String keyword, String dealerCode) {
        ResponseEntity<List> responseEntity = null;
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("dealerCode", dealerCode);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.get("/qualityReport/findAllByKeywordAndDealerCode", requestEntity, List.class);
            List body = responseEntity.getBody();
            List<QualityReportInfo> qualityReportInfos = new ArrayList<>();
            for (Object o : body) {
                qualityReportInfos.add(JsonHelper.map2Bean(o, QualityReportInfo.class));
            }

            return qualityReportInfos;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("QualityReportServiceImpl:findAllByKeywordAndDealerCode:error" + e.getMessage());
            return null;
        }


    }

    /**
     * 启动流程
     *
     * @param qualityReportInfo
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(QualityReportInfo qualityReportInfo, ActiveUser activeUser) {
        ResponseEntity<Map> responseEntity = null;
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = UserInfo.UserInfoBuilder.anUserInfo()
                .withLogId(activeUser.getLogId())
                .withAgency(activeUser.getAgency() == null ? null : activeUser.getAgency())
                .withDealer(activeUser.getDealer() == null ? null : activeUser.getDealer())
                .withName(activeUser.getUsername())
                .withObjId(activeUser.getUserId())
                .withRoles(activeUser.getRoles())
                .withUserType(activeUser.getUserType())
                .build();
        map.put("entity", qualityReportInfo);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/qualityReport/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 作废单据
     *
     * @param objId
     */
    public boolean desertTask(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/qualityReport/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 通过 质量速报获取配件和车辆
     *
     * @param qualityReportInfo
     * @return
     */
    public QualityReportInfo getVehicleAndPartByQr(QualityReportInfo qualityReportInfo) {
        //获取速报配件
        List<ReportPartInfo> reportPartInfos = reportPartService.findByQrId(qualityReportInfo.getObjId());
        qualityReportInfo.setReportPartInfos(reportPartInfos);
        //获取车辆
        List<ReportVehicleInfo> vehicleInfoList = reportVehicleService.findByQrId(qualityReportInfo.getObjId());
        qualityReportInfo.setReportVehicleInfos(vehicleInfoList);
        return qualityReportInfo;
    }

}
