package com.lesson.memo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.authorizeHttpRequests(auth -> auth
	    		
//	    		管理者登録画面とログイン画面はログインしていなくても開けるように
	    		.requestMatchers("/admin/signup", "/admin/signin").permitAll()
//	        	上で指定したもの以外の全てのリクエストはログイン必須
	    		.anyRequest().authenticated()
	    );
	    http.formLogin(form -> form
	    		.loginPage("/admin/signin")
	    	    .loginProcessingUrl("/admin/signin")
	    	    .defaultSuccessUrl("/memo", true)
	    	    .failureUrl("/admin/signin?error")
	    	    .permitAll()
	    );
	    http.logout(logout -> logout
	    	    .logoutUrl("/admin/logout")
	    	    .logoutSuccessUrl("/admin/signin?logout")
	    	    .permitAll()
	    	);
	    return http.build();
	}
}
