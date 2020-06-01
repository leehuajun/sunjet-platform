package com.sunjet.backend.modules.asms.repository.dealer;


import com.sunjet.backend.modules.asms.entity.dealer.DealerAlterRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 * 服务站变更申请
 */
public interface DealerAlterRepostitory extends JpaRepository<DealerAlterRequestEntity, String>, JpaSpecificationExecutor<DealerAlterRequestEntity> {

    //@Query("select ae from DealerAlterRequestEntity ae where code like ?1 or name like ?1")
    //List<DealerEntity> findAllByKeyword(String keyword);
}
