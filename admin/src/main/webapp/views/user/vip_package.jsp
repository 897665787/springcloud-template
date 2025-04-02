<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by JQ棣 on 2018/11/14.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>会员套餐管理</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="marketing_vip"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">会员套餐管理</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">ID：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="dynamic[id]" type="text" class="form-control" placeholder="模糊查询" value="${search.dynamic.id}">
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
							<select name="status" class="form-control">
								<option value="">全部</option>
								<xs:descriptionOptions clazz="user.vip.VipPackage" property="status" value="${search.status}"/>
							</select>
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">平台：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="platform" class="form-control">
								<option value="">全部</option>
								<xs:descriptionOptions clazz="user.vip.VipPackage" property="platform" value="${search.platform}"/>
							</select>
						</div>
                    </div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <sec:authorize access="hasAnyRole(${xs:getPermissions('vip_vipPackage_create')})">
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
								<th>ID</th>
								<th>名称</th>
								<th>金额</th>
								<th>时长（天）</th>
								<th>状态</th>
								<th>顺序</th>
								<th>平台</th>
								<th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:if test="${pageModel.list.size() eq 0}">
                            <tr>
                                <td colspan="7">无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${pageModel.list}" var="item">
                            <tr>
								<td>${item.id}</td>
								<td>${item.name}</td>
								<td><fmt:formatNumber pattern="0.##" value="${item.fee}"/></td>
								<td>${item.duration}</td>
								<td><xs:descriptionDesc clazz="user.vip.VipPackage" property="status" value="${item.status}"/></td>
								<td>${item.seq}</td>
								<td><xs:descriptionDesc clazz="user.vip.VipPackage" property="platform" value="${item.platform}"/></td>
                                <td>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('vip_vipPackage_update')})">
                                        <a href="#" onclick="updateListItem('${item.id}');return false;"
                                           class="btn btn-info btn-xs">
                                            编辑
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('vip_vipPackage_delete')})">
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('vip_vipPackage_create')})">
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
								<label class="control-label required">ID：</label>
							</div>
							<div class="col-xs-9">
								<input name="id" type="text" maxlength="16" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">名称：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="32" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">金额：</label>
							</div>
							<div class="col-xs-9">
								<input name="fee" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">时长（天）：</label>
							</div>
							<div class="col-xs-9">
								<input name="duration" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">状态：</label>
							</div>
							<div class="col-xs-9">
								<select name="status" class="form-control" data-ignore="true">
									<xs:descriptionOptions clazz="user.vip.VipPackage" property="status"/>
								</select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">顺序：</label>
							</div>
							<div class="col-xs-9">
								<input name="seq" type="number" class="form-control" value="0">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">平台：</label>
							</div>
							<div class="col-xs-9">
								<select name="platform" class="form-control" data-ignore="true">
									<xs:descriptionOptions clazz="user.vip.VipPackage" property="platform"/>
								</select>
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
                id: {
                    required: true,
                    notEmpty: true,
                    maxlength: 16
                },
				name: {
					required: true,
					notEmpty: true,
					maxlength: 32
				},
				fee: {
					required: true,
					number: true
				},
                duration: {
                    required: true,
                    number: true,
                    digits: true,
					min: 1
                },
				status: {
					required: true,
					number: true,
					digits: true
				},
				seq: {
					required: true,
					number: true,
					digits: true
				},
				platform: {
					required: true,
					number: true,
					digits: true
				}
			},
			messages: {
                id: {
                    required: "ID不能为空",
                    notEmpty: "ID不能为空",
                    maxlength: "ID最多16个字"
                },
				name: {
					required: "名称不能为空",
					notEmpty: "名称不能为空",
					maxlength: "名称最多32个字"
				},
				fee: {
					required: "金额不能为空",
					number: "金额必须为数字"
				},
                duration: {
                    required: "时长不能为空",
                    number: "时长必须为数字",
                    digits: "时长必须为非负整数",
					min: "时长至少为1"
                },
				status: {
					required: "状态不能为空",
					number: "状态必须为数字",
					digits: "状态必须为非负整数"
				},
				seq: {
					required: "顺序不能为空",
					number: "顺序必须为数字",
					digits: "顺序必须为非负整数"
				},
				platform: {
					required: "平台不能为空",
					number: "平台必须为数字",
					digits: "平台必须为非负整数"
				}
			},
            submitHandler: function () {
                $createSubmit.attr("disabled", true);
                doPost("<%=request.getContextPath()%>/admin/user/vipPackage/save",
                    $createForm.serialize(),
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('vip_vipPackage_update')})">
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
								<input name="name" type="text" maxlength="32" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">金额：</label>
							</div>
							<div class="col-xs-9">
								<input name="fee" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">时长：</label>
							</div>
							<div class="col-xs-9">
								<input name="duration" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">状态：</label>
							</div>
							<div class="col-xs-9">
								<select name="status" class="form-control">
									<xs:descriptionOptions clazz="user.vip.VipPackage" property="status"/>
								</select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">顺序：</label>
							</div>
							<div class="col-xs-9">
								<input name="seq" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">平台：</label>
							</div>
							<div class="col-xs-9">
								<select name="platform" class="form-control">
									<xs:descriptionOptions clazz="user.vip.VipPackage" property="platform"/>
								</select>
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
					maxlength: 32
				},
				fee: {
					required: true,
					number: true
				},
                duration: {
                    required: true,
                    number: true,
                    digits: true,
                    min: 1
                },
				status: {
					required: true,
					number: true,
					digits: true
				},
				seq: {
					required: true,
					number: true,
					digits: true
				},
				platform: {
					required: true,
					number: true,
					digits: true
				}
			},
			messages: {
				name: {
					required: "名称不能为空",
					notEmpty: "名称不能为空",
					maxlength: "名称最多32个字"
				},
				fee: {
					required: "金额不能为空",
					number: "金额必须为数字"
				},
                duration: {
                    required: "时长不能为空",
                    number: "时长必须为数字",
                    digits: "时长必须为非负整数",
                    min: "时长至少为1"
                },
				status: {
					required: "状态不能为空",
					number: "状态必须为数字",
					digits: "状态必须为非负整数"
				},
				seq: {
					required: "顺序不能为空",
					number: "顺序必须为数字",
					digits: "顺序必须为非负整数"
				},
				platform: {
					required: "平台不能为空",
					number: "平台必须为数字",
					digits: "平台必须为非负整数"
				}
			},
            submitHandler: function () {
                $updateSubmit.attr("disabled", true);
                var params = $updateForm.xsJson();


                doPost("<%=request.getContextPath()%>/admin/user/vipPackage/update", params,
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
            doPost("<%=request.getContextPath()%>/admin/user/vipPackage/get", {id: id}, function (data) {
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
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('vip_vipPackage_delete')})">
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteListItem(id) {
            showDeleteModel(null, function () {
                doPost("<%=request.getContextPath()%>/admin/user/vipPackage/remove", {id: id}, function (data) {
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

