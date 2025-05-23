package com.company.admin.mapper.marketing;


import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import com.company.admin.entity.marketing.Feedback;

/**
 * Created by JQ棣 on 11/1/17.
 */
public interface FeedbackDao extends BaseMapper<Feedback> {

    @Select("SELECT * FROM sc_feedback WHERE id=#{id}")
    Feedback get(Long id);

    List<Feedback> list(Feedback feedback);

    Long count(Feedback feedback);

}
