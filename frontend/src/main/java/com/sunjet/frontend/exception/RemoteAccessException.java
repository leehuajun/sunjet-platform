package com.sunjet.frontend.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author: lhj
 * @create: 2017-07-03 22:11
 * @description: 说明
 */
public class RemoteAccessException extends AuthenticationException {
    public RemoteAccessException() {
//        super();
    }

    @Override
    public String getMessage() {
        return "远程服务访问异常!";
    }
}
