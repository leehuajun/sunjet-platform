package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.FreightExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 运费结算单子行
 * Created by Administrator on 2016/10/26.
 */
public interface FreightExpenseRepository extends JpaRepository<FreightExpenseEntity, String>, JpaSpecificationExecutor<FreightExpenseEntity> {


    /**
     * 通过运费结算id查找运费子行列表
     *
     * @param objId
     * @return
     */
    @Query("select fe from FreightExpenseEntity fe where fe.freightSettlementId=?1")
    List<FreightExpenseEntity> findByFreightSettlementId(String objId);


    /**
     * 删除配件运费结算的同时把运费结算子行一同删除
     *
     * @param objId
     */
    @Modifying
    @Query("delete from FreightExpenseEntity fe where fe.freightSettlementId=?1")
    void deleteByFreightSettlementId(String objId);
}
