package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.PartExpenseListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 配件结算单子行
 * Created by Administrator on 2016/10/26.
 */
public interface PartExpenseListRepository extends JpaRepository<PartExpenseListEntity, String>, JpaSpecificationExecutor<PartExpenseListEntity> {

    /**
     * 通过配件结算单id查找配件费用子行列表
     *
     * @param objId
     * @return
     */
    @Query("select pel from PartExpenseListEntity pel where pel.agencySettlementId=?1")
    public List<PartExpenseListEntity> findByAgencySettlementId(String objId);


    /**
     * 通过配件结算单id删除同时配件结算子行
     *
     * @param objId
     */
    @Modifying
    @Query("delete from PartExpenseListEntity pel where pel.agencySettlementId=?1")
    void deleteByAgencySettlementId(String objId);

    @Query("select pel from PartExpenseListEntity pel where pel.srcDocNo like ?1")
    List<PartExpenseListEntity> findAllAgencySettlementObjIdBySrcDocNo(String srcDocNo);

    @Query("select pel from PartExpenseListEntity pel where pel.srcDocID in (?1)")
    List<PartExpenseListEntity> findAllAgencySettlementObjIdBySrcId(List<String> supplyIds);
}
