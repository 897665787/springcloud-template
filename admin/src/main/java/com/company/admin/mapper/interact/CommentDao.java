package com.company.admin.mapper.interact;

import com.company.admin.entity.interact.Comment;
import java.util.List;

/**
 * 评论Dao
 * Created by JQ棣 on 2018/11/09.
 */
public interface CommentDao {

	Comment get(Long comment);

	List<Comment> list(Comment comment);

	Long count(Comment comment);
	
	List<Comment> listComment(Comment comment);
	
	int hided(Comment comment);
}
