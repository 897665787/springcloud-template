package com.company.admin.service.interact;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.admin.entity.interact.CollectNum;
import com.company.admin.mapper.interact.CollectNumDao;
import com.google.common.collect.Maps;

/**
 * 
 * Created by JQ棣 on 2018/05/21.
 */
@Service
@RequiredArgsConstructor
public class CollectService {
	private final CollectNumDao collectNumDao;

	public Map<Long, Integer> mapNumber(Integer module, List<Long> relativeIdList) {
		List<CollectNum> collectNumList = collectNumDao.listByRelativeIdList(module,relativeIdList);
		
		Map<Long, Integer> numberMap = collectNumList.stream().collect(Collectors.toMap(CollectNum::getRelativeId,
				CollectNum::getNumber));
				
		Map<Long, Integer> result = Maps.newHashMap();
		for (Long relativeId : relativeIdList) {
			result.put(relativeId, numberMap.getOrDefault(relativeId, 0));
		}
		return result;
	}
	
	public Integer getNumber(Integer module, Long relativeId) {
		Integer number = collectNumDao.getNumber(new CollectNum().setModule(module).setRelativeId(relativeId));
		return Optional.ofNullable(number).orElse(0);
	}
	
}
