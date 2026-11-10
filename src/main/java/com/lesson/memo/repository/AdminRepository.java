package com.lesson.memo.repository;

import java.util.Optional;
//JpaRepositoryはDBの操作を簡単に行うインターフェース
import org.springframework.data.jpa.repository.JpaRepository;
import com.lesson.memo.model.Admin;

//<Admin,Integer><操作するエンティティ,AdminのIDの型>
public interface AdminRepository extends JpaRepository <Admin,Integer>{
//	Optional<Admin>はAdminが見つかる場合と見つからない場合もある。↓アドレスで管理者を検索する機能
	Optional<Admin> findByEmail(String email);
}