package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.MaintainTypeEntity;
import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 维修项目 主实体
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface MaintainTypeRepository extends JpaRepository<MaintainTypeEntity, String>, JpaSpecificationExecutor<MaintainTypeEntity> {
    @Query("select mte from MaintainTypeEntity mte where mte.parentId=?1")
    List<MaintainTypeEntity> findAllByParentId(String parentId);

    @Query("select mte from MaintainTypeEntity mte where mte.parentId=null ")
    List<MaintainTypeEntity> findModels();

    @Query("select mte from MaintainTypeEntity mte where mte.parentId=?1 ")
    List<MaintainTypeEntity> findSystems(String parentId);

    @Query("select mte from MaintainTypeEntity mte where mte.parentId=?1 ")
    List<MaintainTypeEntity> findSubSystems(String parentId);
}
