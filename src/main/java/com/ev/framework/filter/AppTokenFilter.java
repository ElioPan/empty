package com.ev.framework.filter;


import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.StringUtils;
import com.ev.framework.filter.XssFilter;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yunian
 * @date 2018/6/18
 */
public class AppTokenFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(XssFilter.class);
    public List<String> excludes = new ArrayList<>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(logger.isDebugEnabled()){
            logger.debug("app token filter init~~~~~~~~~~~~");
        }
        String temp = filterConfig.getInitParameter("excludes");
        if (temp != null) {
            String[] url = temp.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        if(handleExcludeURL(request, response)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        if(StringUtils.isNotEmpty(token)){
            //校验令牌是否有效
            StringRedisTemplate stringRedisTemplate =
                    (StringRedisTemplate) WebApplicationContextUtils.
                            getRequiredWebApplicationContext(request.getServletContext()).
                            getBean("stringRedisTemplate");
            Object obj = stringRedisTemplate.opsForValue().get(Constant.REDIS_USER_TOKEN_PREFIX+token);
            if(obj!=null){
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }else{
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json; charset=UTF-8");
                Map<String,Object> results = Maps.newHashMap();
                results.put("code",98);
                results.put("msg","用户令牌不正确或者已过期，请重新登录！");
                String rJson = JSON.toJSONString(results);
                OutputStream out = response.getOutputStream();
                out.write(rJson.getBytes("UTF-8"));
                out.flush();
                return;
            }
        }else{
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=UTF-8");
            Map<String,Object> results = Maps.newHashMap();
            results.put("code",99);
            results.put("msg","请求没有传用户令牌");
            String rJson = JSON.toJSONString(results);
            OutputStream out = response.getOutputStream();
            out.write(rJson.getBytes("UTF-8"));
            out.flush();
            return;
        }
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }

        String url = request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void destroy() {

    }

}
