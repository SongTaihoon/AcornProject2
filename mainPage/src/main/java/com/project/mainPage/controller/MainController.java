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
			Tour tour1 = tourMapper.mainPageTour();
			Acco acco1 = accoMapper.mainPageAcco();
			Restaurant rest1 = restaurankMapper.mainPageRest();
			
			model.addAttribute("tour1", tour1);
			model.addAttribute("acco1", acco1);
			model.addAttribute("rest1", rest1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
}
