<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by wjc on 2018/11/19.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>通讯录管理</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <jsp:include page="user_detail_nav.jsp">
            <jsp:param name="nav" value="8"/>
        </jsp:include>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                        <input name="userId" type="hidden" value="${search.userId}" data-value="${search.userId}">
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">姓名：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="dynamic[name]" type="text" class="form-control" placeholder="模糊查询" value="${search.dynamic.name}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">手机号码：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="dynamic[mobile]" type="text" class="form-control" placeholder="模糊查询" value="${search.dynamic.mobile}">
						</div>
                    </div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userContactBook_create')})">--%>
                                <a href="#" onclick="showCreateModal();return false"
                                   class="btn btn-success pull-left">新增</a>
                            <%--</sec:authorize>--%>
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
								<th>序号</th>
								<th>姓名</th>
								<th>手机号码</th>
								<th>备注</th>
								<th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:if test="${pageModel.list.size() eq 0}">
                            <tr>
                                <td colspan="5">无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${pageModel.list}" var="item">
                            <tr>
								<td>${item.number}</td>
								<td>${item.name}</td>
								<td>${item.mobile}</td>
								<td>${item.remark}</td>
                                <td>
                                    <%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userContactBook_update')})">--%>
                                        <a href="#" onclick="updateListItem('${item.id}');return false;"
                                           class="btn btn-info btn-xs">
                                            编辑
                                        </a>
                                    <%--</sec:authorize>--%>
                                    <%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userContactBook_delete')})">--%>
                                        <a href="#" class="btn btn-danger btn-xs"
                                           onclick="deleteListItem('${item.id}');return false">
                                            删除
                                        </a>
                                    <%--</sec:authorize>--%>
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

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userContactBook_create')})">--%>
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
                        <input type="hidden" name="userId" value="${user.id}" data-value="${user.id}">
                        <div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">姓名：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="100" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">手机号码：</label>
							</div>
							<div class="col-xs-9">
								<input name="mobile" type="text" maxlength="20" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label">备注：</label>
							</div>
							<div class="col-xs-9">
								<input name="remark" type="text" maxlength="255" class="form-control">
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
					maxlength: 100
				},
				mobile: {
					required: true,
					notEmpty: true,
					maxlength: 20
				},
				remark: {
					maxlength: 255
				}
			},
			messages: {
				name: {
					required: "姓名不能为空",
					notEmpty: "姓名不能为空",
					maxlength: "姓名最多100个字"
				},
				mobile: {
					required: "手机号码不能为空",
					notEmpty: "手机号码不能为空",
					maxlength: "手机号码最多20个字"
				},
				remark: {
					maxlength: "备注最多255个字"
				}
			},
            submitHandler: function () {
                $createSubmit.attr("disabled", true);
                doPost("<%=request.getContextPath()%>/admin/user/userContactBook/save",
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
<%--</sec:authorize>--%>


<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userContactBook_update')})">--%>
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
                        <input type="hidden" name="userId">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">姓名：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="100" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">手机号码：</label>
							</div>
							<div class="col-xs-9">
								<input name="mobile" type="text" maxlength="20" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label">备注：</label>
							</div>
							<div class="col-xs-9">
								<input name="remark" type="text" maxlength="255" class="form-control">
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
					maxlength: 100
				},
				mobile: {
					required: true,
					notEmpty: true,
					maxlength: 20
				},
				remark: {
					maxlength: 255
				}
			},
			messages: {
				name: {
					required: "姓名不能为空",
					notEmpty: "姓名不能为空",
					maxlength: "姓名最多100个字"
				},
				mobile: {
					required: "手机号码不能为空",
					notEmpty: "手机号码不能为空",
					maxlength: "手机号码最多20个字"
				},
				remark: {
					maxlength: "备注最多255个字"
				}
			},
            submitHandler: function () {
                $updateSubmit.attr("disabled", true);
                var params = $updateForm.xsJson();


                doPost("<%=request.getContextPath()%>/admin/user/userContactBook/update", params,
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
            doPost("<%=request.getContextPath()%>/admin/user/userContactBook/get", {
                id: id,
                userId: '${user.id}'
            }, function (data) {
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
    </script>
<%--</sec:authorize>--%>

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userContactBook_delete')})">--%>
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteListItem(id) {
            showDeleteModel(null, function () {
                doPost("<%=request.getContextPath()%>/admin/user/userContactBook/remove", {
                    id: id,
                    userId: '${user.id}'
                }, function (data) {
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
<%--</sec:authorize>--%>
</body>
</html>

