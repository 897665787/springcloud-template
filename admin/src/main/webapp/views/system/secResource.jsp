<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: JQ棣
  Date: 2017-05-02
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>资源管理</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/ztree.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<%-- index参数的设置要在content_nav.jsp包含之前。jsp:include不可用，具体原因：未解之谜 --%>
<c:set var="index" value="system_resource"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3">资源管理</h1>
        </div>
        <div class="wrapper-md">
            <form class="form-horizontal" id="searchForm">
                <div class="form-group">
                    <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                        <label class="control-label">名称：</label>
                    </div>
                    <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        <input name="dynamic[name]" type="text" class="form-control" placeholder="模糊查询"
                               value="${search.dynamic.name}">
                    </div>
                    <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                        <label class="control-label">键：</label>
                    </div>
                    <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        <input name="dynamic[key]" type="text" class="form-control" placeholder="模糊查询"
                               value="${search.dynamic.key}">
                    </div>
                    <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                        <label class="control-label">url：</label>
                    </div>
                    <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        <input name="dynamic[url]" type="text" class="form-control" placeholder="模糊查询"
                               value="${search.dynamic.url}">
                    </div>
                    <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                        <label class="control-label">父级：</label>
                    </div>
                    <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        <input id="searchPid" type="hidden" name="parent.id" value="${search.parent.id}">
                        <input id="searchPName" class="form-control" type="text" name="parent.name" readonly
                               onclick="openTreeView(P_TYPE_SEARCH)" value="${search.parent.name}"/>
                    </div>
                    <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                        <label class="control-label">类型：</label>
                    </div>
                    <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        <select name="type" class="form-control" data-value="${search.type}">
                            <option value="">全部</option>
                            <xs:dictOptions key="secResourceType"/>
                        </select>
                    </div>
                </div>
                <div class="form-group m-t-n-md">
                    <div class="col-xs-12">
                        <sec:authorize access="hasAnyRole(${xs:getPermissions('system_resource_create')})">
                            <button class="btn btn-success" type="button" data-toggle="modal" data-target="#create">
                                新增
                            </button>
                            <input class="btn btn-primary" value="重载" type="button" onclick="reloadResource()">
                        </sec:authorize>
                        <input class="btn btn-default pull-right" value="重置" type="button" onclick="$('#searchForm').xsClean()">
                        <input class="btn btn-info pull-right m-r-sm" value="搜索" type="submit">
                    </div>
                </div>
            </form>

            <div class="panel panel-default m-b-none">
                <table class="table text-center table-bordered table-striped m-b-none">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>名称</th>
                        <th>键</th>
                        <th>类型</th>
                        <th>可分配</th>
                        <th>记录日志</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${pageModel.list.size() eq 0}">
                        <tr>
                            <td colspan="7">无数据</td>
                        </tr>
                    </c:if>
                    <c:forEach items="${pageModel.list}" var="resource">
                        <tr>
                            <td>${resource.id}</td>
                            <td>${resource.name}</td>
                            <td>${resource.key}</td>
                            <td><xs:dictDesc key="secResourceType" value="${resource.type}"/></td>
                            <td><xs:dictDesc key="yesno" value="${resource.assign}"/></td>
                            <td><xs:dictDesc key="yesno" value="${resource.log}"/></td>
                            <td>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_resource_update')})">
                                    <button class="btn btn-info btn-xs" onclick="edit('${resource.id}')">编辑</button>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_resource_delete')})">
                                    <button class="btn btn-danger btn-xs" onclick="del('${resource.id}')">删除
                                    </button>
                                </sec:authorize>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <xs:pagination pageModel="${pageModel}"/>
        </div>
    </div>
</div>
<script>
    var $searchForm = $("#searchForm");
    var $searchType = $("#searchType");

    $searchType.val("${type}");

    function resetSearch() {
        $searchForm.find("input[type='text']").each(function () {
            $(this).val("");
        });
        $searchForm.find("input[type='number']").each(function () {
            $(this).val("");
        });
        $searchType.val("");
    }
    <%--<sec:authorize access="hasAnyRole(${sessionScope.sec_op.system_resource_create})">--%>
    var P_TYPE_CREATE = 0;
    <%--</sec:authorize>--%>
    <%--<sec:authorize access="hasAnyRole(${sessionScope.sec_op.system_resource_update})">--%>
    var P_TYPE_EDIT = 1;
    <%--</sec:authorize>--%>
    <%--<sec:authorize access="hasAnyRole(${sessionScope.sec_op.system_resource_update})">--%>
    var P_TYPE_SEARCH = 2;
    <%--</sec:authorize>--%>

    var treeType;
    function openTreeView(type) {
        treeType = type;
        $('#resourceTree').modal('show');
    }
</script>
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_resource_create')})">
    <%--新增资源--%>
    <div class="modal fade" id="create" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">新增资源</h4>
                </div>
                <div class="modal-body">
                    <form name="createForm" class="form-horizontal">
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">父级</label>
                            </div>
                            <div class="col-xs-9">
                                <input id="createPid" type="hidden" name="parent.id">
                                <input id="createPName" class="form-control" type="text" name="parent.name" readonly
                                       onclick="openTreeView(P_TYPE_CREATE)"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">键</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="key"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">名称</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="name"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">类型</label>
                            </div>
                            <div class="col-xs-9">
                                <select id="createType" name="type" class="form-control">
                                    <xs:dictOptions key="secResourceType"/>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row" id="createURL" style="display: none;">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">URL</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="url"/>
                            </div>
                        </div>
                        <div class="form-group row" id="createMethod" style="display: none;">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">请求方法</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="method"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">顺序</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="seq" type="number" min="0" step="1" class="form-control" value="0"
                                       data-value="0">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">描述</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="desc"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">可分配</label>
                            </div>
                            <div class="col-xs-9">
                                <select name="assign" class="form-control">
                                    <xs:dictOptions key="yesno" value="1"/>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">记录日志</label>
                            </div>
                            <div class="col-xs-9">
                                <select name="log" class="form-control">
                                    <xs:dictOptions key="yesno"/>
                                </select>
                            </div>
                        </div>
                    </form>
                    <div class="clearfix"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-success" onclick="submitCreateForm()">确定</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        var $createForm = $("form[name='createForm']");
        var $createType = $("#createType");
        var $createUrl = $("#createURL");
        var $createMethod = $("#createMethod");

        $createType.on("change", function () {
            if ($createType.val() == 2) {
                $createUrl.css("display", "block");
                $createMethod.css("display", "block");
            } else {
                $createUrl.css("display", "none");
                $createUrl.val(" ");
                $createMethod.css("display", "none");
                $createMethod.val(" ");
            }
        });
        var createValidate = $createForm.validate({
            rules: {
                "parent.name": {
                    required: true
                },
                key: {
                    required: true,
                    notEmpty: true
                },
                name: {
                    required: true,
                    notEmpty: true
                },
                type: {
                    required: true
                },
                url: {
                    required: true,
                    notEmpty: true
                },
                method: {
                    required: true,
                    notEmpty: true
                },
                seq: {
                    required: true,
                    digits: true
                },
                desc: {
                    required: true,
                    notEmpty: true
                }
            },
            messages: {
                "parent.name": {
                    required: "请选择父级"
                },
                key: {
                    required: "键不能为空",
                    notEmpty: "键不能为空"
                },
                name: {
                    required: "名称不能为空",
                    notEmpty: "名称不能为空"
                },
                type: {
                    required: "请选择类型"
                },
                url: {
                    required: "URL不能为空",
                    notEmpty: "URL不能为空"
                },
                method: {
                    required: "请求方法不能为空",
                    notEmpty: "请求方法不能为空"
                },
                seq: {
                    required: "顺序不能为空",
                    digits: "请填入数字"
                },
                desc: {
                    required: "描述不能为空",
                    notEmpty: "描述不能为空"
                }
            },
            submitHandler: function (form) {
                doPost("<%=request.getContextPath()%>/admin/security/secResource/save", $(form).serialize(), function (data) {
                    if (data.status) {
                        bootoast({message: "新增成功！"});
                        window.location.reload(true);
                    } else {
                        alert(data.message);
                    }
                });
            }
        });

        $('#create').on('hide.bs.modal', function (e) {
            createValidate.resetForm();
            $createForm[0].reset();
        });

        function submitCreateForm() {
            $createForm.submit();
        }

        function reloadResource() {
            doPost("<%=request.getContextPath()%>/admin/security/secResource/reload", {}, function (data) {
                if (data.status) {
                    bootoast({message: "重载成功！",timeout:2});
                } else {
                    alert(data.message);
                }
            });
        }
    </script>
</sec:authorize>
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_resource_update')})">
    <%--编辑资源--%>
    <div class="modal fade" id="edit" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">编辑资源</h4>
                </div>
                <div class="modal-body">
                    <form name="editForm" class="form-horizontal">
                        <input type="hidden" name="id">
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">父级</label>
                            </div>
                            <div class="col-xs-9">
                                <input id="editPid" type="hidden" name="parent.id">
                                <input id="editPName" class="form-control" type="text" name="parent.name" readonly
                                       onclick="openTreeView(P_TYPE_EDIT)"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">键</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="key"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">名称</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="name"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">类型</label>
                            </div>
                            <div class="col-xs-9">
                                <select id="editType" name="type" class="form-control">
                                    <xs:dictOptions key="secResourceType"/>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row" id="editURL" style="display: none;">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">URL</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="url"/>
                            </div>
                        </div>
                        <div class="form-group row" id="editMethod" style="display: none;">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">请求方法</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="method"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">顺序</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="seq" type="number" min="0" step="1" class="form-control" value="0"
                                       data-value="0">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">描述</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="desc"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">可分配</label>
                            </div>
                            <div class="col-xs-9">
                                <select name="assign" class="form-control">
                                    <xs:dictOptions key="yesno" value="1"/>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">记录日志</label>
                            </div>
                            <div class="col-xs-9">
                                <select name="log" class="form-control">
                                    <xs:dictOptions key="yesno"/>
                                </select>
                            </div>
                        </div>
                    </form>
                    <div class="clearfix"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-info" onclick="submitEditForm()">确定</button>
                </div>
            </div>
        </div>
    </div>
    <script>

        var $editType = $("#editType");
        var $editUrl = $("#editURL");
        var $editMethod = $("#editMethod");

        $editType.on("change", function () {
            if ($editType.val() == 2) {
                $editUrl.css("display", "block");
                $editMethod.css("display", "block");
            } else {
                $editUrl.css("display", "none");
                $editUrl.val(" ");
                $editMethod.css("display", "none");
                $editMethod.val(" ");
            }
        });

        var $editForm = $("form[name='editForm']");
        var editValidate = $editForm.validate({
            rules: {
                "parent.name": {
                    required: true
                },
                key: {
                    required: true,
                    notEmpty: true
                },
                name: {
                    required: true,
                    notEmpty: true
                },
                type: {
                    required: true
                },
                url: {
                    required: true,
                    notEmpty: true
                },
                method: {
                    required: true,
                    notEmpty: true
                },
                seq: {
                    required: true,
                    digits: true
                },
                desc: {
                    required: true,
                    notEmpty: true
                }
            },
            messages: {
                "parent.name": {
                    required: "请选择父级"
                },
                key: {
                    required: "键不能为空",
                    notEmpty: "键不能为空"
                },
                name: {
                    required: "名称不能为空",
                    notEmpty: "名称不能为空"
                },
                type: {
                    required: "请选择类型"
                },
                url: {
                    required: "URL不能为空",
                    notEmpty: "URL不能为空"
                },
                method: {
                    required: "请求方法不能为空",
                    notEmpty: "请求方法不能为空"
                },
                seq: {
                    required: "顺序不能为空",
                    digits: "请填入数字"
                },
                desc: {
                    required: "描述不能为空",
                    notEmpty: "描述不能为空"
                }
            },
            submitHandler: function (form) {
                doPost("<%=request.getContextPath()%>/admin/security/secResource/update", $(form).serialize(), function (data) {
                    if (data.status) {
                        bootoast({message: "更新成功！"});
                        window.location.reload(true);
                    } else {
                        alert(data.message);
                    }
                });
            }
        });

        $('#edit').on('hide.bs.modal', function (e) {
            editValidate.resetForm();
            $editType.trigger("change");
            $("#edit").find(".text-danger").removeClass("text-danger");
        });

        function edit(id) {
            doPost("<%=request.getContextPath()%>/admin/security/secResource/get", {id: id}, function (data) {
                if (data.status) {
                    var _data = data.data;
                    $editForm.find("input[name='id']").val(_data.id);
                    if (_data.parent != undefined) $editForm.find("input[name='parent.id']").val(_data.parent.id);
                    else $editForm.find("input[name='parent.id']").val(0);
                    if (_data.parent != undefined) $editForm.find("input[name='parent.name']").val(_data.parent.name);
                    else $editForm.find("input[name='parent.name']").val("顶级");
                    $editForm.find("input[name='key']").val(_data.key);
                    $editForm.find("input[name='name']").val(_data.name);
                    $editForm.find("input[name='seq']").val(_data.seq);
                    $editForm.find("input[name='desc']").val(_data.desc);
                    $editForm.find("select[name='assign']").val(_data.assign);
                    $editForm.find("select[name='log']").val(_data.log);
                    $editForm.find("select[name='type']").val(_data.type);
                    $editType.trigger("change");
                    if (_data.type == 2) {
                        $editForm.find("input[name='url']").val(_data.url);
                        $editForm.find("input[name='method']").val(_data.method);
                    }
                    $("#edit").modal('show');
                } else {
                    alert(data.message);
                }
            });
        }

        function submitEditForm() {
            $editForm.submit();
        }
    </script>
</sec:authorize>
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_resource_delete')})">
    <%--删除资源--%>
    <div class="modal fade" id="del" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">删除资源</h4>
                </div>
                <div class="modal-body">
                    <h4 class="text-danger">确认删除该资源？</h4>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-danger" onclick="submitDelete()">确定</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        var deleteId;
        function del(id) {
            deleteId = id;
            $("#del").modal('show');
        }
        function submitDelete() {
            doPost("<%=request.getContextPath()%>/admin/security/secResource/remove", {id: deleteId}, function (data) {
                if (data.status) {
                    bootoast({message: "删除成功！"});
                    window.location.reload(true);
                } else {
                    alert(data.message);
                }
            });
        }
    </script>
</sec:authorize>
    <div class="modal fade" id="resourceTree" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">父级</h4>
                </div>
                <div class="modal-body">
                    <ul id="tree" class="ztree" style="overflow:auto;height: 500px"></ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-success" onclick="selectConfirm()">确定</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        var zTreeObj,
            setting = {
                data: {
                    key: {
                        children: "children"
                    }
                },
                view: {
                    selectedMulti: false
                }
            },
            zTreeNodes = [{"name": "顶级", "id": "0", "children":${resourceTree eq null ? "[]":resourceTree}}];

        $(document).ready(function () {
            zTreeObj = $.fn.zTree.init($("#tree"), setting, zTreeNodes);

        });

        function selectConfirm() {
            var selectedNode = zTreeObj.getSelectedNodes()[0];
            if (treeType === P_TYPE_CREATE) {
                $("#createPid").val(selectedNode.id);
                $('#createPName').val(selectedNode.name);
                $createForm.validate().element($("#createPName"));
            } else if (treeType === P_TYPE_EDIT) {
                $("#editPid").val(selectedNode.id);
                $('#editPName').val(selectedNode.name);
                $editForm.validate().element($("#editPName"));
            } else {
                $("#searchPid").val(selectedNode.id);
                $('#searchPName').val(selectedNode.name);
            }
            $('#resourceTree').modal('hide');
        }

        $('#resourceTree').on('hide.bs.modal', function (e) {
            zTreeObj.selectNode(zTreeNodes[0]);
            zTreeObj.expandAll(false);
        });
    </script>
</body>
</html>
