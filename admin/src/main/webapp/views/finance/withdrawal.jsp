<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by xuxiaowei on 2018/11/23.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>提现申请</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="finance_withdrawal"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">提现申请</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">昵称：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="user.nickname" type="text" class="form-control" placeholder="模糊查询" value="${search.user.nickname}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">手机号：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="user.mobile" type="text" class="form-control" placeholder="模糊查询" value="${search.user.mobile}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">事件类型：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="eventType" class="form-control">
								<option value="">全部</option>
								<xs:dictOptions key="withdrawalEventType" value="${search.eventType}"/>
							</select>
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">状态：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="status" class="form-control">
								<option value="">全部</option>
								<xs:descriptionOptions clazz="user.wallet.Withdrawal" property="status" value="${search.status}"/>
							</select>
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">提现渠道：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="channel" class="form-control">
								<option value="">全部</option>
								<xs:descriptionOptions clazz="user.wallet.Withdrawal" property="channel" value="${search.channel}"/>
							</select>
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">开始申请时间：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input type="text" name="dynamic[applicationTimeBegin]" class="form-control datepicker" readonly value="${search.dynamic.applicationTimeBegin}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">结束申请时间：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input type="text" name="dynamic[applicationTimeEnd]" class="form-control datepicker" readonly value="${search.dynamic.applicationTimeEnd}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">开始通过时间：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input type="text" name="dynamic[passTimeBegin]" class="form-control datepicker" readonly value="${search.dynamic.passTimeBegin}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">结束通过时间：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input type="text" name="dynamic[passTimeEnd]" class="form-control datepicker" readonly value="${search.dynamic.passTimeEnd}">
						</div>
                    </div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <input class="btn btn-info pull-right" value="搜索" type="submit">
                            <input class="btn btn-default pull-right  m-r-sm" value="重置" type="button"
                                   onclick="$('#searchForm').xsClean()">
                        </div>
                    </div>
                </form>
				<p4>请各运营人员注意，根据微信企业打款限制，同一微信账号提现金额最小1.00元，最大日累计20000.00元</p4>
                <div class="panel panel-default m-b-none">
                    <table class="table text-center table-bordered table-striped m-b-none">
                        <thead>
                            <tr>
								<th>序号</th>
								<th>昵称</th>
								<th>手机号</th>
								<th>事件类型</th>
								<th>金额</th>
								<th>状态</th>
								<th>提现渠道</th>
								<th>申请时间</th>
								<th>审核人</th>
								<th>通过时间</th>
								<th>拒绝理由</th>
								<th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:if test="${pageModel.list.size() eq 0}">
                            <tr>
                                <td colspan="12">无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${pageModel.list}" var="item" varStatus="status">
                            <tr>
								<td>${status.index + 1}</td>
								<td>${item.user.nickname}</td>
								<td>${item.user.mobile}</td>
								<td><xs:dictDesc key="withdrawalEventType" value="${item.eventType}"/></td>
								<td><fmt:formatNumber pattern="0.##" value="${item.fee}"/></td>
								<td><xs:descriptionDesc clazz="user.wallet.Withdrawal" property="status" value="${item.status}"/></td>
								<td><xs:descriptionDesc clazz="user.wallet.Withdrawal" property="channel" value="${item.channel}"/></td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.applicationTime}"/></td>
								<td>${item.auditorName}</td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.passTime}"/></td>
								<td>${item.rejectReason}</td>
                                <td>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_withdrawal_audit')})">
                                        <c:if test="${item.status==1}">
											<a href="#" onclick="passListItem('${item.id}', '${item.channel}');return false;"
											   class="btn btn-success btn-xs">
												通过
											</a>
											<a href="#" onclick="rejectListItem('${item.id}');return false;"
											   class="btn btn-warning btn-xs">
												拒绝
											</a>
										</c:if>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_withdrawal_delete')})">
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_withdrawal_audit')})">
    <div class="modal fade" id="rejectModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">拒绝</h4>
                </div>
                <form name="rejectForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
                        <input type="hidden" name="id" id="rejectId">
						<input type="hidden" name="status" value="3">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">拒绝理由：</label>
							</div>
							<div class="col-xs-9">
								<textarea name="rejectReason" maxlength="256" class="form-control"></textarea>
							</div>
						</div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button id="rejectSubmit" type="submit" class="btn btn-success">确定</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        var $rejectForm = $("form[name=rejectForm]");
        var $rejectSubmit = $("#rejectSubmit");
        var rejectValidator = $rejectForm.validate({
            onfocusout: function(element) { $(element).valid(); },
			rules: {
                rejectReason: {
					required: true,
					notEmpty: true,
					maxlength: 256
				}
			},
			messages: {
                rejectReason: {
					required: "拒绝理由不能为空",
					notEmpty: "拒绝理由不能为空",
					maxlength: "拒绝理由最多256个字"
				}
			},
            submitHandler: function () {
                $rejectSubmit.attr("disabled", true);
                var params = $rejectForm.xsJson();
                doPost("<%=request.getContextPath()%>/admin/user/withdrawal/audit", params,
                    function (data) {
                        $rejectSubmit.attr("disabled", false);
                        if (data.status) {
                            $("#rejectModel").modal("hide");
                            bootoast({message: "更新成功！"});
                            setTimeout(function () {
                                window.location.reload(true);
                            }, 680);
                        } else {
                            alert(data.message);
                        }
                    }, function (XMLHttpRequest, textStatus) {
                        $rejectSubmit.attr("disabled", false);
                        alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                    });
            }
        });

        function rejectListItem(id) {
            $("#rejectId").val(id)
            $("#rejectModel").modal("show");
        }

        $(function () {
            $("#rejectModel").on('hide.bs.modal', function () {
                $rejectForm.xsClean();
                rejectValidator.resetForm();
            })
        });
    </script>

<div class="modal fade" id="passModel" data-backdrop="static" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
						aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">通过</h4>
			</div>
			<form name="passForm" class="form-horizontal" style="max-width: 800px">
				<div class="modal-body">
					<input type="hidden" name="id" id="passId">
					<input type="hidden" name="status" value="2">
					<p id="passMsg"></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button id="passSubmit" type="submit" class="btn btn-success" onclick="pass()">确定</button>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
    var $passForm = $("form[name=passForm]");
    var $passSubmit = $("#passSubmit");

    function pass() {
        $passSubmit.attr("disabled", true);
        var params = $passForm.xsJson();
        doPost("<%=request.getContextPath()%>/admin/user/withdrawal/audit", params,
            function (data) {
                $passSubmit.attr("disabled", false);
                if (data.status) {
                    $("#passModel").modal("hide");
                    bootoast({message: "更新成功！"});
                    setTimeout(function () {
                        window.location.reload(true);
                    }, 680);
                } else {
                    alert(data.message);
                }
            }, function (XMLHttpRequest, textStatus) {
                $passSubmit.attr("disabled", false);
                alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
            });
    }

    function passListItem(id, channel) {
        $("#passId").val(id);
        if (channel == 3) {
            $("#passMsg").html("微信将自动进行打款");
		}
		else {
            $("#passMsg").html("请确认已经转账后再进行通过");
		}
        $("#passModel").modal("show");
    }
</script>
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('wallet_withdrawal_delete')})">
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteListItem(id) {
            showDeleteModel(null, function () {
                doPost("<%=request.getContextPath()%>/admin/user/withdrawal/remove", {id: id}, function (data) {
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

