package com.sunjet.utils.common;

import java.util.UUID;

/**
 * Created by lhj on 16/6/17.
 */
public class UUIDHelper {
    public static String newUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
