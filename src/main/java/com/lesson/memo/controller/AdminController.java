package com.lesson.memo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.lesson.memo.model.Admin;
import com.lesson.memo.repository.AdminRepository;

@Controller
public class AdminController {
//	finalは一度代入した参照を変更できない
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
//	コンストラクタ
	public AdminController(
			AdminRepository adminRepository,
			PasswordEncoder passwordEncoder) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping("/admin/signup")
	public String showSignupForm() {
		return "admin-signup";
	}
	@GetMapping("/admin/signin")
	public String showSigninForm() {
		return "admin-signin";
	}
	
	@PostMapping("/admin/signup")
	public String signup(
			@ModelAttribute Admin admin,
			RedirectAttributes redirectAttributes) {
//		アドレスが登録済か確認
		if(adminRepository.findByEmail(admin.getEmail()).isPresent()) {
//			redirectAttributesリダイレクト先へ渡すためのオブジェクト.addFlashAttribute一時的に保存するメソッド
			redirectAttributes.addFlashAttribute(
//					"情報につける名前","実際に渡すメッセージ"
					"errorMessage","このメールアドレスは既に登録されています。"
			);
			return "redirect:/admin/signup";
		}
//		hashedPasswordという変数名に、Adminから取得したpassをハッシュ化して代入
		String hashedPassword = passwordEncoder.encode(admin.getPassword());
// 		setterを使ってAdminのpasswordをハッシュ化済みの値に変更
		admin.setPassword(hashedPassword);
//		管理者情報をDBに保存
		adminRepository.save(admin);
		return "redirect:/admin/signin";
	}
}
