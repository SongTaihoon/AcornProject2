package com.project.mainPage.controller;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.project.mainPage.dto.BoardImg;
import com.project.mainPage.service.BoardService;
import com.project.mainPage.dto.Board;
import com.project.mainPage.dto.BoardPrefer;
import com.project.mainPage.dto.Criteria;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.Reply;
import com.project.mainPage.dto.ReplyPrefer;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.BoardImgMapper;
import com.project.mainPage.mapper.BoardMapper;
import com.project.mainPage.mapper.BoardPreferMapper;
import com.project.mainPage.mapper.ReplyMapper;
import com.project.mainPage.mapper.ReplyPreferMapper;
import com.project.mainPage.mapper.UserMapper;
@Controller
@RequestMapping("/board")
public class BoardController {	
	// board > board_img 의 수를 5개로 제한 
	private final static int BOARD_IMG_LIMIT = 5; 
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BoardImgMapper boardImgMapper;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BoardPreferMapper boardPreferMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private ReplyPreferMapper replyPreferMapper;
	
	@Value("${spring.servlet.multipart.location}") // 파일이 임시 저장되는 경로 + 파일을 저장할 경로
	private String savePath;
	
//	후기 리스트 페이지
	@GetMapping("/list/{page}")
	public String list(
			@PathVariable int page, 
			Model model, 
			@SessionAttribute(required = false) UserDto loginUser) {
		int row = 10;
		int startRow = (page - 1) * row;
		List<Board> boardList = boardMapper.selectPageAll(startRow, row);
		int count = boardMapper.selectPageAllCount();
		
		Pagination pagination = new Pagination(page, count, "/board/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("boardList", boardList);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/board/list";
	}
	
//	후기 검색 페이지
	@GetMapping("/search/{page}")
	public String searchProduct(
			@RequestParam(value = "type") String type,
			@RequestParam(value = "keyword") String keyword,
			@PathVariable int page, 
			Criteria cri, 
			Model model) {
		int row = 10;
		int startRow = (page - 1) * row;
		cri.setAmount(row);
		cri.setSkip(startRow);
		List<Board> list = boardMapper.searchBoard(cri);
		int count = boardMapper.boardGetTotal(cri);
		  if(!list.isEmpty()) { 
			model.addAttribute("list", list);
		  } else {
		    model.addAttribute("listCheck","empty"); 
			return "/board/search";
		  }
		Pagination pagination = new Pagination(page, count, "/board/search/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", list);
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/board/search";
	}	
	
//	후기 상세 페이지
	@GetMapping("/detail/{boardNo}")
	public String detail(
			@PathVariable int boardNo,
			Model model,
			@SessionAttribute(required = false) UserDto loginUser, 
			@RequestParam(defaultValue = "1") int replyPage,
			HttpServletRequest req,
			HttpServletResponse resp) {
		Board board = null;		
		BoardPrefer boardPrefer = null;  // 로그인이 안 되면 null
		
		//System.out.println(replyPage);
		//System.out.println(board);
		
		int row = 5;
		int startRow = (replyPage - 1) * row;
		
		String pagingUrl = "/reply/list/" + boardNo;
		Pagination pagination = null;
		String loginUsersId = null;
		try {
			if(loginUser != null) {
				loginUsersId = loginUser.getUser_id();
				board = boardService.boardUpdateView(boardNo);
				boardPrefer = boardPreferMapper.selectFindUserIdAndBoardNo(loginUser.getUser_id(), boardNo);
				System.out.println(boardPrefer);
				if(boardPrefer != null && boardPrefer.getUser_id().equals(loginUser.getUser_id())) {
					if(boardPrefer.isPrefer()) {
						board.setPrefer_active(true);
					} else {
						board.setPrefer_active(false);
					}
				}
				for(Reply reply : board.getReplys()) {
					for (ReplyPrefer prefer : reply.getGood_prefers()) {
						if(prefer.getUser_id().equals(loginUser.getUser_id())) {
							reply.setPrefer_active(true);
						}
					}
					for (ReplyPrefer prefer : reply.getBad_prefers()) {
						if(prefer.getUser_id().equals(loginUser.getUser_id())) {
							reply.setPrefer_active(false);
						}
					}
				}
				int replySize = replyMapper.selectBoardNoCount(boardNo);
				if(replySize > 0) {
					pagination = new Pagination(replyPage, replySize, pagingUrl, row);
					List<Reply> replies = replyMapper.selectBoardNoPage(boardNo, startRow, row, loginUsersId);
					board.setReplys(replies);
					model.addAttribute("pagination", pagination);
				}
			} else {
				board = boardService.boardUpdateView(boardNo);
				int replySize = replyMapper.selectBoardNoCount(boardNo);
				if(replySize > 0) {
					pagination = new Pagination(replyPage, replySize, pagingUrl, row);
					List<Reply> replies = replyMapper.selectBoardNoPage(boardNo, startRow, row, loginUsersId);
					board.setReplys(replies);
					model.addAttribute("pagination", pagination);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(board != null) {
			model.addAttribute("boardPrefer", boardPrefer);
			model.addAttribute("board", board);
			System.out.println(board);
			return "/board/detail";			
		}else {
			return "redirect:/board/list/1";
		}
	}
	
//	후기 등록 페이지(로그인한 사람만)
	@GetMapping("/insert.do")
	public String insert(HttpSession session) {
		if(session.getAttribute("loginUser") != null) {
			return "/board/insert";
		}else {
			return "redirect:/user/login.do";
		}
	}
		
//	후기 등록
	@PostMapping("/insert.do")
	public String insert(
				Board  board,
				@RequestParam(name = "imgFile", required = false) MultipartFile [] imgFiles,
				@SessionAttribute(required = false) UserDto loginUser,
				HttpSession session) {
		int insert = 0;
		try {
			//이미지 저장 및 처리
			if(imgFiles != null) {
				List<BoardImg> boardImgs = new ArrayList<BoardImg>();
				// imgFiles가 null이면 여기서 오류 발생!! 
				for(MultipartFile imgFile : imgFiles) {		
					String type = imgFile.getContentType();
					if(type.split("/")[0].equals("image")) {
						// 새로운 이미지 등록 
						String newFileName = "board_" + System.nanoTime() + "." + type.split("/")[1]; // {"image", "jpeg"}
						Path newFilePath = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(newFilePath); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
						
						BoardImg boardImg = new BoardImg();
						boardImg.setImg_path(newFileName); 
						boardImgs.add(boardImg);
					}
				}
				if(boardImgs.size() > 0) {
					board.setBoardImgs(boardImgs);
				}
			}
			insert = boardService.registBoard(board); // DB에 후기 등록
			System.out.println(board);
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("후기 등록 성공! : " + insert);
			return "redirect:/board/list/1";
		}else {
			System.out.println("후기 등록 실패! : " + insert);
			return "redirect:/board/insert.do";
		}
	}
	
//	후기 삭제 
	@GetMapping("/delete/{boardNo}/{userId}")
	public String delete(
			@PathVariable int boardNo,
			@PathVariable String userId,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session) {
		if(loginUser != null && loginUser.getUser_id().equals(userId)) {
			int delete = 0;
			try {
				delete = boardService.removeBoard(boardNo); // DB에서 후기 삭제
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(delete > 0) {
				System.out.println("후기 삭제 성공! : " + delete);
				return "redirect:/board/list/1";
			}else {
				System.out.println("후기 삭제 성공! : " + delete);
				return "redirect:/board/update/" + boardNo;			
			}			
		}else {
			return "redirect:/user/login.do";
		}
	}
	
//	후기 수정 페이지
	@GetMapping("/update/{boardNo}")
	public String update(
			@PathVariable int boardNo, 
			Model model, 
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session) {
		Board board = null;
		board = boardMapper.selectDetailOneAll(boardNo);
		if((loginUser != null && loginUser.getUser_id().equals(board.getUser().getUser_id())) || ((loginUser).getAdminCk() == 1)) {
			model.addAttribute("board", board);
			return "/board/modify";			
		} else {
			return "redirect:/user/login.do";
		}
	}
	
//	후기 수정 
	@PostMapping("/update.do")
	public String update(
			Board board, 
			Model model,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			@RequestParam(name = "boardImgNo", required = false) int [] boardImgNos, // // required = false : 아무 것도 안 올 수 있는 경우
			@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
 			HttpSession session
			) { 
		int update = 0; 
		if((loginUser != null && loginUser.getUser_id().equals(board.getUser().getUser_id())) || ((loginUser).getAdminCk() == 1)) {
			try {
				int boardImgCount = boardImgMapper.selectCountBoardNo(board.getBoard_no());  // baordImg 등록된 개수 
				int insertBoardImgLength = BOARD_IMG_LIMIT - boardImgCount + ((boardImgNos != null) ? boardImgNos.length : 0); // 5 -  boardImgCount + 등록될 이미지 개수 
				// 이미지 저장 
				if(imgFiles != null && insertBoardImgLength > 0) {
					List<BoardImg> boardImgs = new ArrayList<BoardImg>();
					for(MultipartFile imgFile : imgFiles) { 
						String[] types = imgFile.getContentType().split("/");
						if(types[0].equals("image")) {
							// 새로운 이미지 등록 
							String newFileName = "board_" + System.nanoTime() + "." + types[1];
							Path path = Paths.get(savePath + "/" + newFileName);
							imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
							
							BoardImg boardImg = new BoardImg();
							boardImg.setBoard_no(board.getBoard_no()); 
							boardImg.setImg_path(newFileName);
							boardImgs.add(boardImg);
							
							if(-- insertBoardImgLength == 0) break; // 이미지 수가 5개면 반목문 종료 
						}
					}
					if(boardImgs.size() > 0) {
						board.setBoardImgs(boardImgs);
					}
				}
				update = boardService.modifyBoardRemoveBoardImg(board, boardImgNos); // DB에서 후기 수정
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(update > 0) {
				System.out.println("후기 수정 성공! : " + update);
				return "redirect:/board/detail/" + board.getBoard_no();
			}else {
				System.out.println("후기 수정 성공! : " + update);
				return "redirect:/board/update/" + board.getBoard_no();
			}				
		} else{ 
			return "redirect:/user/login.do";
		}     
	}
	
//	후기 좋아요 싫어요
	@GetMapping("/prefer/{boardNo}/{prefer}")
	public String preferModify(
			Model model,
			@PathVariable int boardNo,
			@PathVariable boolean prefer,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session) {
		int modify = 0;
		try {
			BoardPrefer boardPrefer = boardPreferMapper.selectFindUserIdAndBoardNo(loginUser.getUser_id(), boardNo);
			if(boardPrefer == null) { // 좋아요 싫어요를 한 번도 한 적이 없을 때
				boardPrefer = new BoardPrefer();
				boardPrefer.setBoard_no(boardNo);
				boardPrefer.setPrefer(prefer);
				boardPrefer.setUser_id(loginUser.getUser_id());
				modify = boardPreferMapper.insertOne(boardPrefer);
			}else if(prefer == boardPrefer.isPrefer()) { // 좋아요 싫어요를 한 번 더 눌러서 삭제할 때
				boardPrefer.setPrefer(prefer);
				modify = boardPreferMapper.deleteOne(boardPrefer.getBoard_prefer_no());
			}else if(prefer != boardPrefer.isPrefer()) { // 좋아요에서 싫어요로 바꿀 때 or 싫어요에서 좋아요로 바꿀 때
				boardPrefer.setPrefer(prefer);
				modify = boardPreferMapper.updateOne(boardPrefer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(modify > 0) {
			System.out.println("성공!");
		}else {
			System.out.println("실패!");
		}
		return "redirect:/board/detail/" + boardNo;
	}
}