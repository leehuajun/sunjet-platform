package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.FreightSettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 运输费用结算单
 * Created by Administrator on 2016/10/26.
 */
public interface FreightSettlementRepository extends JpaRepository<FreightSettlementEntity, String>, JpaSpecificationExecutor<FreightSettlementEntity> {
    //@Query("select u from FreightSettlementEntity u left join fetch u.items where u.objId=?1 ")
    //public FreightSettlementEntity findOneById(String objId);
    //
    //@Query("select u from FreightExpenseEntity u  where u.freightSettlement.objId=?1 ")
    //List<FreightExpenseEntity> findFreightExpenseById(String objId);
}
