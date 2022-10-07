package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.TourImg;


// com.project.mainPage.mapper.TourImgMapper
@Mapper
public interface TourImgMapper {
<<<<<<< HEAD
	// 관광지 이미지 등록 
=======
>>>>>>> 45d821cd56c005338a34db006d19f19835443a91
	int insertOne(TourImg tourImg);
	List<TourImg> selectTourRank(int tourRank);
	TourImg selectOne(int tourImgNo);
	int deleteOne(int tourImgNo);
	int selectCountTourRank(int tourRank);
}
