package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.service.BaseService;
import com.sunjet.backend.utils.BeanUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

/**
 * Created by SUNJET_WS on 2017/9/25.
 */
@Slf4j
@RestController
@RequestMapping("/base")
public class BaseContrller {

    @Autowired
    private ApplicationContext applicationContext;
    @Getter
    @Setter
    private BaseService baseService;

    @PostMapping("/save")
    public Object save(@RequestBody LinkedHashMap<String, Object> flowDocInfo) {
        Object info = null;

        try {
            String docNo = "";
            if (flowDocInfo.get("docNo") != null) {
                docNo = flowDocInfo.get("docNo").toString();
                info = BeanUtils.mapToObject(flowDocInfo, BeanUtils.getDocTypeByDocNo(docNo));
                this.setBaseService((BaseService) applicationContext.getBean(BeanUtils.getServiceByDocNo(docNo)));
            }
            Object save = this.baseService.getRepository().save(info);
            return save;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
