package com.lesson.memo.model;

import java.time.LocalDateTime;
//このクラスをDBのテーブルを対応付けるエンティティとして扱う
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
//IDなどの値の生成方法を指定
import jakarta.persistence.GeneratedValue;
//IDの生成方法の種類を表す
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
//テーブル名をadminに指定
@Table(name = "admin")
public class Admin {	
//	idで区別できるようにする
	@Id
//	新しい管理者を登録するときにidを自動発行する
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "last_name",nullable = false)
	private String lastName;
	
	@Column(name = "first_name",nullable = false)
	private String firstName;
//	unique = true同じ値を重複できない
	@Column(nullable = false,unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
//	updatable = false更新の対象にならない
	@Column(name = "created_at",updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
//	コンストラクタ
	public Admin() {
	}
	
//	@PrePersist新しいエンティティをDBに登録する前に指定したメソッドを実行するためのアノテーション
	@PrePersist
	public void onCreate() {
//		現在の日時を取得してnowという変数へ代入
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
	}
//	@PreUpdate既存のエンティティを更新する前に指定したメソッドを実行するためのアノテーション
	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
//	getterとsetter達
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getId() {
		return id;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
