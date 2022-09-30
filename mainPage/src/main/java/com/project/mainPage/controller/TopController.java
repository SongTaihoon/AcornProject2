package com.project.mainPage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.mainPage.dto.Pagination;
import com.project.mainPage.dto.Tour;
import com.project.mainPage.mapper.TourMapper;

@Controller
@RequestMapping("/top")
public class TopController {

	@Autowired
	TourMapper tourMapper;
	
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
	@GetMapping("/detail/{tourRank}")
	public String detail(
			@PathVariable String tourRank,
			Model model
			) {
		
	}
	
	
}
