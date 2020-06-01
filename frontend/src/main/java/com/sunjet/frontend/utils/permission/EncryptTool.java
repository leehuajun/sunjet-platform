package com.sunjet.frontend.utils.permission;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by lhj on 2015/10/13.
 */
public class EncryptTool {
    public static String generatePasswordSha1(String originPassword, String salt, Integer hashIterations) {
        return new Sha1Hash(originPassword, ByteSource.Util.bytes(salt), hashIterations).toString();
    }

    public static String generatePasswordMd5(String originPassword, String salt, Integer hashIterations) {
        return new Md5Hash(originPassword, ByteSource.Util.bytes(salt), hashIterations).toString();
    }
}
