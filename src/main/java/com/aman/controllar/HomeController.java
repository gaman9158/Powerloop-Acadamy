package com.aman.controllar;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.aman.model.UserDtls;
import com.aman.repository.UserRepository;
import com.aman.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserService us;

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
	public String createUser(@ModelAttribute UserDtls user, HttpSession session) {

		boolean f = us.checkEmail(user.getEmail());

		if (f) {
			System.out.println("email already exists");
			session.setAttribute("msg", "email already exists");
//			session.removeAttribute("Msg");
		} else {

			UserDtls userDtls = us.createUser(user);

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

}
