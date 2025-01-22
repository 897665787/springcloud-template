package com.company.admin.entity.stats;

import com.company.admin.entity.base.BaseModel;

/**
 * @Author: JQ棣
 * @Date: 2018/9/21 10:43
 */
public class UserSexStatsDetail extends BaseModel {

    /**
     * 日期
     */
    private String label;

    /**
     * 新增人数（女）
     */
    private Long incre0;
    /**
     * 新增人数（男）
     */
    private Long incre1;

    /**
     * 累积人数（女）
     */
    private Long accu0;
    /**
     * 累积人数（男）
     */
    private Long accu1;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getIncre0() {
        return incre0;
    }

    public void setIncre0(Long incre0) {
        this.incre0 = incre0;
    }

    public Long getIncre1() {
        return incre1;
    }

    public void setIncre1(Long incre1) {
        this.incre1 = incre1;
    }

    public Long getAccu0() {
        return accu0;
    }

    public void setAccu0(Long accu0) {
        this.accu0 = accu0;
    }

    public Long getAccu1() {
        return accu1;
    }

    public void setAccu1(Long accu1) {
        this.accu1 = accu1;
    }
}
