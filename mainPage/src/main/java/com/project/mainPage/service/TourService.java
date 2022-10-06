package com.project.mainPage.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.mainPage.dto.BoardImg;
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
	
	@Value("${spring.servlet.multipart.location}")
	String savePath;
	
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
	
	// 관광지 수정
	@Transactional
	public int updateTourRemoveTourImg(Tour tour, int [] tourImgNos) throws Exception{
		int update = 0;
		// 기존 이미지 삭제 
		if(tourImgNos != null) {
			for(int no : tourImgNos) {
				TourImg tourImg = tourImgMapper.selectOne(no);
				File f = new File(savePath + "/" + tourImg.getImg_path() );
				System.out.println("tour의 이미지 파일(서버) 삭제 성공! : " + f.delete());
				int removeTourImg = tourImgMapper.deleteOne(no);
				System.out.println("tour의 tour_img(DB) 삭제 성공! : " + removeTourImg);
			}
		}
		// 새로운 이미지 등록
		if(tour.getTourImgs() != null) { // 이미지가 1개 이상 저장되면 null이 아니다.
			for(TourImg tourImg : tour.getTourImgs()) {
				int registTourImg = tourImgMapper.insertOne(tourImg); // DB에 이미지 저장
				System.out.println("tour의 tour_img(DB) 등록 성공! : " + registTourImg);
			}
		}
		update = tourMapper.updateOne(tour); // 관광지 수정
		System.out.println(update);
		return update;
	}
	
	
	
	
}
