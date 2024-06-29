package com.boot.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.boot.model.Student;
import com.boot.repo.UserRepository;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Service
public class UserserviceInpl implements UserService{
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public Student saveStudent(Student student, String path) {
		
		String encode = passwordEncoder.encode(student.getPassword());
		student.setPassword(encode);
		student.setRole("ROLE_USER");
		
		
		student.setEnable(false);
		student.setVerification(UUID.randomUUID().toString());
		
		
		Student save = this.repository.save(student);
		
		if(save!=null) {
			sendEmail(save, path);
		}
		
		return save;
	}

	@Override
	public void removeSessionMessage() {

		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();
		session.removeAttribute("msg");
		
	}

	@Override
	public void sendEmail(Student student, String path) {
		
		
		String from = "cryptosafu37@gmail.com";
	    String to = student.getEmail();
	    String subject = "Account Verification";
	    String content = "Dear " + student.getName() + ",<br>" +
                "Please click the link below to verify your registration:<br>" +
                "<h3><a href=\"" + path + "/verify?code=" + student.getVerification() + "\" target=\"_self\">VERIFY</a></h3>" +
                "Thank you";
	    
	    
	    try {
	        MimeMessage mimeMessage = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setFrom(from, "Safu");
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(content, true); // true indicates HTML content

	        mailSender.send(mimeMessage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    System.out.println("send");
	}

	@Override
	public boolean verifyAccount(String verificationCode) {
		
		Student student = repository.findByverification(verificationCode);
		
		
		if(student==null) {
			return false;
		}else {
			
			
			student.setEnable(true);
			student.setVerification(null);
			
			repository.save(student);
			return true;
		}
		
	}	
	
}

