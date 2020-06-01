package com.sunjet.backend.modules.asms.service.asm;

import com.sunjet.backend.modules.asms.entity.asm.ExpenseReportEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.ExpenseReportView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.asm.ExpenseReportInfo;
import com.sunjet.dto.asms.asm.ExpenseReprotItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 售后费用速报
 * Created by lhj on 16/9/17.
 */
public interface ExpenseReportService extends BaseService {

    PageResult<ExpenseReportView> getPageList(PageParam<ExpenseReprotItem> pageParam);

    ExpenseReportInfo findOneById(String objId);

    ExpenseReportEntity save(ExpenseReportEntity expenseReportEntity);

    boolean delete(String objId);

    List<ExpenseReportInfo> findAllByKeywordAndDealerCode(String keyword, String dealerCode);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);

    //ExpenseReportEntity findOneWithVehicles(String objId);
    //
    //ExpenseReportEntity findOneWithVehiclesAndParts(String objId);
    //
    //List<ExpenseReportEntity> findAllByKeyword(String keyword);
    //
    //List<ExpenseReportEntity> findAllByKeywordAndDealerCode(String keyword, String dealerCode);
    //

}
