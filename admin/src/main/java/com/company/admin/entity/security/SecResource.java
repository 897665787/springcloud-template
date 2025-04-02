package com.company.admin.entity.security;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.tree.XSTreeable;

/**
 * 系统资源
 * Created by JQ棣 on 2017/10/23.
 */
public class SecResource extends BaseModel implements XSTreeable<Long, SecResource> {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "名称长度为1-255个字符", groups = {Save.class, Update.class})
    private String name;

    /**
     * 键
     */
    @NotNull(message = "键不能为空", groups = Save.class)
    @Length(min = 1, max = 191, message = "键长度为1-191个字符", groups = {Save.class, Update.class})
    private String key;

    /**
     * 父级
     */
    private SecResource parent;

    /**
     * 类型，0菜单，1功能，2url
     */
    private Integer type;

    /**
     * url
     */
    @Length(max = 255, message = "url长度最多为255个字符", groups = {Save.class, Update.class})
    private String url;

    /**
     * 请求方法
     */
    @Length(max = 255, message = "方法长度最多为255个字符", groups = {Save.class, Update.class})
    private String method;

    /**
     * 顺序
     */
    @NotNull(message = "顺序不能为空", groups = Save.class)
    private Long seq;

    /**
     * 描述
     */
    @NotNull(message = "描述不能为空", groups = Save.class)
    @Length(min = 1, max = 255, message = "描述长度为1-255个字符", groups = {Save.class, Update.class})
    private String desc;

    /**
     * 可分配，0否，1是
     */
    private Integer assign;

    /**
     * 记录日志，0否，1是
     */
    private Integer log;

    /**
     * 拥有，0否，1是
     */
    private Integer checked;

    /**
     * 子级列表
     */
    private List<SecResource> children;

    /**
     * 资源列表
     */
    private List<SecResource> list;

    /**
     * 角色列表
     */
    private List<SecRole> roleList;

    /**
     * 逗号分隔的角色列表字符串
     */
    private String rolesStr;

    /**
     * 从xml文件读取的资源列表
     */
    private List<SecResource> secResourceListFromXml;

    public interface Save {}

    public interface Update {}

    public SecResource() {}

    public SecResource(Long id) {
        this.id = id;
    }

    public SecResource(String key) {
        this.key = key;
    }

    public SecResource(String url, String method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public Long getNodeId() {
        return id;
    }

    @Override
    public Long getParentNodeId() {
        if (parent != null) {
            return parent.getId();
        }
        return null;
    }

    @Override
    public void addChild(SecResource secResource) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(secResource);
    }

    @Override
    public List<SecResource> getChildren() {
        return children;
    }

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

    public SecResource getParent() {
        return parent;
    }

    public void setParent(SecResource parent) {
        this.parent = parent;
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

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public void setChildren(List<SecResource> children) {
        this.children = children;
    }

    public List<SecResource> getList() {
        return list;
    }

    public void setList(List<SecResource> list) {
        this.list = list;
    }

    public List<SecRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SecRole> roleList) {
        this.roleList = roleList;
    }

    public String getRolesStr() {
        return rolesStr;
    }

    public void setRolesStr(String rolesStr) {
        this.rolesStr = rolesStr;
    }

    public List<SecResource> getSecResourceListFromXml() {
        return secResourceListFromXml;
    }

    public void setSecResourceListFromXml(List<SecResource> secResourceListFromXml) {
        this.secResourceListFromXml = secResourceListFromXml;
    }

    public void addSecResourceFromXml(SecResource secResource) {
        if (this.secResourceListFromXml == null) {
            this.secResourceListFromXml = new ArrayList<>();
        }
        this.secResourceListFromXml.add(secResource);
    }
}
