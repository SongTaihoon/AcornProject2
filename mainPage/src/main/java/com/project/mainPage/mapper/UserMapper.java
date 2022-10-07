package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.mainPage.dto.Criteria;
import com.project.mainPage.dto.UserDto;

@Mapper
public interface UserMapper {
	List<UserDto> selectPageAll(
			int startRow, 
			int pageSize,
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	int selectPageAllCount(
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	
	UserDto selectIdPwOne(String userId, String userPw); // 로그인
	UserDto selectId(String userId); // 아이디 중복 검사
	UserDto selectPhone(String userPhone); // 전화번호 중복 검사
	UserDto selectEmail(String userEmail); // 이메일 중복 검사
	
	int deleteOne(String userId);
	int updateOne(UserDto user);
	int insertOne(UserDto user);
	
	// 검색
	public List<UserDto> searchUser(
			Criteria cri,
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	// 검색 개수
	public int userGetTotal(
			Criteria cri,
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	
	public List<UserDto> selectSearchAll(Criteria cri);
}
