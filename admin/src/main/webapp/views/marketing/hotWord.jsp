<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by JQ棣 on 2018/11/07.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>热搜词</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="marketing_hotWord"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">热搜词</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">编号：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="id" type="number" class="form-control" value="${search.id}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">名称：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="dynamic[name]" type="text" class="form-control" placeholder="模糊查询" value="${search.dynamic.name}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">状态：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="status" class="form-control" data-value="${search.status}">
								<option value="">全部</option>
								<xs:descriptionOptions clazz="marketing.HotWord" property="status"/>
							</select>
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">创建时间始：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input type="text" name="dynamic[createTimeBegin]" class="form-control datepicker" readonly value="${search.dynamic.createTimeBegin}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">创建时间末：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input type="text" name="dynamic[createTimeEnd]" class="form-control datepicker" readonly value="${search.dynamic.createTimeEnd}">
						</div>
                    </div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_create')})">
                                <a href="#" onclick="showCreateModal();return false"
                                   class="btn btn-success pull-left">新增</a>
                            </sec:authorize>
                            <input class="btn btn-info pull-right" value="搜索" type="submit">
                            <input class="btn btn-default pull-right  m-r-sm" value="重置" type="button"
                                   onclick="$('#searchForm').xsClean()">
                        </div>
                    </div>
                </form>
                <div class="panel panel-default m-b-none">
                    <table class="table text-center table-bordered table-striped m-b-none">
                        <thead>
                            <tr>
								<th>编号</th>
								<th>名称</th>
								<th>状态</th>
								<th>顺序</th>
								<th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:if test="${pageModel.list.size() eq 0}">
                            <tr>
                                <td colspan="6">无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${pageModel.list}" var="item">
                            <tr>
								<td>${item.id}</td>
								<td>${item.name}</td>
								<td><xs:descriptionDesc clazz="marketing.HotWord" property="status" value="${item.status}"/></td>
								<td>${item.seq}</td>
                                <td>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_update')})">
                                        <c:if test="${item.status eq 0}">
                                            <a href="#" class="btn btn-success btn-xs"
                                               onclick="updateListItemStatus('${item.id}',1);return false">
                                                启用
                                            </a>
                                        </c:if>
                                        <c:if test="${item.status eq 1}">
                                            <a href="#" class="btn btn-danger btn-xs"
                                               onclick="updateListItemStatus('${item.id}',0);return false">
                                                关闭
                                            </a>
                                        </c:if>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_get')})">
                                        <a href="#" onclick="getListItem('${item.id}');return false;"
                                           class="btn btn-success btn-xs">
                                            详情
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_update')})">
                                        <a href="#" onclick="updateListItem('${item.id}');return false;"
                                           class="btn btn-info btn-xs">
                                            编辑
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_delete')})">
                                        <a href="#" class="btn btn-danger btn-xs"
                                           onclick="deleteListItem('${item.id}');return false">
                                            删除
                                        </a>
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
</div>

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_create')})">
    <div class="modal fade" id="createModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">新增</h4>
                </div>
                <form name="createForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">名称：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="50" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">状态：</label>
							</div>
							<div class="col-xs-9">
								<select name="status" class="form-control" data-ignore="true" value="0" data-value="0">
									<xs:descriptionOptions clazz="marketing.HotWord" property="status"/>
								</select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">顺序：</label>
							</div>
							<div class="col-xs-9">
								<input name="seq" type="number" step="1" class="form-control" value="0" data-value="0">
							</div>
						</div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button id="createSubmit" type="submit" class="btn btn-success">确定</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        var $createForm = $("form[name=createForm]");
        var $createSubmit = $("#createSubmit");
        var createValidator = $createForm.validate({
            onfocusout: function(element) { $(element).valid(); },
			rules: {
				name: {
					required: true,
					notEmpty: true,
					maxlength: 50
				},
				status: {
					required: true
				},
				seq: {
					required: true,
					number: true,
				}
			},
			messages: {
				name: {
					required: "名称不能为空",
					notEmpty: "名称不能为空",
					maxlength: "名称最多50个字"
				},
				status: {
					required: "状态不能为空"
				},
				seq: {
					required: "顺序不能为空",
					number: "顺序必须为数字",
				}
			},
            submitHandler: function () {
                $createSubmit.attr("disabled", true);
                doPost("<%=request.getContextPath()%>/admin/marketing/hotWord/save",
                    $createForm.xsJson(),
                    function (data) {
                        $createSubmit.attr("disabled", false);
                        if (data.status) {
                            $("#createModel").modal("hide");
                            bootoast({message: "新增成功！"});
                            setTimeout(function () {
                                window.location.reload(true);
                            }, 680);
                        } else {
                            alert(data.message);
                        }
                    }, function (XMLHttpRequest, textStatus) {
                        $createSubmit.attr("disabled", false);
                        alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                    });
            }
        });

        function showCreateModal() {
            $("#createModel").modal("show");
        }
        $(function () {
            $("#createModel").on('hide.bs.modal', function () {
                $createForm.xsClean();
                createValidator.resetForm();
            })
        });
    </script>
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_get')})">
    <div class="modal fade" id="getModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">详情</h4>
                </div>
                <form name="getForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
                        <input type="hidden" name="id">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">名称：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="50" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">状态：</label>
							</div>
							<div class="col-xs-9">
								<select name="status" class="form-control" disabled="disabled">
									<xs:descriptionOptions clazz="marketing.HotWord" property="status"/>
								</select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">顺序：</label>
							</div>
							<div class="col-xs-9">
								<input name="seq" type="number" step="1" class="form-control" readonly>
							</div>
						</div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" data-dismiss="modal">确定</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        var $getForm = $("form[name=getForm]");

        function getListItem(id) {
            doPost("<%=request.getContextPath()%>/admin/marketing/hotWord/get", {id: id}, function (data) {
                if (data.status) {
                    $getForm.xsSetForm(data.data);

                    $("#getModel").modal("show");
                } else {
                    alert(data.message);
                }
            });
        }

        $(function () {
            $("#getModel").on('hide.bs.modal', function () {
                $getForm.xsClean();
            })
        });
    </script>
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_update')})">
    <div class="modal fade" id="updateModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">编辑</h4>
                </div>
                <form name="updateForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
                        <input type="hidden" name="id">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">名称：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="50" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">状态：</label>
							</div>
							<div class="col-xs-9">
								<select name="status" class="form-control">
                                    <xs:descriptionOptions clazz="marketing.HotWord" property="status"/>
                                </select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">顺序：</label>
							</div>
							<div class="col-xs-9">
								<input name="seq" type="number" step="1" class="form-control">
							</div>
						</div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button id="updateSubmit" type="submit" class="btn btn-success">确定</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        var $updateForm = $("form[name=updateForm]");
        var $updateSubmit = $("#updateSubmit");
        var updateValidator = $updateForm.validate({
            onfocusout: function(element) { $(element).valid(); },
			rules: {
				name: {
					required: true,
					notEmpty: true,
					maxlength: 50
				},
				status: {
					required: true
				},
				seq: {
					required: true,
					number: true,
				}
			},
			messages: {
				name: {
					required: "名称不能为空",
					notEmpty: "名称不能为空",
					maxlength: "名称最多50个字"
				},
				status: {
					required: "状态不能为空"
				},
				seq: {
					required: "顺序不能为空",
					number: "顺序必须为数字"
				}
			},
            submitHandler: function () {
                $updateSubmit.attr("disabled", true);
                var params = $updateForm.xsJson();

                doPost("<%=request.getContextPath()%>/admin/marketing/hotWord/update", params,
                    function (data) {
                        $updateSubmit.attr("disabled", false);
                        if (data.status) {
                            $("#updateModel").modal("hide");
                            bootoast({message: "更新成功！"});
                            setTimeout(function () {
                                window.location.reload(true);
                            }, 680);
                        } else {
                            alert(data.message);
                        }
                    }, function (XMLHttpRequest, textStatus) {
                        $updateSubmit.attr("disabled", false);
                        alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                    });
            }
        });

        function updateListItem(id) {
            doPost("<%=request.getContextPath()%>/admin/marketing/hotWord/get", {id: id}, function (data) {
                if (data.status) {
                    $updateForm.xsSetForm(data.data);

                    $("#updateModel").modal("show");
                } else {
                    alert(data.message);
                }
            });
        }

        $(function () {
            $("#updateModel").on('hide.bs.modal', function () {
                $updateForm.xsClean();
                updateValidator.resetForm();
            })
        });


        function updateListItemStatus(id,status) {
            doPost("<%=request.getContextPath()%>/admin/marketing/hotWord/status/update",
                {
                    id: id,
                    status: status
                },
                function (data) {
                    if (data.status) {
                        setTimeout(function () {
                            bootoast({message: "更新成功！"});
                            window.location.reload(true);
                        }, 380);
                    } else {
                        alert(data.message);
                    }
                });
        }
    </script>
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord_delete')})">
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteListItem(id) {
            showDeleteModel(null, function () {
                doPost("<%=request.getContextPath()%>/admin/marketing/hotWord/remove", {id: id}, function (data) {
                    if (data.status) {
                        bootoast({message: "删除成功！"});
                        setTimeout(function () {
                            window.location.reload(true);
                        }, 680);
                    } else {
                        alert(data.message);
                    }
                })
            })
        }
    </script>
</sec:authorize>
</body>
</html>

