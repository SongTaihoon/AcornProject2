package com.project.mainPage.controller;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.Reply;
import com.project.mainPage.dto.ReplyPrefer;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.ReplyMapper;
import com.project.mainPage.mapper.ReplyPreferMapper;
@Controller
@RequestMapping("/reply")
public class ReplyController {
	@Autowired
	ReplyMapper replyMapper;
<<<<<<< HEAD
	
=======

>>>>>>> 45d821cd56c005338a34db006d19f19835443a91
//	application.properties의 설정 경로 받아 오기
	@Value("${spring.servlet.multipart.location}") // 파일이 임시 저장되는 경로 + 실제로 저장할 경로
	String savePath;
	
	@Autowired
	private ReplyPreferMapper replyPreferMapper;
	
//	댓글 리스트 페이지
	@RequestMapping("/list/{boardNo}/{page}")
	public String list(
			@PathVariable int boardNo,
			@PathVariable int page,
			@SessionAttribute(required = false) UserDto loginUser,
			Model model) {
		int row = 5;
		int startRow = (page - 1) * row;
		String url = "/reply/list/" + boardNo;
		List<Reply> replys = null;
		String loginUserId = (loginUser != null) ? loginUser.getUser_id() : null;
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
		return "/board/replyDetail";
	}
	
//	댓글 등록
	@PostMapping("/insert.do")
	public String insert(
			Reply reply, 
			@SessionAttribute(required = false) UserDto loginUser, 
			HttpSession session,
			MultipartFile imgFile) {
//		MultipartFile 로 받아온 파일은 임시로 저장된 파일
//		transferTo : 임시로 저장된 파일을 실제로 저장하는 함수
//		Paths.get(경로 + / + 파일 이름) : 임시 파일을 실제로 저장할 경로를 지정
		if((loginUser != null && loginUser.getUser_id().equals(reply.getUser().getUser_id())) || (loginUser.getAdminCk() == 1)) {
			int insert = 0;
			try {
				if(imgFile != null && !imgFile.isEmpty()) {
					String [] types = imgFile.getContentType().split("/"); // {"image", "jpeg"}
					if(types[0].equals("image")) {
						// 새로운 이미지 등록 
						String newFileName = "reply_" + System.nanoTime() + "." + types[1];
						Path path = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
						
						reply.setImg_path(newFileName); // DB에 이미지 저장
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
	
//	댓글 수정 (= 삭제 + 등록)
	@PostMapping("/update.do")
	public String update(
			Reply reply, 
			@SessionAttribute(required = false) UserDto loginUser, 
			HttpSession session,
			MultipartFile imgFile) {
		if((loginUser != null && loginUser.getUser_id().equals(reply.getUser().getUser_id())) || (loginUser.getAdminCk() == 1)) {
			int update = 0;
			try {
				if(imgFile != null && !imgFile.isEmpty()) { // 기존의 img_path가 있으면 삭제하고 새로 등록
					String [] types = imgFile.getContentType().split("/");
					if(types[0].equals("image")) {
						if(reply.getImg_path() != null) {
							// 등록되어 있는 이미지 삭제
							File file = new File(savePath + "/" + reply.getImg_path()); // 기존의 이미지 불러오기
							boolean delete = file.delete();
							System.out.println("기존 이미지 삭제 : " + delete);
						}	
						// 새로운 이미지 등록
						String newFileName = "reply_" + System.nanoTime() + "." + types[1];
						Path path = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
						
						reply.setImg_path(newFileName); // DB에 이미지 저장
					}
				} 
				System.out.println(imgFile);
				System.out.println(reply.getRemove_img_check());
				if(reply.getRemove_img_check() == 1) {
					if(reply.getImg_path() != null) {
						// 등록되어 있는 이미지 삭제
						System.out.println("이미지 체크하여 삭제");
						File file = new File(savePath + "/" + reply.getImg_path()); // 기존의 이미지 불러오기
						boolean delete = file.delete();
						System.out.println("기존 이미지 삭제 : " + delete);
						reply.setImg_path(null);
					}	
				}
				update = replyMapper.updateOne(reply);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(update > 0) {
				System.out.println("댓글 수정 성공! : " + update);
				return "redirect:/board/detail/" + reply.getBoard_no();	
			} else {
				System.out.println("댓글 수정 실패! : " + update);
				return "redirect:/board/detail/" + reply.getBoard_no();	
			}
		} else {
			return "redirect:/user/login.do";  
		}
	}
	
//	댓글 삭제
	@GetMapping("/delete/{reply_no}")
	public String delete(
			@PathVariable int reply_no,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session) {
		Reply reply = replyMapper.selectOne(reply_no);
		if((loginUser != null && loginUser.getUser_id().equals(reply.getUser().getUser_id())) || (loginUser.getAdminCk() == 1)) {
			int delete = 0;
			try {
				if(reply.getImg_path() != null) {
					// 등록되어 있는 이미지 삭제
					File file = new File(savePath + "/" + reply.getImg_path()); // 기존의 이미지 불러오기
					boolean del = file.delete();
					System.out.println("댓글 이미지 삭제 : " + del);
				}
				delete = replyMapper.deleteOne(reply_no);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				System.out.println("댓글 삭제 성공! : " + delete);
				return "redirect:/board/detail/" + reply.getBoard_no();	
			} else {
				System.out.println("댓글 삭제 실패! : " + delete);
				return "redirect:/board/detail/" + reply.getBoard_no();	
			}
		} else {
			return "redirect:/user/login.do";
		}
	}
	
//	댓글 좋아요 등록
	@PostMapping("/prefer/insert/{reply_no}/{prefer}")
	public String ReplyPreferInsert(
			@PathVariable int reply_no,
			@PathVariable boolean prefer,
			@SessionAttribute(required = true) UserDto loginUser,
			Model model) {
		System.out.println("post 호줄");
		Reply reply = null;
		int insert = 0;
		try {
			ReplyPrefer replyPrefer = new ReplyPrefer();
			replyPrefer.setReply_no(reply_no);
			replyPrefer.setUser_id(loginUser.getUser_id());
			replyPrefer.setPrefer(prefer);
			
			insert = replyPreferMapper.insertOne(replyPrefer);
			
			reply = replyMapper.selectOneJoinPrefers(reply_no);
			
			model.addAttribute("reply", reply);
			if(insert > 0) {
				System.out.println("댓글 좋아요/싫어요 등록 성공! : " + insert);
				reply.setPrefer_active(prefer);
			} else {
				System.out.println("댓글 좋아요/싫어요 등록 실패! : " + insert);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/replyDetail";
	}
	
//	댓글 좋아요 수정
	@PutMapping("/prefer/update/{reply_no}/{prefer}")
	public String ReplyPreferUpdate(
			@PathVariable int reply_no,
			@PathVariable boolean prefer,
			@SessionAttribute(required = true) UserDto loginUser,
			Model model) {
		System.out.println("put 호줄");
		Reply reply = null;
		int update = 0;
		try {
			ReplyPrefer replyPrefer = new ReplyPrefer();
			replyPrefer.setReply_no(reply_no);
			replyPrefer.setUser_id(loginUser.getUser_id());
			replyPrefer.setPrefer(prefer);
			
			update = replyPreferMapper.updateOne(replyPrefer);
			
			reply = replyMapper.selectOneJoinPrefers(reply_no);
			
			model.addAttribute("reply", reply);
			if(update > 0) {
				System.out.println("댓글 좋아요/싫어요 수정 성공! : " + update);
				reply.setPrefer_active(prefer);
			} else {
				System.out.println("댓글 좋아요/싫어요 수정 실패! : " + update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/replyDetail";
	}
	
//	댓글 좋아요 삭제
	@DeleteMapping("/prefer/delete/{reply_no}")
	public String ReplyPreferDelete(
			@PathVariable int reply_no,
			@SessionAttribute(required = true) UserDto loginUser,
			Model model) {
		System.out.println("delete 호줄");
		Reply reply = null;
		int delete = 0;
		try {
			ReplyPrefer replyPrefer = new ReplyPrefer();
			replyPrefer.setReply_no(reply_no);
			replyPrefer.setUser_id(loginUser.getUser_id());
			
			delete = replyPreferMapper.deleteOne(replyPrefer);
			
			reply = replyMapper.selectOneJoinPrefers(reply_no);
			
			model.addAttribute("reply", reply);
			if(delete > 0) {
				System.out.println("댓글 좋아요/싫어요 삭제 성공! : " + delete);
			} else {
				System.out.println("댓글 좋아요/싫어요 삭제 실패! : " + delete);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/replyDetail";
	}
}