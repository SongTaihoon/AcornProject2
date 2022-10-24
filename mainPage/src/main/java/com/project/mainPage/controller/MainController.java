package com.project.mainPage.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.project.mainPage.dto.Acco;
import com.project.mainPage.dto.Restaurant;
import com.project.mainPage.dto.Tour;
import com.project.mainPage.mapper.AccoMapper;
import com.project.mainPage.mapper.RestaurankMapper;
import com.project.mainPage.mapper.TourMapper;
@Controller
public class MainController {
	@Autowired
	private TourMapper tourMapper;
	
	@Autowired
	private RestaurankMapper restaurankMapper;
	
	@Autowired
	private AccoMapper accoMapper;
	
	@GetMapping("/")
	public String index(Model model) {
		try {
			List<Tour> tourList = tourMapper.mainPageTour();
			List<Acco> accoList = accoMapper.mainPageAcco();
			List<Restaurant> restList = restaurankMapper.mainPageRest();
			
			model.addAttribute("tourList", tourList);
			model.addAttribute("accoList", accoList);
			model.addAttribute("restList", restList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	@GetMapping("/recommendation")
	public String recommendation(Model model) {
		try {
			List<Tour> tourList = tourMapper.mainPageTour();
			List<Acco> accoList = accoMapper.mainPageAcco();
			List<Restaurant> restList = restaurankMapper.mainPageRest();
			
			model.addAttribute("tourList", tourList);
			model.addAttribute("accoList", accoList);
			model.addAttribute("restList", restList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "recommendation";
	}
}
