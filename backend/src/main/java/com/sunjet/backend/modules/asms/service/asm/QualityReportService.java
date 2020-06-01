package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.asm.QualityReportInfo;
import com.sunjet.dto.asms.asm.QualityReprotItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * 售后质量速报
 * Created by lhj on 16/9/17.
 */
public interface QualityReportService extends BaseService {


    QualityReportEntity save(QualityReportEntity qualityReportEntity);

    boolean delete(QualityReportInfo qualityReportInfo);

    boolean delete(String objId);

    QualityReportInfo findOne(String objId);

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<QualityReprotItem> getPageList(PageParam<QualityReprotItem> pageParam);

    List<QualityReportInfo> findAllByKeywordAndDealerCode(String keyword, String dealerCode);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);
}
