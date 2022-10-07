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
import com.project.mainPage.dto.Restaurant;
import com.project.mainPage.dto.RestaurantImg;
import com.project.mainPage.dto.Tour;
import com.project.mainPage.dto.TourImg;
import com.project.mainPage.dto.UserDto;
import com.project.mainPage.mapper.RestaurankMapper;
import com.project.mainPage.mapper.RestaurantImgMapper;
import com.project.mainPage.mapper.TourImgMapper;
import com.project.mainPage.mapper.TourMapper;
import com.project.mainPage.service.RestaurantService;
import com.project.mainPage.service.TourService;

@Controller
@RequestMapping("/top")
public class TopController {

	@Autowired
	private TourMapper tourMapper;
	@Autowired
	private TourService tourService;
	@Autowired
	private TourImgMapper tourImgMapper;
	
	@Value("${spring.servlet.multipart.location}") // 파일이 임시 저장되는 경로 + 파일을 저장할 경로
	private String savePath;
	
	@Autowired
	private RestaurankMapper restaurankMapper;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private RestaurantImgMapper restaurantImgMapper; 
	
	
	// Tour > tour_img 의 수를 5개로 제한 
	private final static int TOUR_IMG_LIMIT = 5; 
	
	// RESTAURANK > RESTAURANK_IMG 의 수를 5개로 제한 
	private final static int RESTAURANK_IMG_LIMIT = 5; 

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
	
	// 맛집 TOP 10 LIST
		@GetMapping("/rest/list/{page}")
		public String restlist(
				@PathVariable int page,
				Model model){
			int row = 20;
			int startRow = (page - 1) * row;
			List<Restaurant> restaurantsList = restaurankMapper.selectListAll(startRow, row);
			int count = restaurankMapper.selectPageAllCount();
			Pagination pagination = new Pagination(page, count, "/top/rest/list/", row);
			model.addAttribute("pagination", pagination);
			model.addAttribute("restaurantsList",restaurantsList);	
			model.addAttribute("row", row);
			model.addAttribute("count", count);
			model.addAttribute("page", page);
			return "/top/rest/list";
		}
	
		
		// 맛집 TOP Detail 
		@GetMapping("/rest/detail/{restRank}")
		public String restdetail(
				@PathVariable int restRank,
				Model model
				) {
			Restaurant restaurant = null; 
			try {
				restaurant = restaurantService.restUpdateView(restRank);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("restaurant : " + restaurant);
			if(restaurant != null) {
				model.addAttribute(restaurant);
				return "/top/rest/detail";
			}else {
				return "redirect:/top/rest/list/1";	
			}
		}
	
		@GetMapping("/rest/insert.do")
		public String restinsert(
				@SessionAttribute(required = false) UserDto loginUser
				) {
			if((loginUser).getAdminCk() == 1) {
				return "/top/rest/insert";
			}else {
				return "redirect:/user/login.do";
			}
		}
		
//		음식점 등록
		@PostMapping("/rest/insert.do")
		public String restinsert(
					Restaurant  restaurant,
					@RequestParam(name = "imgFile", required = false) MultipartFile [] imgFiles,
					@SessionAttribute(required = false) UserDto loginUser,
					HttpSession session) {
			int insert = 0;
			try {
				//이미지 저장 및 처리
				if(imgFiles != null) {
					List<RestaurantImg> restaurantImgs = new ArrayList<RestaurantImg>();
					// imgFiles가 null이면 여기서 오류 발생!! 
					for(MultipartFile imgFile : imgFiles) {		
						String type = imgFile.getContentType();
						if(type.split("/")[0].equals("image")) {
							// 새로운 이미지 등록 
							String newFileName = "rest_" + System.nanoTime() + "." + type.split("/")[1]; // {"image", "jpeg"}
							Path newFilePath = Paths.get(savePath + "/" + newFileName);
							imgFile.transferTo(newFilePath); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
							
							RestaurantImg restaurantImg = new RestaurantImg();
							restaurantImg.setImg_path(newFileName); 
							restaurantImgs.add(restaurantImg);
						}
					}
					if(restaurantImgs.size() > 0) {
						restaurant.setRestaurantImgs(restaurantImgs);
					}
				}
				insert = restaurantService.registRest(restaurant); // DB에 관광지 등록
				System.out.println(restaurant);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(insert > 0) {
				System.out.println("음식점 등록 성공! : " + insert);
				return "redirect:/top/rest/list/1";
			}else {
				System.out.println("음식점 등록 실패! : " + insert);
				return "redirect:/top/rest/insert.do";
			}
		}
		
		// 음식점 수정 페이지 
		@SuppressWarnings("null")
		@GetMapping("/rest/update/{restRank}")
		public String restupdate(
				@PathVariable int restRank, 
				Model model, 
				@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
				HttpSession session) {
			Restaurant restaurant = null;
			restaurant = restaurankMapper.selectDetailOne(restRank);
			if(loginUser != null || (loginUser).getAdminCk() == 1) {
				model.addAttribute("restaurant", restaurant);
				System.out.println("restaurant : "+restaurant);
				return "/top/rest/update";			
			} else {
				return "redirect:/user/login.do";
			}
		}
		
		@PostMapping("/rest/update.do")
		public String restupdate(
				Restaurant restaurant,
				Model model,
				@SessionAttribute(name ="loginUser", required = false) UserDto loginUser,
				@RequestParam(name="restaurankImgNo",required = false ) int [] restaurantImgNos,
				@RequestParam(name = "imgFile", required = false) MultipartFile[] imgFiles,
				HttpSession session
				) {
			int update = 0; 
			if((loginUser).getAdminCk() == 1) {
				try {
					int restImgCount = restaurantImgMapper.selectCountRestRank(restaurant.getRest_rank());
					int insertRestImgLength = RESTAURANK_IMG_LIMIT - restImgCount + ((restaurantImgNos != null) ? restaurantImgNos.length : 0);
					// 이미지 저장 
					if(imgFiles != null && insertRestImgLength > 0) {
						List<RestaurantImg> restaurantImgs = new ArrayList<RestaurantImg>();
						for(MultipartFile imgFile : imgFiles) { 
							String[] types = imgFile.getContentType().split("/");
							if(types[0].equals("image")) {
								// 새로운 이미지 등록 
								String newFileName = "rest_" + System.nanoTime() + "." + types[1];
								Path path = Paths.get(savePath + "/" + newFileName);
								imgFile.transferTo(path); // 서버(static 내부에 있는 img 폴더)에 이미지 저장
								
								RestaurantImg restaurantImg = new RestaurantImg();
								restaurantImg.setRest_rank(restaurant.getRest_rank()); 
								restaurantImg.setImg_path(newFileName);
								restaurantImgs.add(restaurantImg);
								
								if(-- insertRestImgLength == 0) break; // 이미지 수가 5개면 반목문 종료 
							}
						}
						if(restaurantImgs.size() > 0) {
							restaurant.setRestaurantImgs(restaurantImgs);
						}
					}
					update = restaurantService.updateRestRemoveRestImg(restaurant, restaurantImgNos); // DB에서 후기 수정
					System.out.println("update : "+update);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/top/rest/update/" + restaurant.getRest_rank();
				}
				if(update > 0) {
					System.out.println("음식점 수정 성공! : " + update);
					return "redirect:/top/rest/detail/" + restaurant.getRest_rank();
				}else {
					System.out.println("음식점 수정 실패! : " + update);
					return "redirect:/top/rest/update/" + restaurant.getRest_rank();
				}	
			}else{ 
				return "redirect:/user/login.do";
			}  
		}
		
		
		// 관광지 정보 삭제 
		@SuppressWarnings("null")
		@GetMapping("/rest/delete/{restRank}/{userId}")
		public String restdelete(
				@PathVariable int restRank,
				@PathVariable String userId,
				@SessionAttribute(name ="loginUser",required = false) UserDto loginUser,
				HttpSession session
				) {
			System.out.println("loginUser : "+loginUser);
			String msg = "";
			if(loginUser != null || loginUser.getAdminCk() == 1) {
				int delete=0;
				try {
					delete = restaurantService.removeRest(restRank);
				} catch(Exception e) {e.printStackTrace();}
				if(delete > 0) {
					msg="음식점 정보 삭제 성공";
					session.setAttribute("msg", msg);
					System.out.println(msg);
					return "redirect:/top/rest/list/1";
				}else {
					msg="음식점 정보 삭제 실패";
					session.setAttribute("msg", msg);
					System.out.println(msg);
					return "redirect:/top/rest/update/" + restRank;			
				}	
			}else {
				msg="로그인 하셔야 이용가능합니다.";
				session.setAttribute("msg", msg);
				System.out.println(msg);
				return "redirect:/user/login.do";
			}
			
		}
}
