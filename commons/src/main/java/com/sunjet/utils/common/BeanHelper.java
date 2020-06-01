package com.sunjet.utils.common;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lhj on 16/10/31.
 */
public class BeanHelper {
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        emptyNames.add("objId");
        emptyNames.add("docNo");
        emptyNames.add("createrId");
        emptyNames.add("enabled");
        emptyNames.add("createrName");
        emptyNames.add("createdTime");
        emptyNames.add("modifierId");
        emptyNames.add("modifierName");
        emptyNames.add("modifiedTime");

        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static String[] getIgnorePropertyNames() {
        Set<String> ignoreNames = new HashSet<>();
        ignoreNames.add("objId");
        ignoreNames.add("docNo");
        ignoreNames.add("createrId");
        ignoreNames.add("enabled");
        ignoreNames.add("createrName");
        ignoreNames.add("createdTime");
        ignoreNames.add("modifierId");
        ignoreNames.add("modifierName");
        ignoreNames.add("modifiedTime");
        ignoreNames.add("dealerName");
        ignoreNames.add("dealerAddress");
        ignoreNames.add("serviceManager");
        String[] result = new String[ignoreNames.size()];
        return ignoreNames.toArray(result);
    }
}
