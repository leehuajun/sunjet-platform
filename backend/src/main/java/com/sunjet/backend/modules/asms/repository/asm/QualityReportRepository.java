package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 质量速报 dao
 * Created by lhj on 16/9/17.
 */
public interface QualityReportRepository extends JpaRepository<QualityReportEntity, String>, JpaSpecificationExecutor<QualityReportEntity> {

    //@Query("select ex from QualityReportEntity ex left join fetch ex.reportVehicles  where ex.objId=?1")
    //QualityReportEntity findOneWithVehicles(String objId);

    //@Query("select rp from QualityReportEntity rp left join fetch rp.reportParts where rp.objId=?1")
    //QualityReportEntity findOneWithPartes(String objId);
    //
    //@Query("select ex from QualityReportEntity ex left join fetch ex.reportVehicles left join fetch ex.reportParts where ex.objId=?1")
    //QualityReportEntity findOneWithVehiclesAndParts(String objId);
    //
    //@Query("select qr from QualityReportEntity qr where qr.docNo like ?1")
    //List<QualityReportEntity> findAllByKeyword(String keyword);
    //
    //@Query("select qr from QualityReportEntity qr where qr.docNo like ?1 and qr.dealerCode=?2 and qr.status=3 order by qr.createdTime desc")
    //List<QualityReportEntity> findAllByKeywordAndDealerCode(String keyword, String dealerCode);
}
