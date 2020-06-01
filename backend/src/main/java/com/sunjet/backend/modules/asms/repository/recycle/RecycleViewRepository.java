package com.sunjet.backend.modules.asms.repository.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 故障件返回单 视图
 * <p>
 * Created by lhj on 16/9/17.
 */
public interface RecycleViewRepository extends JpaRepository<RecycleView, String>, JpaSpecificationExecutor<RecycleView> {

}
