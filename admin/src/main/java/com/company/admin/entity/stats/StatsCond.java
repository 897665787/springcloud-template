package com.company.admin.entity.stats;

import com.company.admin.entity.base.BaseModel;

import java.util.List;

/**
 * @Author: kunye
 * @Date: 2018/9/21 10:43
 */
public class StatsCond extends BaseModel {

    /**
     * 统计频度，1-日，2-月
     */
    private Integer freq;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    private String dateFormat;

    private List<String> labels;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Integer getFreq() {
        return freq;
    }

    public void setFreq(Integer freq) {
        this.freq = freq;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
