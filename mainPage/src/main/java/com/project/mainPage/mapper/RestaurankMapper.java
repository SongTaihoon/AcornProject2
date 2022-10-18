package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.Restaurant;
import com.project.mainPage.dto.Tour;


// com.project.mainPage.mapper.RestaurankMapper
@Mapper
public interface RestaurankMapper {
	// Restaurant List 
	List<Restaurant> selectListAll(int startRow, int pageSize);
	int selectPageAllCount();
	// detail
	Restaurant selectDetailOne(Integer restRank);
	// 조회수 
	int updateViews(int restRank);
	// 등록
	int insertOne(Restaurant restaurant);
	// 수정
	int updateOne(Restaurant restaurant);
	// 삭제 
	int deleteOne(int restRank);
	// 메인 화면에 출력
	Restaurant mainPageRest();
}
