package com.lesson.memo.service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.lesson.memo.repository.AdminRepository;
import com.lesson.memo.model.Admin;
import org.springframework.security.core.userdetails.User;

@Service
//implementsはインターフェースを実装するときに使うJavaのキーワード
public class AdminDetailService implements UserDetailsService {
	private final AdminRepository adminRepository;
	public AdminDetailService(AdminRepository adminRepository) {
	    this.adminRepository = adminRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username)
	        throws UsernameNotFoundException {
				Admin admin = adminRepository.findByEmail(username)
			    .orElseThrow(() -> new UsernameNotFoundException("管理者が見つかりません"));
//				DBから取得した情報をSprigSecurityが使える形式に返す
				return User.withUsername(admin.getEmail())
						.password(admin.getPassword())
					    .roles("ADMIN")
					    .build();
	}
}
