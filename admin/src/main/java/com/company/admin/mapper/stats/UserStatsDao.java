package com.company.admin.mapper.stats;

import com.company.admin.entity.stats.StatsCond;
import com.company.admin.entity.stats.UserStatsDetail;

import java.util.List;
import java.util.Map;

/**
 * 订单Dao
 * Created by JQ棣 on 2018/07/05.
 */
public interface UserStatsDao {

	Map<String, Long> summary();
    Map<String, Integer> sexSummary();
    List<Integer> ageSummary();

	List<UserStatsDetail> diagramIncre(StatsCond statsCond);
	List<UserStatsDetail> sexDiagramIncre(StatsCond statsCond);
	List<UserStatsDetail> ageDiagramIncre(StatsCond statsCond);

	Long diagramAccuFirst(StatsCond statsCond);
	List<UserStatsDetail> sexDiagramAccuFirst(StatsCond statsCond);
	List<UserStatsDetail> ageDiagramAccuFirst(StatsCond statsCond);
}
