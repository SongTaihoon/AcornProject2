package com.project.mainPage.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.Tour;
import com.project.mainPage.dto.TourImg;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.TourImgMapper;
import com.project.mainPage.mapper.TourMapper;
import com.project.mainPage.service.TourService;

import com.project.mainPage.dto.Acco;
import com.project.mainPage.dto.AccoImg;
import com.project.mainPage.dto.Rest;
import com.project.mainPage.mapper.AccoImgMapper;
import com.project.mainPage.mapper.AccoMapper;
import com.project.mainPage.mapper.RestMapper;
import com.project.mainPage.service.AccoService;
import com.project.mainPage.service.RestService;



@Controller
@RequestMapping("/top")
public class TopController {

	@Autowired
	private TourMapper tourMapper;
	@Autowired
	private TourService tourService;
	@Autowired
	private RestMapper restMapper;
	@Autowired
	private RestService restService;
	@Autowired
	private AccoMapper accoMapper;
	@Autowired
	private AccoService accoService;
	@Autowired
	private TourImgMapper tourImgMapper;
	@Autowired
	private AccoImgMapper accoImgMapper;
	
	@Value("${spring.servlet.multipart.location}") // 파일이 임시 저장되는 경로 + 파일을 저장할 경로
	private String savePath;
	

	// Tour > tour_img 의 수를 5개로 제한 
	private final static int TOUR_IMG_LIMIT = 5; 
	private final static int ACCO_IMG_LIMIT = 5; 
	
	
	// 관광지 TOP 10 LIST
	@GetMapping("/tour/list/{page}")
	public String tour(
			@PathVariable int page,
			Model model){
		int row = 10;
		int startRow = (page - 1) * row;
		List<Tour> tourList = tourMapper.selectListAll(startRow, row);
		int count = tourMapper.selectPageAllCount();
		Pagination pagination = new Pagination(page, count, "/top/tour/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("tourList",tourList);	
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "/top/tour/list";
	}
	
	// 관광지 TOP Detail 
	@GetMapping("/tour/detail/{tourRank}")
	public String detail(
			@PathVariable int tourRank,
			Model model
			) {
		Tour tour = null; 
		try {
			tour = tourService.tourUpdateView(tourRank);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("tour : " + tour);
		if(tour != null) {
			model.addAttribute(tour);
			return "/top/tour/detail";
		}else {
			return "redirect:/top/tour/list/1";	
		}
	}
	
	// 관광지 등록 페이지 (admin 관리자 등록하도록 설정)
	@GetMapping("/tour/insert.do")
	public String insert(
			@SessionAttribute(required = false) UserDto loginUser
			) {
		if((loginUser).getAdminCk() == 1) {
			return "/top/tour/insert";
		}else {
			return "redirect:/user/login.do";
		}
	}
	
//	관광지 등록
	@PostMapping("/tour/insert.do")
	public String insert(
				Tour  tour,
				@RequestParam(name = "imgFile", required = false) MultipartFile [] imgFiles,
				@SessionAttribute(required = false) UserDto loginUser,
				HttpSession session) {
		int insert = 0;
		try {
			//이미지 저장 및 처리
			if(imgFiles != null) {
				List<TourImg> tourImgs = new ArrayList<TourImg>();
				// imgFiles가 null이면 여기서 오류 발생!! 
				for(MultipartFile imgFile : imgFiles) {		
					String type = imgFile.getContentType();
					if(type.split("/")[0].equals("image")) {
						// 새로운 이미지 등록 
						String newFileName = "tour_" + System.nanoTime() + "." + type.split("/")[1]; // {"image", "jpeg"}
						Path newFilePath = Paths.get(savePath + "/" + newFileName);
						imgFile.transferTo(newFilePath); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
						
						TourImg tourImg = new TourImg();
						tourImg.setImg_path(newFileName); 
						tourImgs.add(tourImg);
					}
				}
				if(tourImgs.size() > 0) {
					tour.setTourImgs(tourImgs);
				}
			}
			insert = tourService.registTour(tour); // DB에 관광지 등록
			System.out.println(tour);
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(insert > 0) {
			System.out.println("관광지 등록 성공! : " + insert);
			return "redirect:/top/tour/list/1";
		}else {
			System.out.println("관광지 등록 실패! : " + insert);
			return "redirect:/top/tour/insert.do";
		}
	}
	
	// 관광지 수정 페이지 
	@GetMapping("/tour/update/{tourRank}")
	public String update(
			@PathVariable int tourRank, 
			Model model, 
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			HttpSession session) {
		Tour tour = null;
		tour = tourMapper.selectDetailOne(tourRank);
		if(loginUser != null || loginUser.getAdminCk() == 1) {
			model.addAttribute("tour", tour);
			System.out.println("getMappertour : "+tour);
			return "/top/tour/update";			
		} else {
			return "redirect:/user/login.do";
		}
	}
	
	@PostMapping("/tour/update.do")
	public String update(
			Tour tour,
			Model model,
			@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
			@RequestParam(name="tourImgNo",required = false ) int [] tourImgNos,
			@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
			HttpSession session
			) {
		int update = 0; 
		if((loginUser).getAdminCk() == 1) {
			try {
				int tourImgCount = tourImgMapper.selectCountTourRank(tour.getTour_rank());
				int insertTourImgLength = TOUR_IMG_LIMIT - tourImgCount + ((tourImgNos != null) ? tourImgNos.length : 0);
				// 이미지 저장 
				if(imgFiles != null && insertTourImgLength > 0) {
					List<TourImg> tourImgs = new ArrayList<TourImg>();
					for(MultipartFile imgFile : imgFiles) { 
						String[] types = imgFile.getContentType().split("/");
						if(types[0].equals("image")) {
							// 새로운 이미지 등록 
							String newFileName = "tour_" + System.nanoTime() + "." + types[1];
							Path path = Paths.get(savePath + "/" + newFileName);
							imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
							
							TourImg tourImg = new TourImg();
							tourImg.setTour_rank(tour.getTour_rank()); 
							tourImg.setImg_path(newFileName);
							tourImgs.add(tourImg);
							
							if(-- insertTourImgLength == 0) break; // 이미지 수가 5개면 반목문 종료 
						}
					}
					if(tourImgs.size() > 0) {
						tour.setTourImgs(tourImgs);
					}
				}
				update = tourService.updateTourRemoveTourImg(tour, tourImgNos); // DB에서 후기 수정

				System.out.println("update : "+update);
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/top/tour/update/" + tour.getTour_rank();
			}
			if(update > 0) {
				System.out.println("관광지 수정 성공! : " + update);
				return "redirect:/top/tour/detail/" + tour.getTour_rank();
			}else {
				System.out.println("관광지 수정 실패! : " + update);
				return "redirect:/top/tour/update/" + tour.getTour_rank();
			}	
		}else{ 
			return "redirect:/user/login.do";
		}  
		
	}
	
	// 관광지 정보 삭제 
	@SuppressWarnings("null")
	@GetMapping("/tour/delete/{tourRank}/{userId}")
	public String delete(
			@PathVariable int tourRank,
			@PathVariable String userId,
			@SessionAttribute(name ="loginUser",required = false) UserDto loginUser,
			HttpSession session
			) {
		System.out.println("loginUser : "+loginUser);
		String msg = "";
		if(loginUser != null || loginUser.getAdminCk() == 1) {
			int delete=0;
			try {
				delete = tourService.removeTour(tourRank);
			} catch(Exception e) {e.printStackTrace();}
			if(delete > 0) {
				msg="관광지 정보 삭제 성공";
				session.setAttribute("msg", msg);
				System.out.println(msg);
				return "redirect:/top/tour/list/1";
			}else {
				msg="관광지 정보 삭제 실패";
				session.setAttribute("msg", msg);
				System.out.println(msg);
				return "redirect:/top/tour/update/" + tourRank;			
			}	
		}else {
			msg="로그인 하셔야 이용가능합니다.";
			session.setAttribute("msg", msg);
			System.out.println(msg);
			return "redirect:/user/login.do";
		}
		
	}
	
	@GetMapping("/rest/{page}")
	public String rest(
			@PathVariable int page,
			Model model){
		int row = 10;
		int startRow = (page - 1) * row;
		List<Rest> restList = restMapper.selectListAll(startRow, row);
		int count = restMapper.selectPageAllCount();
		Pagination pagination = new Pagination(page, count, "/top/rest/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("restList",restList);	
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "top/rest";
	}
	
	// 관광지 TOP Detail 
	@GetMapping("/detail/{restRank}")
	public String detailrest(
			@PathVariable int restRank,
			Model model
			) {
		Rest rest = null; 
		try {
			rest = restService.restUpdateView(restRank);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("rest" + rest);
		if(rest != null) {
			model.addAttribute(rest);
			return "/top/detail";
		}else {
			return "redirect:/top/rest/1";	
		}
		
		
	}
	
	@GetMapping("/acco/list/{page}")
	public String acco(
			@PathVariable int page,
			Model model){
		int row = 10;
		int startRow = (page - 1) * row;
		List<Acco> accoList = accoMapper.selectListAll(startRow, row);
		int count = accoMapper.selectPageAllCount();
		Pagination pagination = new Pagination(page, count, "/top/acco/list/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("accoList",accoList);	
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "top/acco/list";
	}
	
	@GetMapping("/acco/detail/{accoRank}")
	public String accoDetail(
			@PathVariable int accoRank,
			Model model
			) {
		Acco acco = null; 
		try {
			acco = accoService.accoUpdateView(accoRank);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("acco" + acco);
		if(acco != null) {
			model.addAttribute(acco);
			return "/top/acco/detail";
		}else {
			return "redirect:/top/acco/list/1";	
		}
	}
	
	// 숙박 등록 페이지 (admin 관리자 등록하도록 설정)
		@GetMapping("/acco/insert.do")
		public String accoInsert(
				@SessionAttribute(required = false) UserDto loginUser
				) {
			if((loginUser).getAdminCk() == 1) {
				return "/top/acco/insert";
			}else {
				return "redirect:/user/login.do";
			}
		}
		
		// 숙박 등록
		@PostMapping("/acco/insert.do")
		public String accoInsert(
					Acco  acco,
					@RequestParam(name = "imgFile", required = false) MultipartFile [] imgFiles,
					@SessionAttribute(required = false) UserDto loginUser,
					HttpSession session) {
			int insert = 0;
			try {
				//이미지 저장 및 처리
				if(imgFiles != null) {
					List<AccoImg> accoImgs = new ArrayList<AccoImg>();
					// imgFiles가 null이면 여기서 오류 발생!! 
					for(MultipartFile imgFile : imgFiles) {		
						String type = imgFile.getContentType();
						if(type.split("/")[0].equals("image")) {
							// 새로운 이미지 등록 
							String newFileName = "acco_" + System.nanoTime() + "." + type.split("/")[1]; // {"image", "jpeg"}
							Path newFilePath = Paths.get(savePath + "/" + newFileName);
							imgFile.transferTo(newFilePath); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
							
							AccoImg accoImg = new AccoImg();
							accoImg.setImg_path(newFileName); 
							accoImgs.add(accoImg);
						}
					}
					if(accoImgs.size() > 0) {
						acco.setAccoImgs(accoImgs);
					}
				}
				insert = accoService.registAcco(acco); // DB에 등록
				System.out.println(acco);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(insert > 0) {
				System.out.println("숙박 등록 성공! : " + insert);
				return "redirect:/top/acco/list/1";
			}else {
				System.out.println("숙박 등록 실패! : " + insert);
				return "redirect:/top/acco/insert.do";
			}
		}
	
		
		// 숙박 수정 페이지 
		@GetMapping("/acco/update/{accoRank}")
		public String accoUpdate(
				@PathVariable int accoRank, 
				Model model, 
				@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
				HttpSession session) {
			Acco acco = null;
			acco = accoMapper.selectDetailOne(accoRank);
			if(loginUser != null || loginUser.getAdminCk() == 1) {
				model.addAttribute("acco", acco);
				System.out.println("getMapperacco : "+acco);
				return "/top/acco/update";			
			} else {
				return "redirect:/user/login.do";
			}
		}
		
		@PostMapping("/acco/update.do")
		public String accoUpdate(
				Acco acco,
				Model model,
				@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
				@RequestParam(name="accoImgNo",required = false ) int [] accoImgNos,
				@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
				HttpSession session
				) {
			int update = 0; 
			if((loginUser).getAdminCk() == 1) {
				try {
					int accoImgCount = accoImgMapper.selectCountAccoRank(acco.getAcco_rank());
					int insertAccoImgLength = ACCO_IMG_LIMIT - accoImgCount + ((accoImgNos != null) ? accoImgNos.length : 0);
					// 이미지 저장 
					if(imgFiles != null && insertAccoImgLength > 0) {
						List<AccoImg> accoImgs = new ArrayList<AccoImg>();
						for(MultipartFile imgFile : imgFiles) { 
							String[] types = imgFile.getContentType().split("/");
							if(types[0].equals("image")) {
								// 새로운 이미지 등록 
								String newFileName = "acco_" + System.nanoTime() + "." + types[1];
								Path path = Paths.get(savePath + "/" + newFileName);
								imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
								
								AccoImg accoImg = new AccoImg();
								accoImg.setAcco_rank(acco.getAcco_rank());
								accoImg.setImg_path(newFileName);
								accoImgs.add(accoImg);
								
								if(-- insertAccoImgLength == 0) break; // 이미지 수가 5개면 반목문 종료 
							}
						}
						if(accoImgs.size() > 0) {
							acco.setAccoImgs(accoImgs);
						}
					}
					update = accoService.updateAccoRemoveAccoImg(acco, accoImgNos); // DB에서 후기 수정

					System.out.println("update : "+update);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/top/acco/update/" + acco.getAcco_rank();
				}
				if(update > 0) {
					System.out.println("숙박 수정 성공! : " + update);
					return "redirect:/top/acco/detail/" + acco.getAcco_rank();
				}else {
					System.out.println("숙박 수정 실패! : " + update);
					return "redirect:/top/acco/update/" + acco.getAcco_rank();
				}	
			}else{ 
				return "redirect:/user/login.do";
			}  
			
		}	
		
		// 숙박 정보 삭제 
		@SuppressWarnings("null")
		@GetMapping("/acco/delete/{accoRank}/{userId}")
		public String accoDelete(
				@PathVariable int accoRank,
				@PathVariable String userId,
				@SessionAttribute(name ="loginUser",required = false) UserDto loginUser,
				HttpSession session
				) {
			System.out.println("loginUser : "+loginUser);
			String msg = "";
			if(loginUser != null || loginUser.getAdminCk() == 1) {
				int delete=0;
				try {
					delete = accoService.removeAcco(accoRank);
				} catch(Exception e) {e.printStackTrace();}
				if(delete > 0) {
					msg="숙박업소 정보 삭제 성공";
					session.setAttribute("msg", msg);
					System.out.println(msg);
					return "redirect:/top/acco/list/1";
				}else {
					msg="숙박업소 정보 삭제 실패";
					session.setAttribute("msg", msg);
					System.out.println(msg);
					return "redirect:/top/acco/update/" + accoRank;			
				}	
			}else {
				msg="로그인 하셔야 이용가능합니다.";
				session.setAttribute("msg", msg);
				System.out.println(msg);
				return "redirect:/user/login.do";
			}
			
		}
}