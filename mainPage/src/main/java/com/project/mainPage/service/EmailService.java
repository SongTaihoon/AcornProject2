package com.project.mainPage.service;
import com.project.mainPage.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;
    
    public void mailSend(UserDto user) {
    	String title = "JEJU THE BEST 비밀번호 찾기 결과"; // 제목
    	String text = user.getUser_id() + " 님의 비밀번호 : " + user.getUser_pw(); // 내용
    	
        SimpleMailMessage message = new SimpleMailMessage(); // 이메일 보내는 객체
        message.setTo(user.getUser_email());  // 이메일 받는 사람
        message.setFrom("kvpark98@naver.com"); // 이메일 보내는 사람
        message.setSubject(title); // 제목
        message.setText(text); // 내용

        mailSender.send(message); // 이메일 보내기
    }
}