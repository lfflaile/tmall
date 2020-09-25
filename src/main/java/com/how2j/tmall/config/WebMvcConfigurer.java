package com.how2j.tmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.how2j.tmall.interceptor.LoginInterceptor;
import com.how2j.tmall.interceptor.OtherInterceptor;

@Configuration
class WebMvcConfigurer extends WebMvcConfigurerAdapter{

	@Bean
	public LoginInterceptor getLoginInterceptor(){
		return new LoginInterceptor();
	}
	
    @Bean
    public OtherInterceptor getOtherIntercepter() {
        return new OtherInterceptor();
    }
	
	//加入拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//不管啥都拦截一下测试一下
		registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(getOtherIntercepter()).addPathPatterns("/**");
	}
	
}

