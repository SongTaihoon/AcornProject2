package com.project.mainPage.controller;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.Reply;
import com.project.mainPage.dto.UsersDto;
import com.project.mainPage.mapper.ReplyMapper;
import com.project.mainPage.mapper.ReplyPreferMapper;
@Controller
@RequestMapping("/reply")
public class ReplyController {
	@Autowired
	ReplyMapper replyMapper;
//	application.properties의 설정 경로 받아 오기
	@Value("${spring.servlet.multipart.location}") // 파일이 임시 저장되는 경로 + 실제로 저장할 경로
	String savePath;
	
	@Autowired
	private ReplyPreferMapper replyPreferMapper;
	
	@RequestMapping("/list/{boardNo}/{page}")
	public String list(
			@PathVariable int boardNo,
			@PathVariable int page,
			@SessionAttribute(required = false) UsersDto loginUsers,
			Model model) {
		int row = 5;
		int startRow = (page - 1) * row;
		String url = "/reply/list/" + boardNo;
		List<Reply> replys = null;
		String loginUserId = (loginUsers != null) ? loginUsers.getUserid() : null;
		try {
			int rowCount = replyMapper.selectBoardNoCount(boardNo);
			Pagination pagination = new Pagination(page, rowCount, url, row);
			replys = replyMapper.selectBoardNoPage(boardNo, startRow, row, loginUserId);
			model.addAttribute("pagination", pagination);
			System.out.println(pagination);
			model.addAttribute("replys", replys);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return "/board/replyList";
	}
	
	@PostMapping("/insert.do")
	public String insert(
			Reply reply, 
			@SessionAttribute(required = false) UsersDto loginUsers, 
			HttpSession session,
			MultipartFile imgFile) {
//		MultipartFile 로 받아온 파일은 임시로 저장된 파일
//		transferTo : 임시로 저장된 파일을 실제로 저장하는 함수
//		Paths.get(경로+/+파일 이름) : 임시 파일을 실제로 저장할 경로를 지정
		if((loginUsers != null && loginUsers.getUserid().equals(reply.getUsers().getUserid())) || (loginUsers.getAdminCk() == 1)) {
			int insert = 0;
			try {
				if(imgFile != null && !imgFile.isEmpty()) {
					String [] types = imgFile.getContentType().split("/"); // {"image", "jpeg"}
					if(types[0].equals("image")) {
						String newFileName = "reply_" + System.nanoTime() + "." + types[1];
						Path path = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(path);
						
						reply.setImg_path(newFileName);
					}
				}
				insert = replyMapper.insertOne(reply);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(insert > 0) {
				System.out.println("댓글 등록 성공! : " + insert);
				return "redirect:/board/detail/" + reply.getBoard_no();
			} else {
				System.out.println("댓글 등록 실패! : " + insert);
				return "redirect:/board/detail/" + reply.getBoard_no();
			}	
		} else {
			return "redirect:/user/login.do"; 
		}
	}
}
