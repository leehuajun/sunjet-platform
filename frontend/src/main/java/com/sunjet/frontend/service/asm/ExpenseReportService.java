package com.sunjet.frontend.service.asm;

import com.sunjet.dto.asms.asm.*;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
 * 售后费用速报
 */
@Slf4j
@Service("expenseReportService")
public class ExpenseReportService {

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

    public PageResult<ExpenseReprotItem> getPageList(PageParam<ExpenseReprotItem> pageParam) {
        try {
            return restClient.getPageList("/expenseReport/getPageList", pageParam, new ParameterizedTypeReference<PageResult<ExpenseReprotItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ExpenseReportServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */

    public ExpenseReportInfo findOneById(String objId) {
        ResponseEntity<ExpenseReportInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/expenseReport/findOneById", requestEntity, ExpenseReportInfo.class);
            ExpenseReportInfo expenseReportInfo = responseEntity.getBody();
            expenseReportInfo = getVehicleAndPartByEr(expenseReportInfo);
            log.info("ExpenseReportServiceImpl:findOneById:success");
            return expenseReportInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ExpenseReportServiceImpl:findOneById:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 新增保存
     *
     * @param expenseReportInfo
     * @return
     */

    public ExpenseReportInfo save(ExpenseReportInfo expenseReportInfo) {
        try {
            HttpEntity<ExpenseReportInfo> requesEntity = new HttpEntity<>(expenseReportInfo, null);
            //保存质量速报
            ResponseEntity<ExpenseReportInfo> responseEntity = restClient.post("/expenseReport/save", requesEntity, ExpenseReportInfo.class);
            //保存速报配件
            for (ReportPartInfo reportPartInfo : expenseReportInfo.getReportPartInfos()) {
                reportPartService.save(reportPartInfo);
            }
            //保存速报车辆
            for (ReportVehicleInfo reportVehicleInfo : expenseReportInfo.getReportVehicleInfos()) {
                reportVehicleService.save(reportVehicleInfo);
            }
            expenseReportInfo = getVehicleAndPartByEr(responseEntity.getBody());

            log.info("ExpenseReportServiceImpl:save:success");
            return expenseReportInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ExpenseReportServiceImpl:save:error:" + e.getMessage());
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
            responseEntity = restClient.delete("/expenseReport/delete", request, Boolean.class);
            log.info("ExpenseReportServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ExpenseReportServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过关键字和服务站信息查询费用速报
     *
     * @param keyword
     * @param dealerCode
     * @return
     */

    public List<ExpenseReportInfo> findAllByKeywordAndDealerCode(String keyword, String dealerCode) {
        ResponseEntity<List> responseEntity = null;
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("dealerCode", dealerCode);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.get("/expenseReport/findAllByKeywordAndDealerCode", requestEntity, List.class);
            List body = responseEntity.getBody();
            List<ExpenseReportInfo> expenseReportInfoList = new ArrayList<>();
            for (Object o : body) {
                expenseReportInfoList.add(JsonHelper.map2Bean(o, ExpenseReportInfo.class));
            }

            return expenseReportInfoList;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("ExpenseReportServiceImpl:findAllByKeywordAndDealerCode:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 启动流程
     *
     * @param expenseReportInfo
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(ExpenseReportInfo expenseReportInfo, ActiveUser activeUser) {
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
        map.put("entity", expenseReportInfo);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/expenseReport/startProcess", requestEntity, Map.class);
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
            responseEntity = restClient.delete("/expenseReport/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 通过 费用速报获取配件和车辆
     *
     * @param expenseReportInfo
     * @return
     */
    public ExpenseReportInfo getVehicleAndPartByEr(ExpenseReportInfo expenseReportInfo) {
        if (StringUtils.isNotBlank(expenseReportInfo.getObjId())) {
            //获取速报配件
            List<ReportPartInfo> reportPartInfos = reportPartService.findByCrId(expenseReportInfo.getObjId());
            expenseReportInfo.setReportPartInfos(reportPartInfos);
            //获取车辆
            List<ReportVehicleInfo> vehicleInfoList = reportVehicleService.findByCrId(expenseReportInfo.getObjId());
            expenseReportInfo.setReportVehicleInfos(vehicleInfoList);
        }
        return expenseReportInfo;
    }
}
