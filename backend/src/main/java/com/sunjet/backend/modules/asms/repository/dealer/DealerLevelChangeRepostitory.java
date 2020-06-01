package com.sunjet.backend.modules.asms.repository.dealer;


import com.sunjet.backend.modules.asms.entity.dealer.DealerLevelChangeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 * 服务站等级变更
 */
public interface DealerLevelChangeRepostitory extends JpaRepository<DealerLevelChangeRequestEntity, String>, JpaSpecificationExecutor<DealerLevelChangeRequestEntity> {
    //@Query("select ae from DealerLevelChangeRequestEntity ae where code like ?1 or name like ?1")
    //List<DealerEntity> findAllByKeyword(String keyword);
}
