package com.lesson.memo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//URLから検索キーワードを受け取るアノテーション↓
import org.springframework.web.bind.annotation.RequestParam;

import com.lesson.memo.model.Memo;
import com.lesson.memo.model.Priority;
import com.lesson.memo.repository.MemoRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/memos")
public class MemoController {

    @Autowired
    private MemoRepository memoRepository;

    @GetMapping
    public String list(Model model) {
        List<Memo> memos = memoRepository.findAll();
//        並び替え、優先度が高い順、Enumの定義の順番で０，１，２～sortが順番通りにしてくれてる　compareTo比較するメソッド
//        a.getPriority()とはPriorityのaの値を引っ張ってきている。aとbのpriorityを比較して
        memos.sort((a, b) -> a.getPriority().compareTo(b.getPriority()));
        model.addAttribute("memos", memos);
        return "memo-list";
    }
    
//    キーワード検索
    @GetMapping("/search")
    public String search(
//    		@RequestParamブラウザから送られた検索キーワードを引数として受け取る
    		@RequestParam(name = "keyword",defaultValue = "")String keyword,Model model) {
    			List<Memo> memos;
    			if(keyword.isBlank()) {
//    				検索キーワードが空の場合全てのメモを取得
    				memos = memoRepository.findAll();
    			}else {
    				memos = memoRepository.findByTitleContainingOrContentContaining(
    						keyword,keyword);
    			}
    			memos.sort((a,b) -> a.getPriority().compareTo(b.getPriority()));
    			model.addAttribute("memos",memos);
    			model.addAttribute("keyword",keyword);
    			
    			return "memo-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("memo", new Memo());
        model.addAttribute("priorities", Priority.values());
        return "memo-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid Memo memo,
            BindingResult result,Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("priorities", Priority.values());
            return "memo-form";
        }

        memo.setCreatedAt(LocalDateTime.now());
        memo.setUpdatedAt(LocalDateTime.now());
//        ここで↓DBに値を保存
        memoRepository.save(memo);
        return "redirect:/memos";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model,
            HttpServletResponse response) {
        Optional<Memo> memo = memoRepository.findById(id);
        if (memo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面にリダイレクト
        }

        model.addAttribute("memo", memo.get());
        return "memo-detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletResponse response) {
    	model.addAttribute("priorities", Priority.values());
        if (model.containsAttribute("memo")) {
            return "memo-form";
        }

        return memoRepository.findById(id)
                .map(memo -> {
                    model.addAttribute("memo", memo);
                    return "memo-form";
                })
                .orElseGet(() -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return "not-found";
                });
    }

    @PostMapping("/update/{id}")
    public String update(
    		@PathVariable Long id,
            @ModelAttribute @Valid Memo memo,
            BindingResult result,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Memo> opt = memoRepository.findById(id);
        if (opt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面表示
        }

        Memo memoToUpdate = opt.get();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.memo", result);
            redirectAttributes.addFlashAttribute("memo", memo);
            return "redirect:/memos/edit/" + id; // editにリダイレクト
        }

        memoToUpdate.setTitle(memo.getTitle());
        memoToUpdate.setContent(memo.getContent());
        memoToUpdate.setPriority(memo.getPriority());
        memoToUpdate.setUpdatedAt(LocalDateTime.now());
        memoRepository.save(memoToUpdate);

        return "redirect:/memos/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
        HttpServletResponse response) {
        if (memoRepository.existsById(id)) {
            memoRepository.deleteById(id);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found";
        }

        return "redirect:/memos";
    }
}
