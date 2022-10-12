package com.project.mainPage.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.mainPage.dto.Criteria;
import com.project.mainPage.dto.Notice;
// com.project.mainPage.mapper.NoticeMapper
@Mapper
public interface NoticeMapper {
	List<Notice> selectPageAll(
			int startRow, 
			int pageSize,
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	int selectPageAllCount(
			@Param(value = "sort")String sort, 
			@Param(value = "direct")String direct);
	Notice selectDetailOne(int noticeNo);
	int updateViews(int noticeNo);
	int insertOne(Notice notice);
	int updateOne(Notice notice);
	int deleteOne(int noticeNo);
	// 검색
	public List<Notice> searchNotice(Criteria cri);
	// 검색 개수
	public int noticeGetTotal(Criteria cri);	
	// 통합 검색
	public List<Notice> searchAllNotice(Criteria cri);
	public int noticeAllGetTotal(Criteria cri);
}
