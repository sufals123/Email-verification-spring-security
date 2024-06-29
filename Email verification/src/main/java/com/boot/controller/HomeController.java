package com.boot.controller;

import java.io.Console;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.boot.model.Student;
import com.boot.repo.UserRepository;
import com.boot.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository repository;
	
	@ModelAttribute
	public void commonData(Principal p, Model m) {
		if (p != null) {
			String name = p.getName();
			Student email = repository.findByEmail(name);
			m.addAttribute("user", email);
		}
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/user/profile")
	public String profile(Principal p, Model m) {
		String student = p.getName();
		Student email = repository.findByEmail(student);
		m.addAttribute("student", email);
		return "profile";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute Student student, HttpSession session, HttpServletRequest request ) {
	    
	    System.out.println(student);
	    
	    StringBuffer requestURI = request.getRequestURL();
	    String contextPath = request.getContextPath();
	    
	    System.out.println("Request URI: " + requestURI);
	    System.out.println("Context Path: " + contextPath);
	    
	    // Extracting the base path
	    String basePath = requestURI.substring(0, requestURI.length() - request.getServletPath().length());
	    
	    System.out.println("Base Path: " + basePath);

	    Student s = this.userService.saveStudent(student, basePath);
	    
	    if (s != null) {
	        System.out.println("Student saved successfully...");
	    } else {
	        System.out.println("Something went wrong...");
	    }
	    
	    return "redirect:/register";
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code , Model m) {
		
		boolean verifyAccount = userService.verifyAccount(code);
		
		if(verifyAccount) {
			m.addAttribute("msg", "successfully your account is verified");
		}else {
			m.addAttribute("msg", "may be Your varification code is incorrect or alredy verifide");
		}
		
		return "message";
		
	}

	 
}

