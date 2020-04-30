package com.ev.framework.config;

import com.ev.framework.filter.AppServiceTimeFilter;
import com.ev.framework.filter.AppTokenFilter;
import com.google.common.collect.Maps;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
public class AppTokenFilterConfig {

	/**
	 * xss过滤拦截器
	 */
	@Bean
	public FilterRegistrationBean appTokenFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		
		filterRegistrationBean.setFilter(new AppTokenFilter());
		filterRegistrationBean.setOrder(0);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/apis/*");
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("excludes", "/apis/user/login*,/apis/user/getGroupsByUser,/apis/alarm/addEvAlarm,/apis/file/*,/apis/document/downloads,/apis/serviceCode/save,/apis/serviceCode/getServiceCode,/apis/user/checkToken,/apis/serviceCode/activateServiceCode");
		filterRegistrationBean.setInitParameters(initParameters);
		return filterRegistrationBean;
	}
	
	@Bean
	public FilterRegistrationBean serviceTimeRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new AppServiceTimeFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/apis/*");
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("excludes", "/apis/user/login*,/apis/user/getGroupsByUser,/apis/alarm/addEvAlarm,/apis/file/*,/apis/document/downloads,/apis/serviceCode/save,/apis/serviceCode/getServiceCode,/apis/user/checkToken,/apis/serviceCode/activateServiceCode");
		filterRegistrationBean.setInitParameters(initParameters);
		return filterRegistrationBean;
	}
/*	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*"); // 1
		corsConfiguration.addAllowedHeader("*"); // 2
		corsConfiguration.addAllowedMethod("*"); // 3
		return corsConfiguration;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 4
		return new CorsFilter(source);
	}*/

}
