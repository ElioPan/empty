package com.ev.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@Component
class WebConfigurer extends WebMvcConfigurerAdapter {
	@Autowired
    ConstantsConfig constantsConfig;
	@Autowired
	LoginInterceptor loginInterceptor;

	// 这个方法是用来配置静态资源的，比如html，js，css，等等
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/files/**").addResourceLocations("file:///"+constantsConfig.getUploadPath());
	}

	// 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns("/**") 表示拦截所有的请求，
		// excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
//		registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/register","/wechat/register");
		super.addInterceptors(registry);    //较新Spring Boot的版本中这里可以直接去掉，否则会报错
	}

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation(constantsConfig.getTmpPath());
		return factory.createMultipartConfig();
	}

}