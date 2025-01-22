package com.company.admin.mapper.marketing;

import com.company.admin.entity.marketing.HotWord;
import java.util.List;

/**
 * 热搜词Dao
 * Created by JQ棣 on 2018/11/07.
 */
public interface HotWordDao {

	int save(HotWord hotWord);

	int remove(HotWord hotWord);

	int update(HotWord hotWord);

	HotWord get(HotWord hotWord);

	List<HotWord> list(HotWord hotWord);

	Long count(HotWord hotWord);
}
