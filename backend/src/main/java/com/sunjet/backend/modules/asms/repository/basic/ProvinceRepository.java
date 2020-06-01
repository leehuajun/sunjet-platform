package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 省/直辖市（Region） dao
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface ProvinceRepository extends JpaRepository<ProvinceEntity, String>, JpaSpecificationExecutor<ProvinceEntity> {

}
