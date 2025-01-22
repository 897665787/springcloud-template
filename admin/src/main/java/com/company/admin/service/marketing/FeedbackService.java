package com.company.admin.service.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.marketing.Feedback;
import com.company.admin.mapper.marketing.FeedbackDao;
import com.company.common.exception.BusinessException;

/**
 * 用户反馈ServiceImpl
 * Created by JQ棣 on 11/1/17.
 */
@Service
public class FeedbackService {

    @Autowired
    FeedbackDao feedbackDao;

    public void remove(Feedback feedback) {
        Feedback existent = get(feedback);
        feedbackDao.deleteById(existent.getId());
    }

    public Feedback get(Feedback feedback) {
        Feedback existent = feedbackDao.get(feedback.getId());
        if (existent == null) {
            throw new BusinessException("反馈不存在");
        }
        return existent;
    }

    public XSPageModel<Feedback> listAndCount(Feedback feedback) {
        feedback.setDefaultSort("create_time", "DESC");
        return XSPageModel.build(feedbackDao.list(feedback), feedbackDao.count(feedback));
    }

    public void solve(Feedback feedback) {
        feedback.setStatus(1);
        feedbackDao.updateById(feedback);
    }
}
