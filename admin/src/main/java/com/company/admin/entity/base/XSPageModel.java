package com.company.admin.entity.base;

import java.util.List;

/**
 * 分页模型
 * Created by xuxiaowei on 2017/10/20.
 */
public class XSPageModel<T> {

    private List<T> list;

    private Long total;

    public XSPageModel() {}

    private XSPageModel(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public static <T> XSPageModel<T> build(List<T> list, Long total) {
        return new XSPageModel<T>(list, total);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
