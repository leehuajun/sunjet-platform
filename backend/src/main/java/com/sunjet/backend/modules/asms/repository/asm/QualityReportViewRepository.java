package com.sunjet.backend.modules.asms.repository.asm;

import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.QualityReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/3.
 * 售后质量速报
 */
public interface QualityReportViewRepository extends JpaRepository<QualityReportView, String>, JpaSpecificationExecutor<QualityReportView> {

    /**
     * 关键字搜素质量速报
     *
     * @param keyword
     * @param dealerCode
     * @return
     */
    @Query("select qr from QualityReportEntity qr where qr.docNo like ?1 and qr.dealerCode=?2 and qr.status=3 order by qr.createdTime desc")
    List<QualityReportEntity> findAllByKeywordAndDealerCode(String keyword, String dealerCode);
}
