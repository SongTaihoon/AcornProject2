package com.project.mainPage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.mainPage.dto.Tour;
import com.project.mainPage.mapper.TourMapper;

@Service
public class TourService {
	@Autowired
	private TourMapper tourMapper;
	
	public Tour tourUpdateView(int tourRank) throws Exception{
		tourMapper.updateViews(tourRank);
		return tourMapper.selectDetailOne(tourRank);
	}
}
