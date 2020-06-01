package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * IconsRepository
 * 公告
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface NoticeRepository extends JpaRepository<NoticeEntity, String>, JpaSpecificationExecutor<NoticeEntity> {


    @Query("select ne from NoticeEntity ne where ne.publishDate>?1")
    List<NoticeEntity> findLastNotices(Date time);
}
