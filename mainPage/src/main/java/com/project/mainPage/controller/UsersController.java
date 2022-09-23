package com.project.mainPage.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.mainPage.dto.Criteria;
import com.project.mainPage.dto.EmailCheck;
import com.project.mainPage.dto.IdCheck;
import com.project.mainPage.dto.Notice;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.PhoneCheck;
import com.project.mainPage.dto.UsersDto;
import com.project.mainPage.mapper.UsersMapper;


@Controller
@RequestMapping("/users")
public class UsersController {
	@Autowired
	private UsersMapper usersMapper;
//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;
	
	//회원 리스트
	@GetMapping("/list/{page}")
	public String list(@PathVariable int page, Model model) {
		int row = 8;
		int startRow = (page - 1)*row;
		List<UsersDto> userList = usersMapper.selectPageAll(startRow,row);
		int count = usersMapper.selectPageAllCount();
		
		Pagination pagination = new Pagination(page, count, "/users/list/", row);
		System.out.println(pagination);
		model.addAttribute("pagination",pagination);
		model.addAttribute("userList",userList);
		model.addAttribute("row",row);
		model.addAttribute("count",count);
		model.addAttribute("page",page);	
		return "/users/list";
	}	
	
	//회원 로그인 페이지
	@GetMapping("/login.do")
		public void login() {};
		
	//회원 로그인
	@PostMapping("/login.do")
		public String login(
				@RequestParam(value="userid") String userId, 
				@RequestParam(value="userpw") String userPw,
				HttpSession session) {
			UsersDto users = null;
			try {
				users = usersMapper.selectIdPwOne(userId, userPw);
			}catch(Exception e) {e.printStackTrace();}
			
			if(users != null) {
				session.setAttribute("loginUsers", users);
				System.out.println("로그인 성공! " + users);
				return "redirect:/";
			}else {
				return "redirect:/users/login.do";				
			}
	}
	
	//회원 로그아웃
	@GetMapping("/logout.do")
	public String logout(HttpSession session) {
		session.removeAttribute("loginUsers");
		System.out.println("로그아웃 성공");
		return "redirect:/";
	}
	
	//회원가입
	@GetMapping("/signup.do")
	public void signup() {}
	@PostMapping("/signup.do")
	public String signup(UsersDto user) {
//		String rawPw = "";            // 인코딩 전 비밀번호
//        String encodePw = "";        // 인코딩 후 비밀번호
		int insert=0;
		System.out.println(user);
		try {
//			rawPw = user.getUserpw();            // 비밀번호 데이터 얻음
//		    encodePw = passwordEncoder.encode(rawPw);        // 비밀번호 인코딩
//		    user.setUserpw(encodePw);               // 인코딩된 비밀번호 user객체에 다시 저장
			insert=usersMapper.insertOne(user); // 회원가입 쿼리실행
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert>0) {
			return "redirect:/users/list/1";
		}else {
			return "redirect:/users/signup.do";
		}
	}
	
	//회원 상세 페이지
	@GetMapping("/detail/{userId}")
	public String detail(@PathVariable String userId, Model model) {
		UsersDto user = usersMapper.selectId(userId);
		model.addAttribute("user", user);
		System.out.println("users : " + user);
		return "users/detail";
	} 
	
	//회원 수정 페이지
	@GetMapping("/update/{userId}")
	public String update(
			@PathVariable String userId,
			Model model,
			HttpSession session
			) {
		UsersDto user = usersMapper.selectId(userId); 
		Object loginUsers_obj = session.getAttribute("loginUsers");
		if(loginUsers_obj != null ) {
			model.addAttribute(user);
			return "/users/update";	
		}else {
			return "redirect:/users/login.do";			
		}	
	}
	
	//회원 정보 수정
	@PostMapping("/update.do")
	public String update(UsersDto user) {
		int update=0;
		try {
			update=usersMapper.updateOne(user);
			System.out.println(user);
			System.out.println(update);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(update>0) {
			System.out.println("수정 성공 : "+ user);
			return "redirect:/users/list/1";
		}else {
			return "redirect:/users/detail/"+user.getUserid();
		}
	}
	
	//회원가입 중복 체크 (id)
	@GetMapping("/idCheck/{userId}")
	//ResponseBody가 들어가야 ajax가 작동한다
	@ResponseBody public IdCheck idCheck(@PathVariable String userId) {
		IdCheck idCheck = new IdCheck();
		UsersDto user=usersMapper.selectId(userId);
		if(user != null) { // 중복된 아이디가 있다
			idCheck.idCheck=true;
			idCheck.user=user;
		}
		return idCheck;
	}
	
	//회원가입 중복 체크 (email)
	@GetMapping("/emailCheck/{user_email}")
	public @ResponseBody EmailCheck emailCheck(@PathVariable String user_email) {
		EmailCheck emailCheck = new EmailCheck();
		UsersDto user = usersMapper.selectEmail(user_email);
		if(user != null) { // 중복된 이메일이 있다
			emailCheck.emailCheck = true;
			emailCheck.user = user;
		}
		return emailCheck;	
	}
	
	//회원가입 중복 체크 (phone)
	@GetMapping("/phoneCheck/{user_phone}")
	public @ResponseBody PhoneCheck phoneCheck(@PathVariable String user_phone) {
		PhoneCheck phoneCheck = new PhoneCheck();
		UsersDto user = usersMapper.selectPhone(user_phone);
		if(user != null) { // 중복된 전화번호가 있다
			phoneCheck.phoneCheck = true;
			phoneCheck.user = user;
		}
		return phoneCheck;	
	}
	
	//회원 삭제
	@GetMapping("/delete/{userId}")
	public String delete(@PathVariable String userId) {
		int delete=0;
		delete=usersMapper.deleteOne(userId);
		if(delete>0) {
			return "redirect:/users/list/1";
		}else {
			return "redirect:/users/detail/"+userId;
		}
	} 
	
	@GetMapping("/search/{page}")
	public String searchProduct(
			@RequestParam(value = "type") String type,
			@RequestParam(value = "keyword") String keyword,
			@PathVariable int page, Criteria cri, Model model) {
		int row = 10;
		int startRow = (page - 1) * row;
		cri.setAmount(row);
		cri.setSkip(startRow);
		List<UsersDto> list= usersMapper.searchUsers(cri);
		int count = usersMapper.usersGetTotal(cri);
		  if(!list.isEmpty()) { model.addAttribute("list",list);
		  }else { model.addAttribute("listCheck","empty"); return "/users/search"; }
		Pagination pagination = new Pagination(page, count, "/users/search/", row);
		System.out.println(pagination);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", list);
		System.out.println("list : "+list);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/users/search";
	}
	
	//푸터 연결용
	@GetMapping("/agreement")
	public void agreement() {};
	@GetMapping("/privacy")
	public void privacy() {};
	@GetMapping("/emailRejection")
	public void emailRejection() {};	
}