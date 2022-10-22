package com.company.admin.mapper.loan;

import com.company.admin.entity.loan.ProductTips;
import java.util.List;

/**
 * 产品提示Dao
 * Created by wjc on 2018/11/20.
 */
public interface ProductTipsDao {

	int save(ProductTips productTips);

	int remove(ProductTips productTips);

	int update(ProductTips productTips);

	ProductTips get(ProductTips productTips);

	List<ProductTips> list(ProductTips productTips);

	Long count(ProductTips productTips);
}
