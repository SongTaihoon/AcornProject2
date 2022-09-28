package com.project.mainPage.service;
import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.project.mainPage.dto.BoardImg;
import com.project.mainPage.dto.Reply;
import com.project.mainPage.mapper.BoardImgMapper;
import com.project.mainPage.mapper.ReplyMapper;
import com.project.mainPage.mapper.UsersMapper;
@Service
public class UserService {
	@Autowired
	private UsersMapper userMapper;
	
	@Autowired
	private BoardImgMapper boardImgMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Value("${spring.servlet.multipart.location}")
	private String savePath;
	
	public int removeUser(String userid) {
//		UserService가 필요한 이유
//		DB에서는 유저를 삭제할 시 유저가 등록한 후기, 댓글, 이미지가 on cascade로 같이 삭제가 되지만 서버(static 폴더 내부에 있는 img 폴더)에 저장된 이미지는 그대로 남아 있기 때문에 따로 삭제해야 함!
		int remove = 0;
		List<BoardImg> boardImgList = boardImgMapper.selectUserId(userid); // 해당 유저가 작성한 모든 후기 글 
		List<Reply> replyList = replyMapper.selectUserId(userid); // 해당 유저가 작성한 모든 댓글
		if(boardImgList != null) { // 해당 유저가 작성한 후기 글에 등록된 모든 이미지들 삭제 
			boardImgList.stream()
				.filter(r -> r.getImg_path() != null)
				.map(BoardImg::getImg_path)
				.forEach(img -> {
					File file = new File(savePath + "/" + img);
					System.out.println("유저가 작성한 후기의 이미지 삭제 성공! : " + file.delete());
				});
		}
		if(replyList != null) { // 해당 유저가 작성한 댓글에 등록된 모든 이미지들 삭제 
			replyList.stream()
				.filter(r -> r.getImg_path() != null)
				.map(Reply::getImg_path)
				.forEach(img -> {
					File file = new File(savePath + "/" + img);
					System.out.println("유저가 작성한 댓글의 이미지 삭제 성공! : " + file.delete());
				});
		}
		remove = userMapper.deleteOne(userid); // DB에서 유저 삭제
		return remove;
	}
}