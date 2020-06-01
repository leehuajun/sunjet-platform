package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 配置参数DAO
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface ConfigRepository extends JpaRepository<ConfigEntity, String>, JpaSpecificationExecutor<ConfigEntity> {

    @Query("select ce.configValue from ConfigEntity ce where ce.configKey =?1")
    String getValueByKey(String key);

}
