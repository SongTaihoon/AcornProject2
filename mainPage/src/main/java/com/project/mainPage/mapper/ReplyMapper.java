package com.project.mainPage.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.mainPage.dto.Reply;
@Mapper
public interface ReplyMapper {
	public Reply selectOne(int reply_no);
	public List<Reply> selectBoardNo(int board_no);
	int selectBoardNoCount(int boardNo);
	List<Reply> selectBoardNoPage(int boardNo, int startRow, int pageSize);
	List<Reply> selectBoardNoPage(int boardNo, int startRow, int pageSize, String loginUsersId);
	public List<Reply> selectUserId(String user_id);
	public int insertOne(Reply reply);
	public int updateOne(Reply reply);
	public int deleteOne(int reply_no);
}
