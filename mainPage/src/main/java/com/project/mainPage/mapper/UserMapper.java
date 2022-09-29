package com.project.mainPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.Criteria;
import com.project.mainPage.dto.UserDto;

@Mapper
public interface UserMapper {
	List<UserDto> selectPageAll(int startRow, int pageSize);
	int selectPageAllCount();
	UserDto selectIdPwOne(String userId, String userPw);
	UserDto selectId(String userId); //아이디 중복검사
	UserDto selectPhone(String userPhone); //아이디 중복검사
	UserDto selectEmail(String userEmail); //아이디 중복검사
	int deleteOne(String userId);
	int updateOne(UserDto user);
	int insertOne(UserDto user);
	
	//검색
	public List<UserDto> searchUser(Criteria cri);
	//검색 갯수
	public int userGetTotal(Criteria cri);
	
	public List<UserDto> selectSearchAll(Criteria cri);
}
