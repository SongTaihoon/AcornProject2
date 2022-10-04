package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.Rest;


// com.project.mainPage.mapper.RestMapper
@Mapper
public interface RestMapper {
	List<Rest> selectListAll(int startRow, int pageSize);
	int selectPageAllCount();
	// detail 
	Rest selectDetailOne(int restRank);
	// 조회수 
	int updateViews(int restRank);
}

