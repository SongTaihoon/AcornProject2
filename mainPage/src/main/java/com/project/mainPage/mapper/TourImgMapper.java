package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.TourImg;


// com.project.mainPage.mapper.TourImgMapper
@Mapper
public interface TourImgMapper {
	int insertOne(TourImg tourImg);
	List<TourImg> selectTourRank(int tourRank);
	TourImg selectOne(int tourImgNo);
	int deleteOne(int tourImgNo);
	int selectCountTourRank(int tourRank);
}
