package com.aman.controllar;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aman.model.UserDtls;
import com.aman.repository.UserRepository;
import com.aman.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;


@Controller
public class HomeController {

	@Autowired
	private UserService us;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/index")
	public String index() {

		return "index";	
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {

		return "register";
	}

	@PostMapping("/createUser")
	public String createUser(@ModelAttribute UserDtls user, HttpSession session, HttpServletRequest request) {
		
		String url = request.getRequestURL().toString(); 
//		 http://localhost:8080/createUser

		url = url.replace(request.getServletPath(), "");
		

		boolean f = us.checkEmail(user.getEmail());

		if (f) {
			System.out.println("email already exists");
			session.setAttribute("msg", "email already exists");
//			session.removeAttribute("Msg");
		} else {

			UserDtls userDtls = us.createUser(user, url);

			if (userDtls != null) {
				System.out.println("Registered Successfully");
				session.setAttribute("msg", "Registered Successfully");
//				return "redirect:/register";
			} else {
				System.out.println("Something wrong on the server");
				session.setAttribute("msg", "Something wrong on the server");
//			return "redirect:/register";
			}
		}
		return "redirect:/register";
	}

	@Autowired
	private UserRepository userRepo;

	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		if (p != null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code) {
		if(us.verifyAccount(code)) {
			return "verify_success";
		}else {
			return "failed";
		}
	}
	
	@GetMapping("/loadForgotPassword")
	public String forgotPassword() {

		return "forgot_password";
	}

	@GetMapping("/loadResetPassword")
	public String loadResetPassword() {
		return "reset_password";
	}

	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email, @RequestParam String mobileNumber, HttpSession session) {

		UserDtls user = userRepo.findByEmailAndMobileNumber(email, mobileNumber);

		if (user != null) {
//			System.out.println("inside if");
			return "redirect:/loadResetPassword";
		} else {
			System.out.println("inside else");
			session.setAttribute("msg", "invalid email & mobile number");
			return "forgot_password";
		}

//		return "";
	}

	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String psw, @RequestParam(defaultValue = "0") Integer id
, HttpSession session) {
		System.out.println("inside change password ");
		UserDtls user = userRepo.findById(2).get();

		String encryptPsw = passwordEncoder.encode(psw);
		user.setPassword(encryptPsw);

		UserDtls updateUser = userRepo.save(user);

		if (updateUser != null) {
			session.setAttribute("msg", "Password Change successfully");
		}else {
			System.out.println("in else method");
		}

		return "redirect:/loadForgotPassword";
	}

}
