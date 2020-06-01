package com.sunjet.backend.modules.asms.repository.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleNoticeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 故障件返回通知单
 * Created by lhj on 16/9/17.
 */
public interface RecycleNoticeViewRepository extends JpaRepository<RecycleNoticeView, String>, JpaSpecificationExecutor<RecycleNoticeView> {

}
