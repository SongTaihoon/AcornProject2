package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.RestaurantImg;


// com.project.mainPage.mapper.RestaurantImgMapper
@Mapper
public interface RestaurantImgMapper {
	int insertOne(RestaurantImg restaurantImg);
	List<RestaurantImg> selectRestRank(int restRank);
	RestaurantImg selectOne(int restaurankImgNo);
	int deleteOne(int restaurankImgNo);
	int selectCountRestRank(int restRank);
}
