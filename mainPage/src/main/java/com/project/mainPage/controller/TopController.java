package com.project.mainPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/top")
public class TopController {

	@GetMapping("/tour")
	public String tour(){
		return "top/tour";
	}
	
}
