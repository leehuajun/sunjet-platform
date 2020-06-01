package com.sunjet.backend.modules.asms.repository.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleItemPartView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 故障返回单配件列表
 */
public interface RecycleItemPartViewRepository extends JpaRepository<RecycleItemPartView, String>, JpaSpecificationExecutor<RecycleItemPartView> {


    @Query("select ri from RecycleItemPartView ri where ri.recycle=?1")
    List<RecycleItemPartView> findByRecyclePartList(String recycle);

}
