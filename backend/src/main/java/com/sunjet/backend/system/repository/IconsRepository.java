package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.IconEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * IconsRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface IconsRepository extends JpaRepository<IconEntity, String>, JpaSpecificationExecutor<IconEntity> {


}
