package com.sunjet.backend.filter;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by wfb on 17-3-27.
 * 实现一个监听用户请求的拦截器
 */
public class RequestLog extends HandlerInterceptorAdapter {

    /**
     * 前置检查
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取用户token
        Method method = handlerMethod.getMethod();
        System.out.println("\r\n\r\n");
        System.out.println("=================用户:" + ip + ",访问目标:" + method.getDeclaringClass().getName() + "." + method.getName());
        return true;
    }

    /**
     * controller处理完成
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        long startTime = (Long) request.getAttribute("requestStartTime");

        long endTime = System.currentTimeMillis();

        long executeTime = endTime - startTime;

        // log it
        if (executeTime > 1000) {
            System.out.println("=================[" + method.getDeclaringClass().getName() + "." + method.getName() + "] 执行耗时 : " + executeTime + "ms");
        } else {
            System.out.println("=================[" + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "] 执行耗时 : " + executeTime + "ms");
        }

    }

}