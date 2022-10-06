package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.Restaurant;


// com.project.mainPage.mapper.RestaurankMapper
@Mapper
public interface RestaurankMapper {
	// Restaurant List 
	List<Restaurant> selectListAll(int startRow, int pageSize);
	int selectPageAllCount();
	// detail
	Restaurant selectDetailOne(int restRank);
	// 조회수 
	int updateViews(int restRank);
	// 등록
	int insertOne(Restaurant restaurant);
	// 수정
	int updateOne(Restaurant restaurant);
	// 삭제 
	int deleteOne(int restRank);
}
