package com.company.admin.service.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.loan.ProductTips;
import com.company.admin.mapper.loan.ProductTipsDao;
import com.company.common.exception.BusinessException;

/**
 * 产品提示Service
 * Created by wjc on 2018/11/20.
 */
@Service
public class ProductTipsService {

    @Autowired
    private ProductTipsDao productTipsDao;

    public void save(ProductTips productTips) {
        productTipsDao.save(productTips);
    }

    public void remove(ProductTips productTips) {
        ProductTips existent = get(productTips);
        productTipsDao.remove(existent);
    }

    public void update(ProductTips productTips) {
        ProductTips existent = get(productTips);
        productTipsDao.update(productTips);
    }

    public ProductTips get(ProductTips productTips) {
        ProductTips existent = productTipsDao.get(productTips);
        if (existent == null) {
            throw new BusinessException("产品提示不存在");
        }
        return existent;
    }

    public XSPageModel<ProductTips> listAndCount(ProductTips productTips) {
        productTips.setDefaultSort("id", "DESC");
        return XSPageModel.build(productTipsDao.list(productTips), productTipsDao.count(productTips));
    }
}
