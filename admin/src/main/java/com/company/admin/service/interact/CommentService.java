package com.company.admin.service.interact;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.company.framework.globalresponse.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.interact.Comment;
import com.company.admin.entity.interact.CommentNum;
import com.company.admin.mapper.interact.CommentDao;
import com.company.admin.mapper.interact.CommentNumDao;
import com.google.common.collect.Maps;

/**
 * 评论Service
 * Created by JQ棣 on 2018/11/09.
 */
@Service
public class CommentService {

	@Autowired
	private CommentDao commentDao;
	@Autowired
	private CommentNumDao commentNumDao;
	
	@Transactional
	public void hided(Comment comment) {
		int affect = commentDao.hided(comment);
		if (affect == 1) {
			Comment existent = commentDao.get(comment.getId());
			
			commentNumDao.numberChange(new CommentNum().setModule(existent.getModule())
					.setRelativeId(existent.getToId() == null ? existent.getRelativeId() : existent.getToId())
					.setNumber(Objects.equals(comment.getHided(), 1) ? -1 : 1));
		}
	}

	public Comment get(Comment comment) {
		Comment existent = commentDao.get(comment.getId());
		if (existent == null) {
			ExceptionUtil.throwException("评论不存在");
		}
		return existent;
	}

	public XSPageModel<Comment> listAndCount(Comment comment) {
		comment.setDefaultSort("id", "DESC");
		return XSPageModel.build(commentDao.list(comment), commentDao.count(comment));
	}

	public List<Comment> listComment(Comment comment) {
		comment.setDefaultSort("id", "ASC");
		return commentDao.listComment(comment);
	}

	public Map<Long, Integer> mapNumber(Integer module, List<Long> relativeIdList) {
		List<CommentNum> commentNumList = commentNumDao.listByRelativeIdList(module, relativeIdList);

		Map<Long, Integer> numberMap = commentNumList.stream()
				.collect(Collectors.toMap(CommentNum::getRelativeId, CommentNum::getNumber));

		Map<Long, Integer> result = Maps.newHashMap();
		for (Long relativeId : relativeIdList) {
			result.put(relativeId, numberMap.getOrDefault(relativeId, 0));
		}
		return result;
	}

	public Integer getNumber(Integer module, Long relativeId) {
		return commentNumDao.getNumber(new CommentNum().setModule(module).setRelativeId(relativeId));
	}
}
