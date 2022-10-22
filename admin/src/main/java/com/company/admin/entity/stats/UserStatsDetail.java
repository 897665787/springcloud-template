package com.company.admin.entity.stats;

import com.company.admin.entity.base.BaseModel;

/**
 * @Author: kunye
 * @Date: 2018/9/21 10:43
 */
public class UserStatsDetail extends BaseModel {

    /**
     * 日期
     */
    private String label;

    /**
     * 分组名
     */
    private String gname;

    /**
     * 新增人数
     */
    private Long incre;

    /**
     * 累积人数
     */
    private Long accu;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getIncre() {
        return incre;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public void setIncre(Long incre) {
        this.incre = incre;
    }

    public Long getAccu() {
        return accu;
    }

    public void setAccu(Long accu) {
        this.accu = accu;
    }
}
