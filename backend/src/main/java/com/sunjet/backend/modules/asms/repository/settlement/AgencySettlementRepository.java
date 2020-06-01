package com.sunjet.backend.modules.asms.repository.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.AgencySettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 费用结算单合作库
 * Created by Administrator on 2016/10/26.
 */
public interface AgencySettlementRepository extends JpaRepository<AgencySettlementEntity, String>, JpaSpecificationExecutor<AgencySettlementEntity> {
    //@Query("select rp from AgencySettlementEntity rp left join fetch rp.items where rp.objId=?1")
    //AgencySettlementEntity findOneById(String objId);
    //
    //@Query("select rp from PartExpenseListEntity rp  where rp.agencySettlement.objId=?1")
    //List<PartExpenseListEntity> findPartExpenseListById(String objId);
}
