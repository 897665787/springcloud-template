package com.company.admin.service.interact;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.admin.entity.interact.PraiseNum;
import com.company.admin.mapper.interact.PraiseNumDao;
import com.google.common.collect.Maps;

/**
 * 
 * Created by JQ棣 on 2018/05/21.
 */
@Service
@RequiredArgsConstructor
public class PraiseService {
	private final PraiseNumDao praiseNumDao;

	public Map<Long, Integer> mapNumber(Integer module, List<Long> relativeIdList) {
		List<PraiseNum> praiseNumList = praiseNumDao.listByRelativeIdList(module, relativeIdList);

		Map<Long, Integer> numberMap = praiseNumList.stream()
				.collect(Collectors.toMap(PraiseNum::getRelativeId, PraiseNum::getNumber));

		Map<Long, Integer> result = Maps.newHashMap();
		for (Long relativeId : relativeIdList) {
			result.put(relativeId, numberMap.getOrDefault(relativeId, 0));
		}
		return result;
	}

	public Integer getNumber(Integer module, Long relativeId) {
		Integer number = praiseNumDao.getNumber(new PraiseNum().setModule(module).setRelativeId(relativeId));
		return Optional.ofNullable(number).orElse(0);
	}

}
