package com.sunjet.backend.modules.asms.repository.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 故障件返回单 dao
 * <p>
 * Created by lhj on 16/9/17.
 */
public interface RecycleRepository extends JpaRepository<RecycleEntity, String>, JpaSpecificationExecutor<RecycleEntity> {
    //@Query("select rp from RecycleEntity rp left join fetch rp.items where rp.objId=?1")
    //RecycleEntity findOneWithItems(String objId);
    //
    @Query("select rp from RecycleEntity rp where  rp.status=?1")
    List<RecycleEntity> findAllbySettlement(int status);
}
