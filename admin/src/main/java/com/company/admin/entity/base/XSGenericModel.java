package com.company.admin.entity.base;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 模型基础类
 * Created by xuxiaowei on 2017/10/20.
 */
public class XSGenericModel {
	@TableField(exist = false)
    protected Long offset;
	@TableField(exist = false)
    protected Long limit;
	@TableField(exist = false)
    protected Long page;
	@TableField(exist = false)
    protected Map<String, String> sort;
	@TableField(exist = false)
    protected Map<String, String> dynamic;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;

    public void setDefaultSort(String field, String order) {
        if (this.sort == null) {
            this.sort = new HashMap<>();
            this.sort.put(field, order);
        }
    }

    public void setDefaultSort(String[] fields, String[] orders) {
        if (this.sort == null && fields.length == orders.length) {
            this.sort = new LinkedHashMap<>();
            for (int i = 0; i < fields.length; ++i) {
                if (!StringUtils.isBlank(fields[i]) && !StringUtils.isBlank(orders[i])) {
                    this.sort.put(fields[i], orders[i]);
                }
            }
        }
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page > 0 ? page : 1;
        if (limit == null || limit <= 0) {
            limit = 10L;
        }
        this.offset = (this.page - 1) * this.limit;
    }

    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    public Map<String, String> getDynamic() {
        return dynamic;
    }

    public void setDynamic(Map<String, String> dynamic) {
        this.dynamic = dynamic;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
