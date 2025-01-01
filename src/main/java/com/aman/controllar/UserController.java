package com.aman.controllar;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aman.model.UserDtls;
import com.aman.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	BCryptPasswordEncoder passwordEncode;

	@Autowired
	private UserRepository userRepo;

	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String email = p.getName();
		UserDtls user = userRepo.findByEmail(email);
		m.addAttribute("user", user);
	}

	@GetMapping("/")
	public String home() {
		return "user/home";
	}

	@GetMapping("/changePass")
	public String loadChangePassword() {
		return "user/change_Password";
	}

	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass,
			@RequestParam("newPass") String newPass, HttpSession session) {

		String email = p.getName();

		UserDtls loginUser = userRepo.findByEmail(email);

		boolean f = passwordEncode.matches(oldPass, loginUser.getPassword());

		if (f) {
			loginUser.setPassword(passwordEncode.encode(newPass));
			UserDtls updatePasswordUser = userRepo.save(loginUser);

			if (updatePasswordUser != null) {
				session.setAttribute("msg", "Password changed Successfully");

			} else {

				session.setAttribute("msg", "Something Wrong on Server");
			}

		} else {
			session.setAttribute("msg", "Your Old Password is Incorrect");
		}

		return "redirect:/user/changePass";
	}
}
