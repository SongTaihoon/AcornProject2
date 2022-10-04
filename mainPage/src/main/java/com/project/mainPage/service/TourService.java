package com.project.mainPage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.mainPage.dto.Tour;
import com.project.mainPage.dto.TourImg;
import com.project.mainPage.mapper.TourImgMapper;
import com.project.mainPage.mapper.TourMapper;

@Service
public class TourService {
	@Autowired
	private TourMapper tourMapper;
	@Autowired
	private TourImgMapper tourImgMapper;
	
	public Tour tourUpdateView(int tourRank) throws Exception{
		tourMapper.updateViews(tourRank);
		return tourMapper.selectDetailOne(tourRank);
	}

	//관광지 등록
	@Transactional
	public int registTour(Tour tour) throws Exception {
		int regist = 0;
		int imgRegist = 0;
		// useGeneratedKeys = "true" keyProperty = "Tour_rank" : 등록한 pk를 Tour에 저장함
		regist = tourMapper.insertOne(tour);
		if(regist > 0 && tour.getTourImgs() != null) {
			for(TourImg tourImg : tour.getTourImgs()) {
				tourImg.setTour_rank(tour.getTour_rank()); // Auto Increment로 저장된 대표키 값
				imgRegist += tourImgMapper.insertOne(tourImg); // DB에 이미지 저장
			}
		}
		System.out.println("관광지 이미지 등록 성공! : " + imgRegist);
		return regist;
	}
	
	
	
	
	
}
