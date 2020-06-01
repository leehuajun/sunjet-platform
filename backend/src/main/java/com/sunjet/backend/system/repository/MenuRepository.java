package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * UserRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface MenuRepository extends JpaRepository<MenuEntity, String>, JpaSpecificationExecutor<MenuEntity> {

    //@Query("select m from MenuEntity m where m.enabled=true order by m.seq asc")
    //public List<MenuEntity> findAllByAsc();
    //
    //@Query("select m from MenuEntity m where m.url=?1")
    //MenuEntity findOneByUrl(String url);
    //
    @Query("select m from MenuEntity m where m.enabled=true order by m.seq asc")
    public List<MenuEntity> findAll();


    @Query("select m from MenuEntity m where m.enabled =true and m.permissionCode in (?1) order by m.seq asc")
    public List<MenuEntity> findAllByCodes(ArrayList<String> codes);

    @Query("select m from MenuEntity m where m.enabled = true and m.objId in(select DISTINCT m1.parent.objId from MenuEntity m1 where m1.permissionCode in (?1)) order by m.seq asc")
    public List<MenuEntity> findParentsByCodes(ArrayList<String> codes);

    @Query("select m from MenuEntity m where m.url=?1")
    MenuEntity findMenuByUrl(String url);
}
