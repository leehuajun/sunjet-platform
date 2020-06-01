package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.ExpenseReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 费用速报 dao
 * Created by lhj on 16/9/17.
 */
public interface ExpenseReportRepository extends JpaRepository<ExpenseReportEntity, String>, JpaSpecificationExecutor<ExpenseReportEntity> {


    //@Query("select ex from ExpenseReportEntity ex left join fetch ex.reportVehicles where ex.objId=?1")
    //ExpenseReportEntity findOneWithVehicles(String objId);
    //
    //@Query("select ex from ExpenseReportEntity ex left join fetch ex.reportVehicles left join fetch ex.reportParts where ex.objId=?1")
    //ExpenseReportEntity findOneWithVehiclesAndParts(String objId);
    //
    //@Query("select ex from ExpenseReportEntity ex where ex.docNo like ?1")
    //List<ExpenseReportEntity> findAllByKeyword(String keyword);
    //
    @Query("select ex from ExpenseReportEntity ex where ex.docNo like ?1 and ex.dealerCode=?2 and ex.status=3 order by ex.createdTime desc")
    List<ExpenseReportEntity> findAllByKeywordAndDealerCode(String keyword, String dealerCode);
}
