package com.project.mainPage.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.mainPage.dto.Acco;
import com.project.mainPage.dto.AccoImg;
import com.project.mainPage.mapper.AccoImgMapper;
import com.project.mainPage.mapper.AccoMapper;

@Service
public class AccoService {
	@Autowired
	private AccoMapper accoMapper;
	@Autowired
	private AccoImgMapper accoImgMapper;
	
	@Value("${spring.servlet.multipart.location}")
	String savePath;
	
	public Acco accoUpdateView(int accoRank) throws Exception{
		accoMapper.updateViews(accoRank);
		return accoMapper.selectDetailOne(accoRank);
	}

	//관광지 등록
	@Transactional
	public int registAcco(Acco acco) throws Exception {
		int regist = 0;
		int imgRegist = 0;
		// useGeneratedKeys = "true" keyProperty = "Acco_rank" : 등록한 pk를 Tour에 저장함
		regist = accoMapper.insertOne(acco);
		if(regist > 0 && acco.getAccoImgs() != null) {
			for(AccoImg accoImg : acco.getAccoImgs()) {
				accoImg.setAcco_rank(acco.getAcco_rank()); // Auto Increment로 저장된 대표키 값
				imgRegist += accoImgMapper.insertOne(accoImg); // DB에 이미지 저장
			}
		}
		System.out.println("숙박업소 이미지 등록 성공! : " + imgRegist);
		return regist;
	}
	
	// 관광지 수정
	@Transactional
	public int updateAccoRemoveAccoImg(Acco acco, int [] accoImgNos) throws Exception{
		int update = 0;
		// 기존 이미지 삭제 
		if(accoImgNos != null) {
			for(int no : accoImgNos) {
				AccoImg accoImg = accoImgMapper.selectOne(no);
				File f = new File(savePath + "/" + accoImg.getImg_path() );
				System.out.println("acco의 이미지 파일(서버) 삭제 성공! : " + f.delete());
				int removeAccoImg = accoImgMapper.deleteOne(no);
				System.out.println("acco의 acco_img(DB) 삭제 성공! : " + removeAccoImg);
			}
		}
		// 새로운 이미지 등록
		if(acco.getAccoImgs() != null) { // 이미지가 1개 이상 저장되면 null이 아니다.
			for(AccoImg accoImg : acco.getAccoImgs()) {
				int registAccoImg = accoImgMapper.insertOne(accoImg); // DB에 이미지 저장
				System.out.println("acco의 acco_img(DB) 등록 성공! : " + registAccoImg);
			}
		}
		update = accoMapper.updateOne(acco); // 관광지 수정
		System.out.println("service update : "+update);
		return update;
	}
}
