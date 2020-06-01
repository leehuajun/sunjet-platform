package com.sunjet.backend.modules.asms.service.basic;

import com.sunjet.backend.modules.asms.entity.basic.DocumentNoEntity;
import com.sunjet.backend.modules.asms.repository.basic.DocumentNoRepository;
import com.sunjet.backend.utils.common.CommonHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * 编号生成类
 */
@Transactional
@Service
public class DocumentNoServiceImpl implements DocumentNoService {
    @Autowired
    private DocumentNoRepository repository;

    @Override
    public String getDocumentNo(String key) {
        String docNo = StringUtils.EMPTY;
        synchronized (this) {
            String strToday = LocalDate.now().toString().replace("-", "");
            DocumentNoEntity entity = repository.findOneByKey(key);
            if (entity == null) {
                DocumentNoEntity documentNoEntity = new DocumentNoEntity();
                documentNoEntity.setDocKey(key);
                documentNoEntity.setDocCode(CommonHelper.mapDocumentNo.get(key).toString());
                documentNoEntity.setLastNoDate(strToday);
                documentNoEntity.setLastNoSerialNumber(CommonHelper.genFixedStringBySn(1, 4));
                repository.save(documentNoEntity);
                docNo = CommonHelper.mapDocumentNo.get(key).toString() + strToday + CommonHelper.genFixedStringBySn(1, 4);
            } else {
                if (StringUtils.equals(strToday, entity.getLastNoDate())) {
                    Integer nowNo = Integer.parseInt(entity.getLastNoSerialNumber()) + 1;
                    entity.setLastNoSerialNumber(CommonHelper.genFixedStringBySn(nowNo, 4));
                    repository.save(entity);
                    docNo = CommonHelper.mapDocumentNo.get(key).toString() + strToday + CommonHelper.genFixedStringBySn(nowNo, 4);
                } else {
                    entity.setLastNoDate(strToday);
                    entity.setLastNoSerialNumber(CommonHelper.genFixedStringBySn(1, 4));
                    repository.save(entity);
                    docNo = CommonHelper.mapDocumentNo.get(key).toString() + strToday + CommonHelper.genFixedStringBySn(1, 4);
                }
            }
        }
        return docNo;
    }
}
