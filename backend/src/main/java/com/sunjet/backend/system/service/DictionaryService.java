package com.sunjet.backend.system.service;


import com.sunjet.dto.system.admin.DictionaryInfo;

import java.util.List;

/**
 * @author lhj
 * @create 2015-12-18 下午2:47
 */
public interface DictionaryService {

    /**
     * 保存
     *
     * @param dictionaryInfo
     * @return
     */
    DictionaryInfo save(DictionaryInfo dictionaryInfo);

    boolean delete(DictionaryInfo dictionaryInfo);

    boolean delete(String objId);

    DictionaryInfo findOne(String objId);

    /**
     * @return
     */
    //public List<DictionaryInfo> findDictionaryList();

    List<DictionaryInfo> findAll();

    //List<DictionaryInfo> findAllEnabled();

    List<DictionaryInfo> findAllParent();

    List<DictionaryInfo> findDictionariesByParentCode(String code);

    DictionaryInfo findDictionaryByCode(String code);
}
