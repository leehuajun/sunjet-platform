package com.sunjet.backend.modules.asms.repository.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.RecycleItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 故障返回件子行
 */
public interface RecycleItemRepository extends JpaRepository<RecycleItemEntity, String>, JpaSpecificationExecutor<RecycleItemEntity> {

    /**
     * 通过recycle(故障件返回单) 查找返回单子行列表
     *
     * @param recycle
     * @return
     */
    @Query("select ri from RecycleItemEntity ri where ri.recycle=?1")
    List<RecycleItemEntity> findByRecycle(String recycle);

    /**
     * 通过（返回单子行id） 查找返回通知单子行需求列表
     *
     * @param noticeItemId
     * @return
     */
    @Query("select ri from RecycleItemEntity ri where ri.noticeItemId=?1")
    List<RecycleItemEntity> findAllByNoticeItemId(String noticeItemId);

    /**
     * 通过父类objId 删除子行明细
     *
     * @param objId
     */
    @Modifying
    @Query("delete from RecycleItemEntity ri where ri.recycle=?1 ")
    void deleteByRecycleObjId(String objId);

    /**
     * 通过来源单号查询所有故障件明细
     *
     * @param srcDocNo
     * @return
     */
    @Query("select ri from  RecycleItemEntity  ri where ri.srcDocNo like ?1")
    List<RecycleItemEntity> findAllRecycleItemBySrcDocNo(String srcDocNo);

    @Query("select ri from  RecycleItemEntity  ri where ri.srcDocNo in (?1)")
    List<RecycleItemEntity> findAllRecycleItemBySrcDocNos(List<String> srcDocIds);
}
