package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.Tour;


// com.project.mainPage.mapper.TourMapper
@Mapper
public interface TourMapper {
	List<Tour> selectListAll(int startRow, int pageSize);
	int selectPageAllCount();
	// detail 
	Tour selectDetailOne(int tourRank);
	// 조회수 
	int updateViews(int tourRank);
	int insertOne(Tour tour);
}

