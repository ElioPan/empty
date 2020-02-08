package com.ev.framework.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.JSONUtils;
import com.ev.framework.utils.RSAUtils;
import com.ev.framework.filter.XssFilter;
import com.google.common.collect.Maps;

/**
 * @author gumingjie
 * @date 2019/11/13
 */
public class AppServiceTimeFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(XssFilter.class);
	public List<String> excludes = new ArrayList<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("app servcieTime filter init~~~~~~~~~~~~");
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
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		 if(handleExcludeURL(request, response)){
	            filterChain.doFilter(servletRequest, servletResponse);
	            return;
	        }
		// 获取redis 中的服务码
		StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) WebApplicationContextUtils
				.getRequiredWebApplicationContext(request.getServletContext()).getBean("stringRedisTemplate");
		Object obj = stringRedisTemplate.opsForValue().get("serviceCode");
		if (obj != null) {
			String serviceCode = new String(Base64.getDecoder().decode(obj.toString().getBytes()), StandardCharsets.UTF_8.name());
			Map<String, Object> serviceCodeMap = JSONUtils.jsonToMap(serviceCode);
			// 验签
			boolean verify = false;
			try {
				String publicKey = serviceCodeMap.get("publicKey").toString();
				String serviceLicense = serviceCodeMap.get("serviceLicense").toString();
				String endTime = serviceCodeMap.get("serviceEndTime").toString();
				verify = RSAUtils.verify(endTime.getBytes(), publicKey, serviceLicense);
				if (!verify) {
					response.setCharacterEncoding(StandardCharsets.UTF_8.name());
					response.setContentType("application/json; charset=UTF-8");
					Map<String, Object> results = Maps.newHashMap();
					results.put("code", 97);
					results.put("msg", "用户服务码不正确，请获取正确的服务码！");
					String rJson = JSON.toJSONString(results);
					OutputStream out = response.getOutputStream();
					out.write(rJson.getBytes(StandardCharsets.UTF_8.name()));
					out.flush();
					return;
				}
				Date now = new Date();
//    			Date endTime = DateFormatUtil.getDateByParttern(Date,"yyyy-MM-dd");
				String startTime = serviceCodeMap.get("serviceStartDate").toString();
				Date serviceStartDate = DateFormatUtil.getDateByParttern(startTime);
				Date serviceEndTime = DateFormatUtil.getDateByParttern(endTime);
				if (serviceStartDate.after(now) || serviceEndTime.before(now)) {
					response.setCharacterEncoding(StandardCharsets.UTF_8.name());
					response.setContentType("application/json; charset=UTF-8");
					Map<String, Object> results = Maps.newHashMap();
					results.put("code", 96);
					results.put("msg", "服务不在期限内");
					String rJson = JSON.toJSONString(results);
					OutputStream out = response.getOutputStream();
					out.write(rJson.getBytes(StandardCharsets.UTF_8.name()));
					out.flush();
					return;
				}
				filterChain.doFilter(servletRequest,servletResponse);
                return;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("application/json; charset=UTF-8");
		Map<String, Object> results = Maps.newHashMap();
		results.put("code", 95);
		results.put("msg", "没有生成服务码，请先获取服务码");
		String rJson = JSON.toJSONString(results);
		OutputStream out = response.getOutputStream();
		out.write(rJson.getBytes(StandardCharsets.UTF_8.name()));
		out.flush();
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
