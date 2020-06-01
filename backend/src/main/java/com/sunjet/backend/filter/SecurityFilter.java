package com.sunjet.backend.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: lhj
 * @create: 2017-07-01 13:42
 * @description: 说明
 */
@Slf4j
public class SecurityFilter implements Filter {

    //    @Autowired
//    private ServerConfig serverConfig;
    // 是否启用访问权限控制，默认为false
    private Boolean auth = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        log.info("auth-access:" + auth);
        if (this.auth) {
            if (httpRequest.getRequestURL().toString().contains("swagger")
                    || httpRequest.getRequestURL().toString().contains("webjars")
                    || httpRequest.getRequestURL().toString().contains("v2/api-docs")) {
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                chain.doFilter(request, response);
            } else if ("OPTIONS".equals(httpRequest.getMethod())) {
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                chain.doFilter(request, response);
            } else {
                log.info("身份验证！");
                final String ip = httpRequest.getHeader("CUSTOM_IP");
                final String token = httpRequest.getHeader("TOKEN");
                log.info("CUSTOM_ID:" + ip);
                log.info("CUSTOM_TOKEN:" + token);

                if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(token)) {
                    httpResponse.setCharacterEncoding("UTF-8");
                    httpResponse.setContentType("application/json; charset=utf-8");
                    httpResponse.setStatus(HttpServletResponse.SC_OK);
                    log.info("验证通过！");
                    chain.doFilter(request, response);
                } else {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write("拒绝访问:非法用户");
                    log.warn("拒绝访问:非法用户！");
                }
            }
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
