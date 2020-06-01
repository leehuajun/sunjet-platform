package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.ExpenseListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务站结算单子行
 * Created by Administrator on 2016/10/26.
 */
public interface ExpenseListRepository extends JpaRepository<ExpenseListEntity, String>, JpaSpecificationExecutor<ExpenseListEntity> {


    /**
     * 通过费用id查找费用需求列表
     *
     * @param objId
     * @return
     */
    @Query("select el from ExpenseListEntity el where el.dealerSettlementId=?1")
    List<ExpenseListEntity> findByDealerSettlementId(String objId);

    /**
     * 通过费用删除的同时删除与此关联的费用子行
     *
     * @param objId
     */
    @Modifying
    @Query("delete from ExpenseListEntity el where el.dealerSettlementId=?1")
    void deleteByDealerSettlementId(String objId);

    @Query("select el from ExpenseListEntity el where el.srcDocNo like ?1")
    List<ExpenseListEntity> findAllDealerSettlementObjIdBySrcDocNo(String srcDocNo);

    @Query("select el from ExpenseListEntity el where el.srcDocID in (?1)")
    List<ExpenseListEntity> findAllDealerSettlementObjIdBySrcId(List<String> srcObjIds);
}
