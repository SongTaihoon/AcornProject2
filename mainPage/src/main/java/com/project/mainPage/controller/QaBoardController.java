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
import org.springframework.web.bind.annotation.SessionAttribute;
import com.project.mainPage.dto.Criteria;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.QaBoard;
import com.project.mainPage.dto.QaReply;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.QaBoardMapper;
import com.project.mainPage.mapper.QaReplyMapper;
import com.project.mainPage.service.QaBoardService;
@Controller
@RequestMapping("/qaboard")
public class QaBoardController {
	@Autowired
	private QaBoardMapper qaBoardMapper;
	
	@Autowired
	private QaReplyMapper qaReplyMapper;
	
	@Autowired
	private QaBoardService qaBoardService;
	
//	고객 문의 리스트 페이지
	@GetMapping("/list/{page}")
	public String list(
			@PathVariable int page, 
			Model model) {
		int row = 10;
		int startRow = (page - 1) * row;
		List<QaBoard> list = qaBoardMapper.selectPageAll(startRow, row);
		int count = qaBoardMapper.selectPageAllCount();
		
		Pagination pagination = new Pagination(page, count, "/qaboard/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", list);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/qaboard/list";
	}
	
//	고객 문의 리스트 검색 페이지
	@GetMapping("/search/{page}")
	public String searchProduct(
			@PathVariable int page,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "keyword") String keyword,
			Criteria cri, 
			Model model) {
		int row = 10;
		int startRow = (page - 1) * row;
		cri.setAmount(row);
		cri.setSkip(startRow);
		List<QaBoard> list = qaBoardMapper.searchQaBoard(cri);
		int count = qaBoardMapper.QaBoardGetTotal(cri);
		  if(!list.isEmpty()) { 
			  model.addAttribute("list", list);
		  } else { 
			  model.addAttribute("listCheck","empty"); 
			  return "/qaboard/search"; 
		  }
		Pagination pagination = new Pagination(page, count, "/qaboard/search/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", list);
		System.out.println("list : " + list);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/qaboard/search";
	}
	
//	고객 문의 상세 페이지
	@GetMapping("/detail/{qaBoardno}")
	public String detail(
			@PathVariable int qaBoardno, 
			Model model,
			@SessionAttribute(required = false) UserDto loginUser) {
		QaBoard qaBoard = null;
		try {
			qaBoard = qaBoardService.qaBoardUpdateView(qaBoardno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("detail_qaBoard" + qaBoard);
		model.addAttribute("qaBoard", qaBoard);
		return "/qaboard/detail";
	}
	
//	고객 문의 등록 페이지
	@GetMapping("/insert.do")
	public String insert(
			Model model,
			@SessionAttribute(required = false) UserDto loginUser) {
		return "/qaboard/insert";
	}
	
//	고객 문의 등록
	@PostMapping("/insert.do")
	public String insert(QaBoard qaBoard) {
		int insert = 0;
		try {
			insert = qaBoardMapper.insertOne(qaBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("qaBoard 등록 성공! : " + insert);
			return "redirect:/qaboard/list/1";
		}else {
			System.out.println("qaBoard 등록 실패! : " + insert);
			return "redirect:/qaboard/insert.do";
		}
	}
	
//	고객 문의 삭제
	@GetMapping("/delete/{qaBoardNo}")
	public String delete(
			@PathVariable int qaBoardNo,
			HttpSession session,
			@SessionAttribute(required = false) UserDto loginUser) {
		int delete = 0;
		QaBoard qaBoard = qaBoardMapper.selectOne(qaBoardNo);
		try {
			if(loginUser == null) { // 로그인이 안 되어 있는 경우
				System.out.println("로그인하세요.");
				return "redirect:/user/login.do";			
			} else if(qaBoard.getUser().getUser_id().equals(loginUser.getUser_id()) || (loginUser.getAdminCk() == 1)) { // 로그인된 일반 회원이 본인이 작성한 고객 문의 글을 삭제 / 관리자는 모든 글 삭제 가능
				delete = qaBoardMapper.deleteOne(qaBoardNo);
				return "/qaboard/modify";	
			} else { // 로그인된 일반 회원이 다른 회원이 작성한 고객 문의 글을 삭제할 수 없음
				System.out.println("다른 회원이 작성한 고객 문의 글을 삭제할 수 없습니다.");
				return "redirect:/";	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(delete > 0) {
			System.out.println("qaBoard 삭제 성공! : " + delete);
			return "redirect:/qaboard/list/1";
		}else {
			System.out.println("qaBoard 삭제 실패! : " + delete);
			return "redirect:/qaboard/detail/"+qaBoardNo;
		}
	} 
	
//	고객 문의 수정 페이지
	@GetMapping("/update/{qaBoardno}")
	public String update(
			@PathVariable int qaBoardno, 
			Model model, 
			HttpSession session,
			@SessionAttribute(required = false) UserDto loginUser) {
		QaBoard qaBoard = null;
		qaBoard = qaBoardMapper.selectOne(qaBoardno);
		if(loginUser == null) { // 로그인이 안 되어 있는 경우
			System.out.println("로그인하세요.");
			return "redirect:/user/login.do";			
		} else if(qaBoard.getUser().getUser_id().equals(loginUser.getUser_id()) || (loginUser.getAdminCk() == 1)) { // 로그인된 일반 회원이 본인이 작성한 고객 문의 수정 페이지로 이동 / 관리자는 모든 글 조회 가능
			System.out.println("고객 문의 수정 페이지로 이동 성공!");
			model.addAttribute("qaBoard : ", qaBoard);
			System.out.println(qaBoard);
			return "/qaboard/modify";	
		} else { // 로그인된 일반 회원이 다른 회원이 작성한 고객 문의 수정 페이지로 이동할 수 없음
			System.out.println("다른 회원이 작성한 고객 문의 글을 수정할 수 없습니다.");
			return "redirect:/";	
		}
	}
	
//	고객 문의 수정
	@PostMapping("/update.do")
	public String update(QaBoard qaBoard) {
		int update = 0;
		try {
			update = qaBoardMapper.updateOne(qaBoard);
			System.out.println("postUpdate_qaBoard : "+qaBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(update > 0) {
			System.out.println("qaBoard 수정 성공! : " + update);
			return "redirect:/qaboard/detail/" + qaBoard.getQaBoardNo();
		} else {
			System.out.println("qaBoard 수정 실패! : " + update);
			return "redirect:/qaboard/update/" + qaBoard.getQaBoardNo();
		}
	}
	
//	고객 문의 답변 등록(관리자)
	@PostMapping("/replyInsert.do")
	public String replyInsert(
			QaReply qaReply, 
			QaBoard qaBoard) {
		int insert = 0;
		int update = 0; // 답변 여부 1로 바꾸기
		int page = qaBoard.getQaBoardNo();
		try {
			insert = qaReplyMapper.insertOne(qaReply);
			update = qaBoardMapper.answerOne(qaBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("qaBoard 답변 등록 성공! : " + insert);
			System.out.println("qaBoard 답변 여부 1로 바꾸기 성공! : " + update);
			return "redirect:/qaboard/detail/" + page;
		}else {
			System.out.println("qaBoard 답변 등록 실패! : " + insert);
			System.out.println("qaBoard 답변 여부 1로 바꾸기 성공! : " + update);
			return "redirect:/qaboard/detail/" + page;
		}
	}
	
//	고객 문의 답변 수정 페이지(관리자)
	@GetMapping("/replyUpdate/{qaBoardNo}")
	public String replyUpdate(
			@PathVariable int qaBoardNo, 
			Model model, 
			HttpSession session) {
		Object loginUser_obj = session.getAttribute("loginUser");
		QaBoard qaBoard = null;
		try {
			qaBoard = qaBoardMapper.selectOne(qaBoardNo);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		if((((UserDto)loginUser_obj).getAdminCk() == 1)) { 
			model.addAttribute("qaBoard", qaBoard);
			return "/qaboard/modifyReply";			
		} else { 
			return "redirect:/user/login.do";
		}
	}
	
//	고객 문의 답변 수정(관리자)
	@PostMapping("/replyUpdate.do")
	public String replyUpdate(QaReply qaReply) {
		int update = 0;
		try {
			update = qaReplyMapper.updateOne(qaReply);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		if(update > 0) {
			System.out.println("qaBoard 답변 수정 성공! : " + update);
			return "redirect:/qaboard/detail/" + qaReply.getQaBoardNo();
		} else {
			System.out.println("qaBoard 답변 수정 실패! : " + update);
			return "redirect:/qaboard/replyUpdate/" + qaReply.getQaBoardNo();
		}
	}
	
//	고객 문의 답변 삭제(관리자)
	@GetMapping("/replyDelete/{qaBoardNo}")
	public String replyDelete(
			@PathVariable int qaBoardNo, 
			QaBoard qaBoard) {
		int delete = 0;
		int update = 0; // 답변 여부 다시 0으로 바꿔서 새로 등록할 수 있게 하기
		try {
			delete = qaReplyMapper.deleteOne(qaBoardNo);
			update = qaBoardMapper.answerOne(qaBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(delete > 0) {
			System.out.println("qaBoard 답변 삭제 성공! : " + delete);
			System.out.println("qaBoard 답변 여부 다시 0으로 바꾸기 성공! : " + update);
			return "redirect:/qaboard/detail/" + qaBoardNo;
		} else {
			System.out.println("qaBoard 답변 삭제 실패! : " + delete);
			System.out.println("qaBoard 답변 여부 다시 0으로 바꾸기 실패! : " + update);
			return "redirect:/qaboard/replyUpdate/" + qaBoardNo;
		}
	}
}