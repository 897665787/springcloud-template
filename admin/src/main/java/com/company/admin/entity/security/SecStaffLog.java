package com.company.admin.entity.security;

import java.util.Date;

import com.company.admin.entity.base.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 系统用户日志
 * Created by xuxiaowei on 2017/11/2.
 */
public class SecStaffLog extends BaseModel {

    /**
     * id
     */
    private Long id;

    /**
     * 系统用户
     */
    private SecStaff staff;

    /**
     * ip
     */
    private String ip;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 操作
     */
    private String operation;

    /**
     * 状态，0失败，1成功
     */
    private Integer status;

    /**
     * 参数
     */
    private String parameters;

    /**
     * 结果
     */
    private String result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SecStaff getStaff() {
        return staff;
    }

    public void setStaff(SecStaff staff) {
        this.staff = staff;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
