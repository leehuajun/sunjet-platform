package com.sunjet.backend.modules.asms.repository.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 故障件返回通知单子行
 * <p>
 * Created by lhj on 16/9/17.
 */
public interface RecycleNoticeItemRepository extends JpaRepository<RecycleNoticeItemEntity, String>, JpaSpecificationExecutor<RecycleNoticeItemEntity> {


    /**
     * 通过故障件返回通知单objId 查找返回通知列表
     *
     * @param recycleNoticeId
     * @return
     */
    @Query("select rn from RecycleNoticeItemEntity rn where rn.recycleNoticeId=?1")
    List<RecycleNoticeItemEntity> findByRecycleNoticeId(String recycleNoticeId);


    /**
     * 通过返回通知单 objId 删除与返回通知子行相关联的数据
     *
     * @param objId
     */
    @Modifying
    @Query("delete from RecycleNoticeItemEntity rn where rn.recycleNoticeId=?1")
    void deleteByRecycleNoticeId(String objId);


    @Query("select rn from  RecycleNoticeItemEntity  rn where  rn.recycleNoticeId in (?1)")
    List<RecycleNoticeItemEntity> findAllByRecycleNoticeObjIds(List<String> recycleNoticeObjIds);


    ///**
    // * 根据配件编号或名字关键字搜索故障件配件信息
    // * @param key
    // * @return
    // */
    //@Query("select v from RecycleNoticeItemEntity v where v.currentAmount>0 and (v.partCode like ?1 or v.partName like ?1)")
    //List<RecycleNoticeItemEntity> findCanReturnParts(String key);

}
