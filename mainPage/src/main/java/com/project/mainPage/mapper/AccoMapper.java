package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.Acco;


// com.project.mainPage.mapper.AccoMapper
@Mapper
public interface AccoMapper {
	List<Acco> selectListAll(int startRow, int pageSize);
	int selectPageAllCount();
	// detail 
	Acco selectDetailOne(int accoRank);
	// 조회수 
	int updateViews(int accoRank);
	// 등록
	int insertOne(Acco acco);
	// 수정
	int updateOne(Acco acco);
	// 삭제 
	int deleteOne(int accoRank);
	
}

