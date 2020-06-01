package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.service.DictionaryService;
import com.sunjet.dto.system.admin.DictionaryInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/7/24.
 * 字典 数据
 */
@Slf4j
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/findAll")
    public List<DictionaryInfo> findAll() {
        return dictionaryService.findAll();
    }

    @PostMapping("/save")
    public DictionaryInfo save(@RequestBody DictionaryInfo dictionaryInfo) {
        return dictionaryService.save(dictionaryInfo);
    }

    @PostMapping("/findOne")
    public DictionaryInfo findOne(@RequestBody String objId) {
        return dictionaryService.findOne(objId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return dictionaryService.delete(objId);
    }

    @GetMapping("/findAllParent")
    public List<DictionaryInfo> findAllParent() {
        return dictionaryService.findAllParent();
    }

    @PostMapping("/findDictionariesByParentCode")
    public List<DictionaryInfo> findDictionariesByParentCode(@RequestBody String code) {
        return dictionaryService.findDictionariesByParentCode(code);
    }

    @PostMapping("/findDictionaryByCode")
    public DictionaryInfo findDictionaryByCode(@RequestBody String code) {
        return dictionaryService.findDictionaryByCode(code);
    }
}
