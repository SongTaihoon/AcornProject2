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
	// 등록
	int insertOne(Tour tour);
	// 수정
	int updateOne(Tour toru);
	// 삭제 
	int deleteOne(int tourRank);
	
}

