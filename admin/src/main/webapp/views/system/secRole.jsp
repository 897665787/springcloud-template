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
    <title>角色管理</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/ztree.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<%-- index参数的设置要在content_nav.jsp包含之前。jsp:include不可用，具体原因：未解之谜 --%>
<c:set var="index" value="system_role"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3">角色管理</h1>
        </div>
        <div class="wrapper-md">
            <form class="form-horizontal" id="searchForm">
                <div class="form-group">
                    <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        <label class="control-label">名称：</label>
                    </div>
                    <div class="col-xs-8 col-md-4 col-lg-3 m-b-md">
                        <input class="form-control" type="text" name="dynamic[name]" value="${search.dynamic.name}"
                               placeholder="模糊查询"/>
                    </div>
                    <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        <label class="control-label">状态：</label>
                    </div>
                    <div class="col-xs-8 col-md-4 col-lg-3 m-b-md">
                        <select name="status" class="form-control" data-value="${search.status}">
                            <option value="">全部</option>
                            <xs:dictOptions key="nof"/>
                        </select>
                    </div>
                </div>
                <div class="form-group m-t-n-md">
                    <div class="col-xs-12">
                        <sec:authorize access="hasAnyRole(${xs:getPermissions('system_role_create')})">
                        <button class="btn btn-success" type="button" data-toggle="modal" data-target="#create">
                            新增
                        </button>
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
                        <th>名称</th>
                        <th>描述</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${pageModel.list.size() eq 0}">
                        <tr>
                            <td colspan="4">无数据</td>
                        </tr>
                    </c:if>
                    <c:forEach items="${pageModel.list}" var="role">
                        <tr>
                            <td>${role.name}</td>
                            <td>${role.desc}</td>
                            <td><xs:dictDesc key="nof" value="${role.status}"/></td>
                            <td>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_role_authorize')})">
                                    <button class="btn btn-primary btn-xs" onclick="auth('${role.id}')">授权</button>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_role_update')})">
                                    <button class="btn btn-info btn-xs" onclick="edit('${role.id}')">编辑</button>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_role_delete')})">
                                    <button class="btn btn-danger btn-xs" onclick="del('${role.id}')">删除</button>
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
    var $searchStatus = $("#searchStatus");

    $searchStatus.val("${status}");

    function resetSearch() {
        $searchForm.find("input[type='text']").each(function () {
            $(this).val("");
        });
        $searchForm.find("input[type='number']").each(function () {
            $(this).val("");
        });
        $searchStatus.val("");
    }

</script>
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_role_create')})">
    <%--新增角色--%>
    <div class="modal fade" id="create" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">新增角色</h4>
                </div>
                <div class="modal-body">
                    <form name="createForm" class="form-horizontal">
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
                                <label class="control-label">描述</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="desc"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">状态</label>
                            </div>
                            <div class="col-xs-9">
                                <select name="status" class="form-control">
                                    <xs:dictOptions key="nof" value="1"/>
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

        var createValidate = $createForm.validate({
            rules: {
                name: {
                    required: true,
                    notEmpty: true
                },
                status: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "名称不能为空",
                    notEmpty: "名称不能为空"
                },
                status: {
                    required: "请选择状态"
                }
            },
            submitHandler: function (form) {
                doPost("<%=request.getContextPath()%>/admin/security/secRole/save", $(form).serialize(), function (data) {
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

    </script>
</sec:authorize>
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_role_update')})">
    <%--编辑角色--%>
    <div class="modal fade" id="edit" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">编辑角色</h4>
                </div>
                <div class="modal-body">
                    <form name="editForm" class="form-horizontal">
                        <input type="hidden" name="id">
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
                                <label class="control-label">描述</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="desc"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">状态</label>
                            </div>
                            <div class="col-xs-9">
                                <select name="status" class="form-control">
                                    <xs:dictOptions key="nof"/>
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

        var $editForm = $("form[name='editForm']");
        var editValidate = $editForm.validate({
            rules: {
                name: {
                    required: true,
                    notEmpty: true
                },
                status: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "名称不能为空",
                    notEmpty: "名称不能为空"
                },
                status: {
                    required: "请选择状态"
                }
            },
            submitHandler: function (form) {
                doPost("<%=request.getContextPath()%>/admin/security/secRole/update", $(form).serialize(), function (data) {
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
            $("#edit").find(".text-danger").removeClass("text-danger");
        });

        function edit(id) {
            doPost("<%=request.getContextPath()%>/admin/security/secRole/get", {id: id}, function (data) {
                if (data.status) {
                    var _data = data.data;
                    $editForm.find("input[name='id']").val(_data.id);
                    $editForm.find("input[name='name']").val(_data.name);
                    $editForm.find("input[name='desc']").val(_data.desc);
                    $editForm.find("select[name='status']").val(_data.status);
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
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_role_delete')})">
    <%--删除角色--%>
    <div class="modal fade" id="del" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">删除角色</h4>
                </div>
                <div class="modal-body">
                    <h4 class="text-danger">确认删除该角色？</h4>
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
            doPost("<%=request.getContextPath()%>/admin/security/secRole/remove", {id: deleteId}, function (data) {
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
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_role_authorize')})">
    <%--角色授权--%>
    <div class="modal fade" id="auth" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">角色授权</h4>
                </div>
                <div class="modal-body pos-rlt" style="height: 500px;">
                    <ul id="tree" class="ztree" style="overflow:auto;max-height: 100%"></ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-success" onclick="submitAuth()">确定</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        var roleId = "";
        var zTreeObj,
            setting = {
                data: {
                    key: {
                        children: "children"
                    }
                },
                check: {
                    enable: true,
                    chkboxType: {
                        "Y": "ps",
                        "N": "s"
                    }
                },
                async: {
                    enable: true,
                    url: asyncUrl,
                    dataFilter: asyncFilter
                },
                callback: {
                    beforeAsync: beforeAsync
                }
            },
            zTreeNodes = [];

        $(document).ready(function () {
            zTreeObj = $.fn.zTree.init($("#tree"), setting, zTreeNodes);
        });

        function beforeAsync(treeId, treeNode) {
            if (treeNode === undefined)
                return false;
        }

        function asyncUrl() {
            return "<%=request.getContextPath()%>/admin/security/secRole/secResource/tree?id=" + roleId;
        }

        function asyncFilter(treeId, parentNode, responseData) {
            if (responseData.status) {
                return responseData.data;
            } else {
                alert(responsedata.message);
                return [];
            }
        }

        function auth(id) {
            roleId = id;
            zTreeObj.reAsyncChildNodes(null, "refresh");
            $("#auth").modal("show");
        }

        function submitAuth() {
            var selectedNodes = zTreeObj.getCheckedNodes();
            var resources = [];
            $.each(selectedNodes, function (index, node) {
                resources.push(node.id);
            });
            doPost("<%=request.getContextPath()%>/admin/security/secRole/authorize", {
                id: roleId,
                resourceIds: resources + ""
            }, function (data) {
                if (data.status) {
                    bootoast({message: "授权成功！", timeout: 1});
                } else {
                    alert(data.message);
                }
                $('#auth').modal('hide');
            });
        }
    </script>
</sec:authorize>
</body>
</html>
