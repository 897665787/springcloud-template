package com.company.admin.entity.security;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

/**
 * 从xml文件读取的系统资源
 * Created by xuxiaowei on 2018/6/6.
 */
public class SecResourceFromXml {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(min = 1, max = 255, message = "名称长度为1-255个字符")
    private String name;

    /**
     * 键
     */
    @NotBlank(message = "键不能为空")
    @Length(min = 1, max = 191, message = "键长度为1-191个字符")
    private String key;

    /**
     * 父级id
     */
    private Long pid;

    /**
     * 父级键
     */
    private String pkey;

    /**
     * 类型，0-菜单，1-功能，2-url
     */
    private Integer type;

    /**
     * url
     */
    @Length(max = 255, message = "url长度最多为255个字符")
    private String url;

    /**
     * 请求方法
     */
    @Length(max = 255, message = "方法长度最多为255个字符")
    private String method;

    /**
     * 顺序
     */
    private Long seq = 0L;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    @Length(min = 1, max = 255, message = "描述长度为1-255个字符")
    private String desc;

    /**
     * 可分配，0-否，1-是
     */
    private Integer assign = 1;

    /**
     * 记录日志，0-否，1-是
     */
    private Integer log = 0;

    /**
     * 资源列表
     */
    public final static List<SecResourceFromXml> secResourceList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getAssign() {
        return assign;
    }

    public void setAssign(Integer assign) {
        this.assign = assign;
    }

    public Integer getLog() {
        return log;
    }

    public void setLog(Integer log) {
        this.log = log;
    }

    public void addMenu(SecResourceFromXml secResourceFromXml) {
        dealAndAddResource(secResourceFromXml, 0);
    }

    public void addFunction(SecResourceFromXml secResourceFromXml) {
        dealAndAddResource(secResourceFromXml, 1);
    }

    public void addApi(SecResourceFromXml secResourceFromXml) {
        dealAndAddResource(secResourceFromXml, 2);
    }

    /**
     * @param type 资源类型，0-菜单，1-功能，2-url
     */
    private void dealAndAddResource(SecResourceFromXml secResourceFromXml, Integer type) {
        secResourceFromXml.setType(type);
        //空字符串或者非整数时Digester会给id赋值0
        if (Long.valueOf(0).equals(secResourceFromXml.getId())) {
            secResourceFromXml.setId(null);
        }
        //若未显式设置pkey，则直接取父级标签的key作为pkey
        if (secResourceFromXml.getPkey() == null) {
            secResourceFromXml.setPkey(this.key);
        }
        //desc字段必填，因为该字段的内容更加友好；若打印日志则不处理desc字段，而是等待后面的数据格式校验再处理
        if (secResourceFromXml.getLog().equals(0) && StringUtils.isBlank(secResourceFromXml.getDesc())) {
            secResourceFromXml.setDesc(secResourceFromXml.getName());
        }
        SecResourceFromXml.secResourceList.add(secResourceFromXml);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", pid=" + pid +
                ", pkey=" + pkey +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", seq=" + seq +
                ", desc='" + desc + '\'' +
                ", assign=" + assign +
                ", log=" + log +
                '}';
    }
}
