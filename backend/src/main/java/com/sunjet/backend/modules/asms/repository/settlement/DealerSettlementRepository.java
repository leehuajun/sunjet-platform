package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.DealerSettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 三包费用结算单
 * Created by Administrator on 2016/10/26.
 */
public interface DealerSettlementRepository extends JpaRepository<DealerSettlementEntity, String>, JpaSpecificationExecutor<DealerSettlementEntity> {

    //public DealerSettlementEntity findOneById(String objId);
    //
    //@Query("select u from ExpenseListEntity u  where u.dealerSettlement.objId=?1 ")
    //List<ExpenseListEntity> findExpenseListById(String objId);
}
