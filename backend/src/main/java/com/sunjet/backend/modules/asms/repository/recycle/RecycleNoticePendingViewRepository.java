package com.sunjet.backend.modules.asms.repository.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleNoticePendingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 故障件返回通知单
 * Created by lhj on 16/9/17.
 */
public interface RecycleNoticePendingViewRepository extends JpaRepository<RecycleNoticePendingView, String>, JpaSpecificationExecutor<RecycleNoticePendingView> {

    /**
     * 根据配件编号或配件名称搜索配件需求列表
     *
     * @param key
     * @param dealerCode
     * @return
     */
    @Query("select v from RecycleNoticePendingView v where v.currentAmount>0 and v.dealerCode=?2 and (v.partCode like ?1 or v.partName like ?1)")
    List<RecycleNoticePendingView> findByReturnOrParts(String key, String dealerCode);

}
