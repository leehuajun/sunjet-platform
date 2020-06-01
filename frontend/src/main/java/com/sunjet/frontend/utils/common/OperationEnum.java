package com.sunjet.frontend.utils.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lhj
 * @create: 2017-11-16 15:59
 * @description: 说明
 */
public enum OperationEnum {
    SEARCH("查询", "search"),
    CREATE("创建", "create"),
    MODIFY("修改", "modify"),
    DELETE("删除", "delete"),
    AUDIT("审核", "audit"),
    DISAUDIT("弃审", "disaudit"),
    IMPORT("导入", "import"),
    EXPORT("导出", "export"),
    PRINT("打印", "print");


    // 成员变量
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String code;

    // 构造方法
    private OperationEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // 普通方法
    public static String getName(String code) {
        for (OperationEnum c : OperationEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

    public static OperationEnum getDocStatus(String code) {
        for (OperationEnum c : OperationEnum.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        return null;
    }
}
