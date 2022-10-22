<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by JQ棣 on 2018/11/23.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
	<title>用户详情</title>
	<%@include file="../common/head.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
	<div class="app-content-body">
		<jsp:include page="user_detail_nav.jsp">
			<jsp:param name="nav" value="11"/>
		</jsp:include>
		<div class="wrapper-md row">
			<div class="col-xs-6" align="center">
	            <div class="panel panel-default m-b-none" style="width: 400px;">
					<table id="bankCardTable" class="table text-center table-bordered m-b-none">
						<thead>
						<tr>
							<th>银行卡（<span id="bankCardCount">${bankCardPageModel.total}</span>张）<a onclick="showCreateModal()" class="btn-sm btn-success">新增</a></th>
						</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${bankCardPageModel.total == 0}">
									<tr>
										<td>暂无数据</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${bankCardPageModel.list}" var="item" varStatus="status">
										<tr>
											<td>
												<div class="media">
													<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">序号：${status.index + 1}</div>
													<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">银行信息：${item.bankName}</div>
													<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">银行卡号：${item.cardNumber}</div>
													<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">开户人：${item.owner}</div>
													<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">身份证：${item.identityNumber}</div>
													<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">手机号：${item.mobile}</div>
													<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">绑定时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createTime}"/></div>
													<a class="btn-sm btn-info" onclick="updateListItem(${item.id})">编辑</a>
													<a class="btn-sm btn-danger" onclick="deleteListItem(${item.id})">删除</a>
												</div>
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
            </div>
            <div class="col-xs-6" align="center">
            	<div class="panel panel-default m-b-none" style="width: 400px;">
					<table id="aliPayTable" class="table text-center table-bordered m-b-none">
						<thead>
						<tr>
							<th>支付宝（<span id="aliPayCardCount">${aliPayPageModel.total}</span>张）<a onclick="showCreateModal4Ali()" class="btn-sm btn-success">新增</a></th>
						</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${aliPayPageModel.total == 0}">
								<tr>
									<td>暂无数据</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${aliPayPageModel.list}" var="item" varStatus="status">
									<tr>
										<td>
											<div class="media">
												<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">序号：${status.index + 1}</div>
												<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">账号：${item.account}</div>
												<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">真实姓名：${item.name}</div>
												<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">绑定时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createTime}"/></div>
												<a class="btn-sm btn-info" onclick="updateListItem4Ali(${item.id})">编辑</a>
												<a class="btn-sm btn-danger" onclick="deleteListItem4Ali(${item.id})">删除</a>
											</div>
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>
				</div>
            </div>
		</div>
	</div>
</div>

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_bankCard_create')})">--%>
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
					<input name="user.id" type="hidden" value="${user.id}">
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label required">卡号：</label>
						</div>
						<div class="col-xs-9">
							<input name="cardNumber" type="text" maxlength="32" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label">开户人：</label>
						</div>
						<div class="col-xs-9">
							<input name="owner" type="text" maxlength="32" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label">身份证号码：</label>
						</div>
						<div class="col-xs-9">
							<input name="identityNumber" type="text" maxlength="32" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label">手机号：</label>
						</div>
						<div class="col-xs-9">
							<input name="mobile" type="text" maxlength="32" class="form-control">
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
            cardNumber: {
                required: true,
                notEmpty: true,
                maxlength: 32
            },
            owner: {
                maxlength: 32
            },
            identityNumber: {
                maxlength: 32
            },
            mobile: {
                maxlength: 32
            }
        },
        messages: {
            cardNumber: {
                required: "卡号不能为空",
                notEmpty: "卡号不能为空",
                maxlength: "卡号最多32个字"
            },
            owner: {
                maxlength: "开户人最多32个字"
            },
            identityNumber: {
                maxlength: "身份证号码最多32个字"
            },
            mobile: {
                maxlength: "手机号最多32个字"
            }
        },
        submitHandler: function () {
            $createSubmit.attr("disabled", true);
            var params = $createForm.serialize();
            doPost("<%=request.getContextPath()%>/admin/user/bankCard/save",
                params,
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

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_bankCard_update')})">--%>
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
							<label class="control-label required">卡号：</label>
						</div>
						<div class="col-xs-9">
							<input name="cardNumber" type="text" maxlength="32" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label">开户人：</label>
						</div>
						<div class="col-xs-9">
							<input name="owner" type="text" maxlength="32" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label">身份证号码：</label>
						</div>
						<div class="col-xs-9">
							<input name="identityNumber" type="text" maxlength="32" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label">手机号：</label>
						</div>
						<div class="col-xs-9">
							<input name="mobile" type="text" maxlength="32" class="form-control">
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
            cardNumber: {
                required: true,
                notEmpty: true,
                maxlength: 32
            },
            owner: {
                maxlength: 32
            },
            identityNumber: {
                maxlength: 32
            },
            mobile: {
                maxlength: 32
            }
        },
        messages: {
            cardNumber: {
                required: "卡号不能为空",
                notEmpty: "卡号不能为空",
                maxlength: "卡号最多32个字"
            },
            owner: {
                maxlength: "开户人最多32个字"
            },
            identityNumber: {
                maxlength: "身份证号码最多32个字"
            },
            mobile: {
                maxlength: "手机号最多32个字"
            }
        },
        submitHandler: function () {
            $updateSubmit.attr("disabled", true);
            var params = $updateForm.xsJson();
            doPost("<%=request.getContextPath()%>/admin/user/bankCard/update", params,
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
        doPost("<%=request.getContextPath()%>/admin/user/bankCard/get", {id: id}, function (data) {
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

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_bankCard_delete')})">--%>
<%@include file="../common/deleteConfirm.jsp" %>
<script>
    function deleteListItem(id) {
        showDeleteModel(null, function () {
            doPost("<%=request.getContextPath()%>/admin/user/bankCard/remove", {id: id}, function (data) {
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

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_aliAccount_create')})">--%>
<div class="modal fade" id="createModel4Ali" data-backdrop="static" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
						aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">新增</h4>
			</div>
			<form name="createForm4Ali" class="form-horizontal" style="max-width: 800px">
				<div class="modal-body">
					<input name="user.id" type="hidden" value="${user.id}">
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label required">真实姓名：</label>
						</div>
						<div class="col-xs-9">
							<input name="name" type="text" maxlength="32" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label required">支付宝账号：</label>
						</div>
						<div class="col-xs-9">
							<input name="account" type="text" maxlength="32" class="form-control">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button id="createSubmit4Ali" type="submit" class="btn btn-success">确定</button>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
    var $createForm4Ali = $("form[name=createForm4Ali]");
    var $createSubmit4Ali = $("#createSubmit4Ali");
    var createValidator4Ali = $createForm4Ali.validate({
        onfocusout: function(element) { $(element).valid(); },
        rules: {
            name: {
                required: true,
                notEmpty: true,
                maxlength: 32
            },
            account: {
                required: true,
                notEmpty: true,
                maxlength: 32
            }
        },
        messages: {
            name: {
                required: "真实姓名不能为空",
                notEmpty: "真实姓名不能为空",
                maxlength: "真实姓名最多32个字"
            },
            account: {
                required: "支付宝账号不能为空",
                notEmpty: "支付宝账号不能为空",
                maxlength: "支付宝账号最多32个字"
            }
        },
        submitHandler: function () {
            $createSubmit4Ali.attr("disabled", true);
            doPost("<%=request.getContextPath()%>/admin/user/aliAccount/save",
                $createForm4Ali.serialize(),
                function (data) {
                    $createSubmit4Ali.attr("disabled", false);
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
                    $createSubmit4Ali.attr("disabled", false);
                    alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                });
        }
    });

    function showCreateModal4Ali() {
        $("#createModel4Ali").modal("show");
    }
    $(function () {
        $("#createModel4Ali").on('hide.bs.modal', function () {
            $createForm4Ali.xsClean();
            createValidator4Ali.resetForm();
        })
    });
</script>
<%--</sec:authorize>--%>

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_aliAccount_update')})">--%>
<div class="modal fade" id="updateModel4Ali" data-backdrop="static" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
						aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">编辑</h4>
			</div>
			<form name="updateForm4Ali" class="form-horizontal" style="max-width: 800px">
				<div class="modal-body">
					<input type="hidden" name="id">
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label required">真实姓名：</label>
						</div>
						<div class="col-xs-9">
							<input name="name" type="text" maxlength="32" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-3 text-right">
							<label class="control-label required">支付宝账号：</label>
						</div>
						<div class="col-xs-9">
							<input name="account" type="text" maxlength="32" class="form-control">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button id="updateSubmit4Ali" type="submit" class="btn btn-success">确定</button>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
    var $updateForm4Ali = $("form[name=updateForm4Ali]");
    var $updateSubmit4Ali = $("#updateSubmit4Ali");
    var updateValidator4Ali = $updateForm4Ali.validate({
        onfocusout: function(element) { $(element).valid(); },
        rules: {
            name: {
                required: true,
                notEmpty: true,
                maxlength: 32
            },
            account: {
                required: true,
                notEmpty: true,
                maxlength: 32
            }
        },
        messages: {
            name: {
                required: "真实姓名不能为空",
                notEmpty: "真实姓名不能为空",
                maxlength: "真实姓名最多32个字"
            },
            account: {
                required: "支付宝账号不能为空",
                notEmpty: "支付宝账号不能为空",
                maxlength: "支付宝账号最多32个字"
            }
        },
        submitHandler: function () {
            $updateSubmit4Ali.attr("disabled", true);
            var params = $updateForm4Ali.xsJson();


            doPost("<%=request.getContextPath()%>/admin/user/aliAccount/update", params,
                function (data) {
                    $updateSubmit4Ali.attr("disabled", false);
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
                    $updateSubmit4Ali.attr("disabled", false);
                    alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                });
        }
    });

    function updateListItem4Ali(id) {
        doPost("<%=request.getContextPath()%>/admin/user/aliAccount/get", {id: id}, function (data) {
            if (data.status) {
                $updateForm4Ali.xsSetForm(data.data);

                $("#updateModel4Ali").modal("show");
            } else {
                alert(data.message);
            }
        });
    }

    $(function () {
        $("#updateModel4Ali").on('hide.bs.modal', function () {
            $updateForm4Ali.xsClean();
            updateValidator4Ali.resetForm();
        })
    });
</script>
<%--</sec:authorize>--%>

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_aliAccount_delete')})">--%>
<%@include file="../common/deleteConfirm.jsp" %>
<script>
    function deleteListItem4Ali(id) {
        showDeleteModel(null, function () {
            doPost("<%=request.getContextPath()%>/admin/user/aliAccount/remove", {id: id}, function (data) {
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
</body>
</html>
