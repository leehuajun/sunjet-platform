package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.SupplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 供货单/发货单
 * Created by lhj on 16/9/17.
 */
public interface SupplyRepository extends JpaRepository<SupplyEntity, String>, JpaSpecificationExecutor<SupplyEntity> {

    //@Query("select rp from SupplyEntity rp left join fetch rp.items where rp.objId=?1")
    //SupplyEntity findOneWithPartsById(String objId);
    //
    @Query("select rp from SupplyEntity rp  where  rp.status=?1")
    List<SupplyEntity> findAllbySettlement(int status);
}
