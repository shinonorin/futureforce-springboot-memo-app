package com.lesson.memo.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
//対象のフィールドやパラメータがnullではないことを検証
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//ここでクラスMemoを定義
@Entity
@Data
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "タイトルを入力してください")
    @Column(nullable = false, length = 100)
    private String title;

    @NotBlank(message = "内容を入力してください")
    @Column(nullable = false, length = 1000)
    private String content;
    
    @NotNull(message = "優先度を選択してください")
//    enumをDBにどう保存するか
    @Enumerated(EnumType.STRING)
//    DB側でnullを禁止
    @Column(nullable = false)
//    priority.javaでPriority型を定義してる
    private Priority priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
