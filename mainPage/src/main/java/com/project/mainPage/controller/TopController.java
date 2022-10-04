package com.project.mainPage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.mainPage.dto.Acco;
import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.Rest;
import com.project.mainPage.dto.Tour;
import com.project.mainPage.mapper.AccoMapper;
import com.project.mainPage.mapper.RestMapper;
import com.project.mainPage.mapper.TourMapper;
import com.project.mainPage.service.AccoService;
import com.project.mainPage.service.RestService;
import com.project.mainPage.service.TourService;

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
	
	// 관광지 TOP 10 LIST
	@GetMapping("/tour/{page}")
	public String tour(
			@PathVariable int page,
			Model model){
		int row = 10;
		int startRow = (page - 1) * row;
		List<Tour> tourList = tourMapper.selectListAll(startRow, row);
		int count = tourMapper.selectPageAllCount();
		Pagination pagination = new Pagination(page, count, "/top/tour/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("tourList",tourList);	
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "top/tour";
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
		System.out.println("tour" + tour);
		if(tour != null) {
			model.addAttribute(tour);
			return "/top/tour/detail";
		}else {
			return "redirect:/top/tour/1";	
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
	
	@GetMapping("/acco/{page}")
	public String acco(
			@PathVariable int page,
			Model model){
		int row = 10;
		int startRow = (page - 1) * row;
		List<Acco> accoList = accoMapper.selectListAll(startRow, row);
		int count = accoMapper.selectPageAllCount();
		Pagination pagination = new Pagination(page, count, "/top/acco/", row);
		model.addAttribute("pagination", pagination);
		model.addAttribute("accoList",accoList);	
		model.addAttribute("row", row);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "top/acco";
	}
	
	@GetMapping("/acco/detail/{accoRank}")
	public String accodetail(
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
			return "redirect:/top/acco/1";	
		}
		
		
	}
	
}
