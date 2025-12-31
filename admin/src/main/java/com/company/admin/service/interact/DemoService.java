package com.company.admin.service.interact;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.interact.Demo;
import com.google.common.collect.Lists;

/**
 * 实例代码，项目中请删除
 */
@Deprecated
@Service
@RequiredArgsConstructor
public class DemoService {
	private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

	private final CommentService commentService;
	private final PraiseService praiseService;
	private final CollectService collectService;

	public XSPageModel<Demo> listAndCount(Demo condition) {
		List<Demo> list = Lists.newArrayList(new Demo().setId(1L).setUserId("userid1"),
				new Demo().setId(2L).setUserId("userid2"), new Demo().setId(3L).setUserId("userid3"),
				new Demo().setId(4L).setUserId("userid4"));
		
		List<Long> idList = list.stream().map(Demo::getId).collect(Collectors.toList());

		/*同步处理*/
		Map<Long, Integer> commentNumberMap = commentService.mapNumber(2, idList);
		Map<Long, Integer> praiseNumberMap = praiseService.mapNumber(2, idList);
		Map<Long, Integer> collectNumberMap = collectService.mapNumber(1, idList);
		for (Demo demo : list) {
			demo.setCommentNum(commentNumberMap.get(demo.getId()));
			demo.setPraiseNum(praiseNumberMap.get(demo.getId()));
			demo.setCollectNum(collectNumberMap.get(demo.getId()));
		}
		return XSPageModel.build(list, Long.valueOf(list.size()));
	}

	public Demo get(Demo demo) {
		List<Demo> list = Lists.newArrayList(new Demo().setId(1L).setUserId("userid1"),
				new Demo().setId(2L).setUserId("userid2"), new Demo().setId(3L).setUserId("userid3"),
				new Demo().setId(4L).setUserId("userid4"));
		Map<Long, Demo> demoMap = list.stream().collect(Collectors.toMap(Demo::getId, Function.identity()));
		
		Demo demo2 = demoMap.get(demo.getId());
		demo2.setCommentNum(401);
		demo2.setCommentNum(commentService.getNumber(2, demo2.getId()));
		demo2.setPraiseNum(praiseService.getNumber(2, demo2.getId()));
		demo2.setCollectNum(praiseService.getNumber(1, demo2.getId()));
		return demo2;
	}
}
