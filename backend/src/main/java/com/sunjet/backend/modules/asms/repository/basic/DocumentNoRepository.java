package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.DocumentNoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 单据编号 dao
 * <p>
 * Created by Administrator on 2016/9/5.
 */
public interface DocumentNoRepository extends JpaRepository<DocumentNoEntity, String>, JpaSpecificationExecutor<DocumentNoEntity> {
    @Query("select dn from DocumentNoEntity dn where dn.docKey=?1")
    public DocumentNoEntity findOneByKey(String key);
}
