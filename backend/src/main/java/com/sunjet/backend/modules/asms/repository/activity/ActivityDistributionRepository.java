package com.sunjet.backend.modules.asms.repository.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/11/5.
 * 活动分配dao
 */
public interface ActivityDistributionRepository extends JpaRepository<ActivityDistributionEntity, String>, JpaSpecificationExecutor<ActivityDistributionEntity> {

    @Query("select ad from ActivityDistributionEntity ad where status=3 and ad.repair =false and ad.docNo like ?1 and ad.dealerCode=?2 order by ad.createdTime desc ")
    List<ActivityDistributionEntity> findAllByStatusAndKeywordAndDealerCode(String keyword, String dealerCode);

    @Query("select ad from ActivityDistributionEntity ad where ad.activityNoticeId =?1")
    List<ActivityDistributionEntity> findAllByActivityNoticeObjId(String objId);

    /**
     * 查询已经关闭的活动分配单
     *
     * @return
     */
    @Query("select ad from ActivityDistributionEntity ad where ad.status=3  ")
    List<ActivityDistributionEntity> findCloseActivityDistribution();

    //@Query("select ex from ActivityDistributionEntity ex left join fetch ex.activityVehicles where ex.objId=?1")
    //ActivityDistributionEntity findOneWithVehicles(String objId);
    //
    //// @Query("select distinct  de from ActivityDistributionEntity de left join fetch de.vehicles left join fetch de.parts where de.dealerCode like ?1 or de.dealerNames like ?1")
    //@Query("select de from ActivityDistributionEntity de left join fetch de.activityVehicles where de.dealerCode=?1 or de.dealerName like ?1")
    //List<ActivityDistributionEntity> findAllByKeyword(String keyword);
    //
    //@Query("select de from ActivityDistributionEntity de left join fetch de.activityVehicles where de.dealerCode=?1 or de.docNo=?2")
    //ActivityDistributionEntity findAllWithVehicleByDocNo(String s, String docNo);
    //
    //@Query("select de from ActivityDistributionEntity de where status=?1 and docNo like ?2 and de.dealerCode=?3")
    //List<ActivityDistributionEntity> findAllByStatusAndKeywordAndDealerCode(Integer status, String keyword, String dealerCode);
}
