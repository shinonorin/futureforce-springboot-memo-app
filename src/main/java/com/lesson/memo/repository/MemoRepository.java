package com.lesson.memo.repository;
//java標準で用意しているListという仕組みを使えるようにする宣言
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lesson.memo.model.Memo;

public interface MemoRepository extends JpaRepository<Memo, Long> {
//	findByタイトルOr本文に指定した文字列を含むメモを検索する
	List<Memo> findByTitleContainingOrContentContaining(String titleKeyword,String contentKeyward);
}