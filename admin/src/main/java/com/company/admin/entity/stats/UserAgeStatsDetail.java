package com.company.admin.entity.stats;

import com.company.admin.entity.base.BaseModel;

/**
 * @Author: kunye
 * @Date: 2018/9/21 10:43
 */
public class UserAgeStatsDetail extends BaseModel {

    /**
     * 日期
     */
    private String label;

    /**
     * 新增人数（80岁以上）
     */
    private Long incre1;
    /**
     * 新增人数（71~80岁）
     */
    private Long incre2;
    /**
     * 新增人数（61~70岁）
     */
    private Long incre3;
    /**
     * 新增人数（51~60岁）
     */
    private Long incre4;
    /**
     * 新增人数（41~50岁）
     */
    private Long incre5;
    /**
     * 新增人数（40岁及以下）
     */
    private Long incre6;
    /**
     * 新增人数（未知）
     */
    private Long incre7;


    /**
     * 累积人数（80岁以上）
     */
    private Long accu1;
    /**
     * 累积人数（71~80岁）
     */
    private Long accu2;
    /**
     * 累积人数（61~70岁）
     */
    private Long accu3;
    /**
     * 累积人数（51~60岁）
     */
    private Long accu4;
    /**
     * 累积人数（41~50岁）
     */
    private Long accu5;
    /**
     * 累积人数（40岁及以下）
     */
    private Long accu6;
    /**
     * 累积人数（未知）
     */
    private Long accu7;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getIncre1() {
        return incre1;
    }

    public void setIncre1(Long incre1) {
        this.incre1 = incre1;
    }

    public Long getIncre2() {
        return incre2;
    }

    public void setIncre2(Long incre2) {
        this.incre2 = incre2;
    }

    public Long getIncre3() {
        return incre3;
    }

    public void setIncre3(Long incre3) {
        this.incre3 = incre3;
    }

    public Long getIncre4() {
        return incre4;
    }

    public void setIncre4(Long incre4) {
        this.incre4 = incre4;
    }

    public Long getIncre5() {
        return incre5;
    }

    public void setIncre5(Long incre5) {
        this.incre5 = incre5;
    }

    public Long getIncre6() {
        return incre6;
    }

    public void setIncre6(Long incre6) {
        this.incre6 = incre6;
    }

    public Long getIncre7() {
        return incre7;
    }

    public void setIncre7(Long incre7) {
        this.incre7 = incre7;
    }

    public Long getAccu1() {
        return accu1;
    }

    public void setAccu1(Long accu1) {
        this.accu1 = accu1;
    }

    public Long getAccu2() {
        return accu2;
    }

    public void setAccu2(Long accu2) {
        this.accu2 = accu2;
    }

    public Long getAccu3() {
        return accu3;
    }

    public void setAccu3(Long accu3) {
        this.accu3 = accu3;
    }

    public Long getAccu4() {
        return accu4;
    }

    public void setAccu4(Long accu4) {
        this.accu4 = accu4;
    }

    public Long getAccu5() {
        return accu5;
    }

    public void setAccu5(Long accu5) {
        this.accu5 = accu5;
    }

    public Long getAccu6() {
        return accu6;
    }

    public void setAccu6(Long accu6) {
        this.accu6 = accu6;
    }

    public Long getAccu7() {
        return accu7;
    }

    public void setAccu7(Long accu7) {
        this.accu7 = accu7;
    }
}
