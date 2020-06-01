package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.repository.asm.ReportVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("qrVehicleService")
public class QrVehicleServiceImpl implements QrVehicleService {
    @Autowired
    private ReportVehicleRepository reportVehicleRepository;


    //@Override
    //public List<ReportVehicleEntity> filter(String keyword) {
    //    return reportVehicleRepository.fitlter(keyword);
    //}
}
