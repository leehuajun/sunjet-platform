package com.sunjet.backend.modules.asms.repository.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 活动通知车辆子行
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityVehicleRepository extends JpaRepository<ActivityVehicleEntity, String>, JpaSpecificationExecutor<ActivityVehicleEntity> {
    @Query("select count(av) from ActivityVehicleEntity av where av.activityNoticeId = ?1 ")
    Integer countVehicleSize(String objId);

    @Query("select av from ActivityVehicleEntity av where av.activityNoticeId = ?1 and av.vehicleId = ?2")
    ActivityVehicleEntity findOneByActivityNoticeIdAndVehicleId(String activityNoticeId, String vehicleId);

    /**
     * 通过活动通知单的ID查当前活动单下的所有车辆
     *
     * @param objId
     * @return
     */
    @Query("select av from ActivityVehicleEntity av where av.activityNoticeId = ?1")
    List<ActivityVehicleEntity> findVehicleListByActibityNoticeId(String objId);

    /**
     * 删除一条ActivityVehicle记录
     *
     * @param activityVehicleId
     */
    @Modifying
    @Query("delete from ActivityVehicleEntity av where av.objId=?1")
    void deleteActivityVehicleById(String activityVehicleId);

    /**
     * 通过活动通知单ID删除该活动单下的所有活动车辆
     *
     * @param activityNoticeId
     */
    @Modifying
    @Query("delete from ActivityVehicleEntity av where av.activityNoticeId = ?1")
    void deleteByActivityNoticeId(String activityNoticeId);

    @Query("SELECT av FROM  ActivityVehicleEntity av where av.activityDistributionId =?1")
    List<ActivityVehicleEntity> findAllVehicleByActivityDistributionObjId(String objId);

    @Query("select av from ActivityVehicleEntity av where av.vehicleId =?1")
    List<ActivityVehicleEntity> findAllActivityVehicleByVehicelId(String objId);

    @Query("select av from ActivityVehicleEntity av where av.vehicleId in (?1) ")
    List<ActivityVehicleEntity> findAllByVehicelIds(List<String> vehiceleObjIds);

    @Query("SELECT av FROM  ActivityVehicleEntity av where av.distribute = false and av.activityNoticeId =?1")
    List<ActivityVehicleEntity> findUndistributedActivityVehiclesByNoticeId(String objId);

    @Query("SELECT av FROM  ActivityVehicleEntity av where av.repair = false and av.activityNoticeId =?1")
    List<ActivityVehicleEntity> findUnRepairActivityVehiclesByNoticeId(String objId);

    /**
     * 查询活动分配未维修的车辆
     *
     * @param objId 活动分配单id
     * @return
     */
    @Query("SELECT av FROM  ActivityVehicleEntity av where av.repair = false and av.activityDistributionId =?1")
    List<ActivityVehicleEntity> findUnrepairActivityVehiclesByDistributionId(String objId);


    /**
     * 查询活动分配已经维修的车辆
     *
     * @param objId 活动分配单id
     * @return
     */
    @Query("SELECT av FROM  ActivityVehicleEntity av where av.repair = true and av.activityDistributionId =?1")
    List<ActivityVehicleEntity> findrepairActivityVehiclesByDistributionId(String objId);
    //@Query("select u from ActivityVehicleEntity u where u.vehicle.vin like ?1 ")
    //List<ActivityVehicleEntity> filter(String keyword);
//    @Query("select de from ActivityNoticeEntity de left join fetch de.vehicles left join fetch de.parts where de.docNo=?2 or de.title like ?1")
//    ActivityNoticeEntity findallDocNo(String s, String docNo);
//    @Query("select de from ActivityVehicleEntity de where de.docNo like ?2 or de.rowNum like ?1")
//    List<ActivityVehicleEntity> findAllByKeywordcurrent(String keyword, String current);
//    @Query("select de from ActivityVehicleEntity de where de.docNo like ?1")
//    List<ActivityVehicleEntity> findAllcurrent(String s);
}
