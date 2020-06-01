package com.sunjet.backend.modules.asms.repository.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 服务活动配件子行
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityPartRepository extends JpaRepository<ActivityPartEntity, String>, JpaSpecificationExecutor<ActivityPartEntity> {
    /**
     * 通过活动通知单ID删除该活动单下的所有活动配件
     *
     * @param activityNoticeId
     */
    @Modifying
    @Query("delete from ActivityPartEntity ap where ap.activityNoticeId = ?1")
    void deleteByActivityNoticeId(String activityNoticeId);
    //@Query("select r from ActivityPartEntity r  where r.part.code like ?1")
    //ActivityPartEntity filter(String keyword);
    //
    //@Query("select pe from ActivityPartEntity pe where pe.activityNotice.objId=?1")
    //List<ActivityPartEntity> findActivityPartListByNoticeId(String noticeId);
    //
    //@Query("select an from ActivityNoticeEntity an left join fetch an.activityParts where an.objId=?1")
    //ActivityNoticeEntity findOneWithPartById(String objId);
}
