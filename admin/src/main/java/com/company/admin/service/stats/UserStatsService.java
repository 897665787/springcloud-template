package com.company.admin.service.stats;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.admin.entity.stats.StatsCond;
import com.company.admin.entity.stats.UserAgeStatsDetail;
import com.company.admin.entity.stats.UserSexStatsDetail;
import com.company.admin.entity.stats.UserStatsDetail;
import com.company.admin.mapper.stats.UserStatsDao;

/**
 * 商品Service
 * Created by JQ棣 on 2018/06/09.
 */
@Service
@RequiredArgsConstructor
public class UserStatsService {

	private final UserStatsDao userStatsDao;

	/**
	 * 昨日关键指标
	 */
	public Map<String, Double> summary() {
		Map<String, Long> map = userStatsDao.summary();
		Long increCount = map.get("increCount");
		Long accuCount = map.get("accuCount");
		Map<String, Double> result = new HashMap<>();
		result.put("increCount", BigDecimal.valueOf(increCount).setScale(0).doubleValue());
		result.put("increCount1", computeRate(increCount, map.get("increCount1")));
		result.put("increCount7", computeRate(increCount, map.get("increCount7")));
		result.put("increCount30", computeRate(increCount, map.get("increCount30")));

		result.put("accuCount", BigDecimal.valueOf(accuCount).setScale(0).doubleValue());
		result.put("accuCount1", computeRate(accuCount, map.get("accuCount1")));
		result.put("accuCount7", computeRate(accuCount, map.get("accuCount7")));
		result.put("accuCount30", computeRate(accuCount, map.get("accuCount30")));
		return result;
	}

	public Double computeRate(Long a, Long b){
		if(Objects.equals(0L,b)){
			return null;
		}else{
			return BigDecimal.valueOf((a-b)*100).divide(BigDecimal.valueOf(b), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
	}

	/**
	 * 用户增长分析统计图
	 */
	public Map<String, Object> diagram(StatsCond statsCond) throws ParseException {
		//生成日期标签
		generateLabel(statsCond);

		List<UserStatsDetail> increList = userStatsDao.diagramIncre(statsCond);
		Map<String, Long> groupStats = new HashMap<>();
		increList.stream().forEach(a->groupStats.put(a.getLabel(), a.getIncre()));

		List<UserStatsDetail> list = new ArrayList<>();
		List<Long> accuDatasets = new ArrayList<>();
		List<Long> increDatasets = new ArrayList<>();
		for(int i=0;i<statsCond.getLabels().size();i++){
			String a = statsCond.getLabels().get(i);
			UserStatsDetail detail = new UserStatsDetail();
			detail.setLabel(a);
			detail.setIncre(Optional.ofNullable(groupStats.get(a)).orElse(0L));

			if(i == 0){
				detail.setAccu(userStatsDao.diagramAccuFirst(statsCond));
			}else{
				detail.setAccu(list.get(i-1).getAccu()+detail.getIncre());
			}

			accuDatasets.add(detail.getAccu());
			increDatasets.add(detail.getIncre());
			list.add(detail);
		}

		Map<String, Object> result = new HashMap<>();
		result.put("labels", statsCond.getLabels());
		result.put("accuDatasets", accuDatasets);//累积用户
		result.put("increDatasets", increDatasets);//新增用户
		Collections.reverse(list);//倒序
		result.put("detail", list);//统计详情列表
		return result;
	}

	/**
	 * 性别比例概况
	 */
	public Map<String, Integer> sexSummary() {
		return userStatsDao.sexSummary();
	}

	/**
	 * 性别分析统计图
	 */
	public Map<String, Object> sexDiagram(StatsCond statsCond) throws ParseException {
		//生成日期标签
		generateLabel(statsCond);

		List<UserStatsDetail> increList = userStatsDao.sexDiagramIncre(statsCond);
		List<UserStatsDetail> firstList = userStatsDao.sexDiagramAccuFirst(statsCond);
		UserSexStatsDetail first = new UserSexStatsDetail();
		first.setAccu0(0L);
		first.setAccu1(0L);
		firstList.stream().forEach(a->{
			if(a.getGname().equals("0")){
				first.setAccu0(a.getAccu());
			}else if(a.getGname().equals("1")){
				first.setAccu1(a.getAccu());
			}
		});

		List<UserSexStatsDetail> list = new ArrayList<>();
		Map<String, List<UserStatsDetail>> groupStats = increList.stream().collect(Collectors.groupingBy(UserStatsDetail::getLabel,LinkedHashMap::new,Collectors.toList()));
		for(int i=0;i<statsCond.getLabels().size();i++){
			String a = statsCond.getLabels().get(i);
			UserSexStatsDetail detail = new UserSexStatsDetail();
			detail.setLabel(a);
			detail.setIncre0(0L);
			detail.setIncre1(0L);
			List<UserStatsDetail> increDetail = groupStats.get(a);
			if(!CollectionUtils.isEmpty(increDetail)){
				increDetail.stream().forEach(b->{
					if(b.getGname().equals("0")){
						detail.setIncre0(b.getIncre());
					}else if(b.getGname().equals("1")){
						detail.setIncre1(b.getIncre());
					}
				});
			}

			if(i == 0){
				detail.setAccu0(first.getAccu0());
				detail.setAccu1(first.getAccu1());
			}else{
				UserSexStatsDetail prev =list.get(i-1);
				detail.setAccu0(prev.getAccu0()+detail.getIncre0());
				detail.setAccu1(prev.getAccu1()+detail.getIncre1());
			}

			list.add(detail);
		}

		List<Long> increDatasets0 = new ArrayList<>();
		List<Long> increDatasets1 = new ArrayList<>();
		List<Long> accuDatasets0 = new ArrayList<>();
		List<Long> accuDatasets1 = new ArrayList<>();
		list.stream().forEach(a->{
			accuDatasets0.add(a.getAccu0());
			accuDatasets1.add(a.getAccu1());
			increDatasets0.add(a.getIncre0());
			increDatasets1.add(a.getIncre1());
		});

		Map<String, Object> result = new HashMap<>();
		result.put("labels", statsCond.getLabels());
		result.put("accuDatasets0", accuDatasets0);
		result.put("accuDatasets1", accuDatasets1);
		result.put("increDatasets0", increDatasets0);
		result.put("increDatasets1", increDatasets1);
		Collections.reverse(list);//倒序
		result.put("detail", list);//统计详情列表
		return result;
	}

	/**
	 * 各年龄层比例概况
	 */
	public List<Integer> ageSummary() {
		return userStatsDao.ageSummary();
	}

	/**
	 * 年龄分析统计图
	 */
	public Map<String, Object> ageDiagram(StatsCond statsCond) throws ParseException {
		//生成日期标签
		generateLabel(statsCond);

		List<UserStatsDetail> increList = userStatsDao.ageDiagramIncre(statsCond);
		List<UserStatsDetail> firstList = userStatsDao.ageDiagramAccuFirst(statsCond);
		UserAgeStatsDetail first = new UserAgeStatsDetail();
		first.setAccu1(0L);
		first.setAccu2(0L);
		first.setAccu3(0L);
		first.setAccu4(0L);
		first.setAccu5(0L);
		first.setAccu6(0L);
		first.setAccu7(0L);
		firstList.stream().forEach(a->{
			if(a.getGname().equals("1")){
				first.setAccu1(a.getAccu());
			}else if(a.getGname().equals("2")){
				first.setAccu2(a.getAccu());
			}else if(a.getGname().equals("3")){
				first.setAccu3(a.getAccu());
			}else if(a.getGname().equals("4")){
				first.setAccu4(a.getAccu());
			}else if(a.getGname().equals("5")){
				first.setAccu5(a.getAccu());
			}else if(a.getGname().equals("6")){
				first.setAccu6(a.getAccu());
			}else if(a.getGname().equals("7")){
				first.setAccu7(a.getAccu());
			}
		});

		List<UserAgeStatsDetail> list = new ArrayList<>();
		Map<String, List<UserStatsDetail>> groupStats = increList.stream().collect(Collectors.groupingBy(UserStatsDetail::getLabel,LinkedHashMap::new,Collectors.toList()));

		for(int i=0;i<statsCond.getLabels().size();i++){
			String a = statsCond.getLabels().get(i);
			UserAgeStatsDetail detail = new UserAgeStatsDetail();
			detail.setLabel(a);
			detail.setIncre1(0L);
			detail.setIncre2(0L);
			detail.setIncre3(0L);
			detail.setIncre4(0L);
			detail.setIncre5(0L);
			detail.setIncre6(0L);
			detail.setIncre7(0L);
			List<UserStatsDetail> increDetail = groupStats.get(a);
			if(!CollectionUtils.isEmpty(increDetail)){
				increDetail.stream().forEach(b->{
					if(b.getGname().equals("1")){
						detail.setIncre1(b.getIncre());
					}else if(b.getGname().equals("2")){
						detail.setIncre2(b.getIncre());
					}else if(b.getGname().equals("3")){
						detail.setIncre3(b.getIncre());
					}else if(b.getGname().equals("4")){
						detail.setIncre4(b.getIncre());
					}else if(b.getGname().equals("5")){
						detail.setIncre5(b.getIncre());
					}else if(b.getGname().equals("6")){
						detail.setIncre6(b.getIncre());
					}else if(b.getGname().equals("7")){
						detail.setIncre7(b.getIncre());
					}
				});
			}

			if(i == 0){
				detail.setAccu1(first.getAccu1());
				detail.setAccu2(first.getAccu2());
				detail.setAccu3(first.getAccu3());
				detail.setAccu4(first.getAccu4());
				detail.setAccu5(first.getAccu5());
				detail.setAccu6(first.getAccu6());
				detail.setAccu7(first.getAccu7());
			}else{
				UserAgeStatsDetail prev = list.get(i-1);
				detail.setAccu1(prev.getAccu1()+detail.getIncre1());
				detail.setAccu2(prev.getAccu2()+detail.getIncre2());
				detail.setAccu3(prev.getAccu3()+detail.getIncre3());
				detail.setAccu4(prev.getAccu4()+detail.getIncre4());
				detail.setAccu5(prev.getAccu5()+detail.getIncre5());
				detail.setAccu6(prev.getAccu6()+detail.getIncre6());
				detail.setAccu7(prev.getAccu7()+detail.getIncre7());
			}

			list.add(detail);
		}

		List<Long> increDatasets1 = new ArrayList<>();
		List<Long> increDatasets2 = new ArrayList<>();
		List<Long> increDatasets3 = new ArrayList<>();
		List<Long> increDatasets4 = new ArrayList<>();
		List<Long> increDatasets5 = new ArrayList<>();
		List<Long> increDatasets6 = new ArrayList<>();
		List<Long> increDatasets7 = new ArrayList<>();
		List<Long> accuDatasets1 = new ArrayList<>();
		List<Long> accuDatasets2 = new ArrayList<>();
		List<Long> accuDatasets3 = new ArrayList<>();
		List<Long> accuDatasets4 = new ArrayList<>();
		List<Long> accuDatasets5 = new ArrayList<>();
		List<Long> accuDatasets6 = new ArrayList<>();
		List<Long> accuDatasets7 = new ArrayList<>();
		list.stream().forEach(a->{
			accuDatasets1.add(a.getAccu1());
			accuDatasets2.add(a.getAccu2());
			accuDatasets3.add(a.getAccu3());
			accuDatasets4.add(a.getAccu4());
			accuDatasets5.add(a.getAccu5());
			accuDatasets6.add(a.getAccu6());
			accuDatasets7.add(a.getAccu7());
			increDatasets1.add(a.getIncre1());
			increDatasets2.add(a.getIncre2());
			increDatasets3.add(a.getIncre3());
			increDatasets4.add(a.getIncre4());
			increDatasets5.add(a.getIncre5());
			increDatasets6.add(a.getIncre6());
			increDatasets7.add(a.getIncre7());
		});

		Map<String, Object> result = new HashMap<>();
		result.put("labels", statsCond.getLabels());
		result.put("accuDatasets1", accuDatasets1);
		result.put("accuDatasets2", accuDatasets2);
		result.put("accuDatasets3", accuDatasets3);
		result.put("accuDatasets4", accuDatasets4);
		result.put("accuDatasets5", accuDatasets5);
		result.put("accuDatasets6", accuDatasets6);
		result.put("accuDatasets7", accuDatasets7);
		result.put("increDatasets1", increDatasets1);
		result.put("increDatasets2", increDatasets2);
		result.put("increDatasets3", increDatasets3);
		result.put("increDatasets4", increDatasets4);
		result.put("increDatasets5", increDatasets5);
		result.put("increDatasets6", increDatasets6);
		result.put("increDatasets7", increDatasets7);
		Collections.reverse(list);//倒序
		result.put("detail", list);//统计详情列表
		return result;
	}

	/**
	 * 生成日期标签
	 */
	private void generateLabel(StatsCond statsCond) throws ParseException {
		SimpleDateFormat sdf;
		int dateType;
		List<String> labs = new ArrayList<>();
		if(statsCond.getFreq()==2){
			sdf = new SimpleDateFormat("yyyy-MM");
			dateType = Calendar.MONTH;
			statsCond.setDateFormat("%Y-%m");
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			dateType = Calendar.DATE;
			statsCond.setDateFormat("%Y-%m-%d");
		}

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(sdf.parse(statsCond.getStartTime()));
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(sdf.parse(statsCond.getEndTime()));
		while (!startDate.after(endDate)) {
			labs.add(sdf.format(startDate.getTime()));
			startDate.add(dateType, 1);
		}
		statsCond.setLabels(labs);
	}
}
