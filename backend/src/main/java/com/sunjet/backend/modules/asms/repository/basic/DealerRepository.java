package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务站
 * Created by Administrator on 2016/9/5.
 */
public interface DealerRepository extends JpaRepository<DealerEntity, String>, JpaSpecificationExecutor<DealerEntity> {
    @Query("select de from DealerEntity de where de.enabled=1 and (de.code like ?1 or de.name like ?1)")
    List<DealerEntity> findAllByKeyword(String keyword);

    @Query("select  de from DealerEntity de where de.enabled=1 and de.parentId = null and (de.code like ?1 or de.name like ?1)")
    List<DealerEntity> findAllParentDealers(String keyword);


    //
    ////    @Query("select de from DealerEntity de where de.parent=null and de.objId in (select distinct de2.parent.objId from DealerEntity de2 where de2.parent<>null)")
    //@Query("select distinct de.parent from DealerEntity de where de.enabled=1 and de.parent<>null")
    //List<DealerEntity> findParentDealers();
    //
    //@Query("select de from DealerEntity de where de.enabled=1 and de.parent=null and de.city=?1 and de.objId in (select distinct de2.parent.objId from DealerEntity de2 where de2.parent<>null)")
    //List<DealerEntity> findParentDealersByCity(CityEntity selectedCity);
    //
    //@Query("select de from DealerEntity de where de.enabled=1 and de.parent=null and de.province=?1 and de.objId in (select distinct de2.parent.objId from DealerEntity de2 where de2.parent<>null)")
    //List<DealerEntity> findParentDealersByProvince(ProvinceEntity selectedProvince);
    //
    //@Query("select distinct  de from DealerEntity de where de.enabled=1 and de.parent=null and (de.name like ?1 or de.code like ?1)")
    //List<DealerEntity> findWithoutChild(String keyword);
    //
    //@Query("select de from DealerEntity de where de.enabled=1 and de.parent.objId=?1")
    //List<DealerEntity> findChildrenByParentId(String objId);
    //
    @Query("select de from DealerEntity de where de.enabled=1 and de.parentId=?1 and (de.code like ?2 or de.name like ?2)")
    List<DealerEntity> findChildrenByParentIdAndFilter(String objId, String filter);

    //
    @Query("select distinct de.parentId from DealerEntity de where de.enabled=1 and de.code=?1")
    DealerEntity findParentDealerByDealerCode(String DealerCode);

    //
    @Query("select de from DealerEntity de where de.enabled=1 and de.serviceManagerId=?1 and (de.code like ?2 or de.name like ?2)")
    List<DealerEntity> findAllByKeywordAndServiceManager(String userId, String s);

    @Query("select de from DealerEntity de where de.enabled=1 and de.code=?1")
    DealerEntity findOneByCode(String code);

    @Query("select de from DealerEntity de where de.enabled=1 and de.serviceManagerId =?1")
    List<DealerEntity> findAllByServiceManagerId(String serviceManagerId);

    @Query("select de from DealerEntity de where de.enabled=1 and de.serviceManagerId is null ")
    List<DealerEntity> findAllNotServiceManager();


//  @Query("select de from DealerEntity de where de.enabled=1 and de.parent=null and (de.code like ?1 or de.name like ?1)")
//  List<DealerEntity> findParentByKeyword(String s);
}
