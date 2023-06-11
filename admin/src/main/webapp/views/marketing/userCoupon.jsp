<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>营销-用户优惠券</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="marketing_userCoupon"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">营销-用户优惠券</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">bu_user_info.id：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="userId" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.userId}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">mk_coupon_template.id：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="couponTemplateId" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.couponTemplateId}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">优惠券名称：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="name" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.name}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">最大优惠金额：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="maxAmount" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.maxAmount}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">折扣：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="discount" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.discount}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">满X金额可用：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="conditionAmount" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.conditionAmount}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">有效期开始时间：</label>
                    	</div>
                        <div class="col-xs-8 col-md-4 col-lg-7 m-b-md">
                            <div class="row">
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="beginTimeStart" class="form-control datepicker"
                                           value="${search.beginTimeStart}" readonly>
                                </div>
                                <label class="pull-left control-label" style="width: 15px">至</label>
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="beginTimeEnd" class="form-control datepicker"
                                           value="${search.beginTimeEnd}" readonly>
                                </div>
                            </div>
                        </div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">有效期结束时间：</label>
                    	</div>
                        <div class="col-xs-8 col-md-4 col-lg-7 m-b-md">
                            <div class="row">
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="endTimeStart" class="form-control datepicker"
                                           value="${search.endTimeStart}" readonly>
                                </div>
                                <label class="pull-left control-label" style="width: 15px">至</label>
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="endTimeEnd" class="form-control datepicker"
                                           value="${search.endTimeEnd}" readonly>
                                </div>
                            </div>
                        </div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">状态：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="status" class="form-control" data-value="${search.status}">
								<option value="">全部</option>
								<xs:descriptionOptions clazz="marketing.UserCoupon" property="status"/>
							</select>
						</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">最大叠加数：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="maxNum" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.maxNum}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">备注：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="remark" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.remark}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">创建时间：</label>
                    	</div>
                        <div class="col-xs-8 col-md-4 col-lg-7 m-b-md">
                            <div class="row">
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="createTimeStart" class="form-control datepicker"
                                           value="${createTimeStart}" readonly>
                                </div>
                                <label class="pull-left control-label" style="width: 15px">至</label>
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="createTimeEnd" class="form-control datepicker"
                                           value="${createTimeEnd}" readonly>
                                </div>
                            </div>
                        </div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">更新时间：</label>
                    	</div>
                        <div class="col-xs-8 col-md-4 col-lg-7 m-b-md">
                            <div class="row">
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="updateTimeStart" class="form-control datepicker"
                                           value="${updateTimeStart}" readonly>
                                </div>
                                <label class="pull-left control-label" style="width: 15px">至</label>
                                <div class="col-xs-10 col-md-4 col-lg-4 ">
                                    <input type="text" name="updateTimeEnd" class="form-control datepicker"
                                           value="${updateTimeEnd}" readonly>
                                </div>
                            </div>
                        </div>
					</div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_userCoupon_create')})">
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
                        		<th>bu_user_info.id</th>
                        		<th>mk_coupon_template.id</th>
                        		<th>优惠券名称</th>
                        		<th>最大优惠金额</th>
                        		<th>折扣</th>
                        		<th>满X金额可用</th>
                        		<th>有效期开始时间</th>
                        		<th>有效期结束时间</th>
                        		<th>状态</th>
                        		<th>最大叠加数</th>
                        		<th>备注</th>
								<th>创建/更新时间</th>
								<th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:if test="${pageModel.list.size() eq 0}">
                            <tr>
                                <td colspan="13">无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${pageModel.list}" var="item">
                            <tr>
                            	<td>${item.userId}</td>
                            	<td>${item.couponTemplateId}</td>
                            	<td>${item.name}</td>
                            	<td>${item.maxAmount}</td>
                            	<td>${item.discount}</td>
                            	<td>${item.conditionAmount}</td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd" value="${item.beginTime}"/></td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd" value="${item.endTime}"/></td>
                                <td><xs:descriptionDesc clazz="marketing.UserCoupon" property="status" value="${item.status}"/></td>
                            	<td>${item.maxNum}</td>
                            	<td>${item.remark}</td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createTime}"/><br/><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.updateTime}"/></td>
                                <td>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_userCoupon_get')})">
                                        <a href="#" onclick="getListItem('${item.id}');return false;"
                                           class="btn btn-success btn-xs">
                                            详情
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_userCoupon_update')})">
                                        <a href="#" onclick="updateListItem('${item.id}');return false;"
                                           class="btn btn-info btn-xs">
                                            编辑
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_userCoupon_delete')})">
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_userCoupon_create')})">
    <div class="modal fade" id="createModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">新增</h4>
                </div>
                <form name="createForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">bu_user_info.id：</label>
							</div>
							<div class="col-xs-9">
								<input name="userId" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">mk_coupon_template.id：</label>
							</div>
							<div class="col-xs-9">
								<input name="couponTemplateId" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">优惠券名称：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">最大优惠金额：</label>
							</div>
							<div class="col-xs-9">
								<input name="maxAmount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">折扣：</label>
							</div>
							<div class="col-xs-9">
								<input name="discount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">满X金额可用：</label>
							</div>
							<div class="col-xs-9">
								<input name="conditionAmount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">有效期开始时间：</label>
							</div>
							<div class="col-xs-9">
								<input name="beginTime" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">有效期结束时间：</label>
							</div>
							<div class="col-xs-9">
								<input name="endTime" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">状态：</label>
							</div>
							<div class="col-xs-9">
								<select name="status" class="form-control">
									<xs:descriptionOptions clazz="marketing.UserCoupon" property="status"/>
								</select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">最大叠加数：</label>
							</div>
							<div class="col-xs-9">
								<input name="maxNum" type="number" class="form-control">
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
				userId: {
					required: true
				},
				couponTemplateId: {
					required: true
				},
				name: {
					required: true,
					notEmpty: true,
					maxlength: 128
				},
				maxAmount: {
					required: true
				},
				discount: {
					required: true
				},
				conditionAmount: {
					required: true
				},
				beginTime: {
					required: true
				},
				endTime: {
					required: true
				},
				status: {
					required: true
				},
				maxNum: {
					required: true
				},
			},
			messages: {
				userId: {
					required: "bu_user_info.id不能为空"
				},
				couponTemplateId: {
					required: "mk_coupon_template.id不能为空"
				},
				name: {
					required: "优惠券名称不能为空",
					notEmpty: "优惠券名称不能为空",
					maxlength: "优惠券名称最多128个字"
				},
				maxAmount: {
					required: "最大优惠金额不能为空"
				},
				discount: {
					required: "折扣不能为空"
				},
				conditionAmount: {
					required: "满X金额可用不能为空"
				},
				beginTime: {
					required: "有效期开始时间不能为空"
				},
				endTime: {
					required: "有效期结束时间不能为空"
				},
				status: {
					required: "状态不能为空"
				},
				maxNum: {
					required: "最大叠加数不能为空"
				},
			},
            submitHandler: function () {
                $createSubmit.attr("disabled", true);
                doPost("<%=request.getContextPath()%>/admin/marketing/userCoupon/save",
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_userCoupon_get')})">
    <div class="modal fade" id="getModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">详情</h4>
                </div>
                <form name="getForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
                        <input type="hidden" name="id">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">bu_user_info.id：</label>
							</div>
							<div class="col-xs-9">
								<input name="userId" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">mk_coupon_template.id：</label>
							</div>
							<div class="col-xs-9">
								<input name="couponTemplateId" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">优惠券名称：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">最大优惠金额：</label>
							</div>
							<div class="col-xs-9">
								<input name="maxAmount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">折扣：</label>
							</div>
							<div class="col-xs-9">
								<input name="discount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">满X金额可用：</label>
							</div>
							<div class="col-xs-9">
								<input name="conditionAmount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">有效期开始时间：</label>
							</div>
							<div class="col-xs-9">
								<input name="beginTime" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">有效期结束时间：</label>
							</div>
							<div class="col-xs-9">
								<input name="endTime" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">状态：</label>
							</div>
							<div class="col-xs-9">
								<select name="status" class="form-control">
									<xs:descriptionOptions clazz="marketing.UserCoupon" property="status"/>
								</select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">最大叠加数：</label>
							</div>
							<div class="col-xs-9">
								<input name="maxNum" type="number" class="form-control">
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
                        <button type="button" class="btn btn-success" data-dismiss="modal">确定</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        var $getForm = $("form[name=getForm]");

        function getListItem(id) {
            doPost("<%=request.getContextPath()%>/admin/marketing/userCoupon/get", {id: id}, function (data) {
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_userCoupon_update')})">
    <div class="modal fade" id="updateModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">编辑</h4>
                </div>
                <form name="updateForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
                        <input type="hidden" name="id">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">bu_user_info.id：</label>
							</div>
							<div class="col-xs-9">
								<input name="userId" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">mk_coupon_template.id：</label>
							</div>
							<div class="col-xs-9">
								<input name="couponTemplateId" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">优惠券名称：</label>
							</div>
							<div class="col-xs-9">
								<input name="name" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">最大优惠金额：</label>
							</div>
							<div class="col-xs-9">
								<input name="maxAmount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">折扣：</label>
							</div>
							<div class="col-xs-9">
								<input name="discount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">满X金额可用：</label>
							</div>
							<div class="col-xs-9">
								<input name="conditionAmount" type="number" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">有效期开始时间：</label>
							</div>
							<div class="col-xs-9">
								<input name="beginTime" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">有效期结束时间：</label>
							</div>
							<div class="col-xs-9">
								<input name="endTime" type="text" maxlength="255" class="form-control">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">状态：</label>
							</div>
							<div class="col-xs-9">
								<select name="status" class="form-control">
									<xs:descriptionOptions clazz="marketing.UserCoupon" property="status"/>
								</select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">最大叠加数：</label>
							</div>
							<div class="col-xs-9">
								<input name="maxNum" type="number" class="form-control">
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
				userId: {
					required: true
				},
				couponTemplateId: {
					required: true
				},
				name: {
					required: true,
					notEmpty: true,
					maxlength: 128
				},
				maxAmount: {
					required: true
				},
				discount: {
					required: true
				},
				conditionAmount: {
					required: true
				},
				beginTime: {
					required: true
				},
				endTime: {
					required: true
				},
				status: {
					required: true
				},
				maxNum: {
					required: true
				},

			},
			messages: {
				userId: {
					required: "bu_user_info.id不能为空"
				},
				couponTemplateId: {
					required: "mk_coupon_template.id不能为空"
				},
				name: {
					required: "优惠券名称不能为空",
					notEmpty: "优惠券名称不能为空",
					maxlength: "优惠券名称最多128个字"
				},
				maxAmount: {
					required: "最大优惠金额不能为空"
				},
				discount: {
					required: "折扣不能为空"
				},
				conditionAmount: {
					required: "满X金额可用不能为空"
				},
				beginTime: {
					required: "有效期开始时间不能为空"
				},
				endTime: {
					required: "有效期结束时间不能为空"
				},
				status: {
					required: "状态不能为空"
				},
				maxNum: {
					required: "最大叠加数不能为空"
				},

			},
            submitHandler: function () {
                $updateSubmit.attr("disabled", true);
                var params = $updateForm.xsJson();

                doPost("<%=request.getContextPath()%>/admin/marketing/userCoupon/update", params,
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
            doPost("<%=request.getContextPath()%>/admin/marketing/userCoupon/get", {id: id}, function (data) {
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_userCoupon_delete')})">
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteListItem(id) {
            showDeleteModel(null, function () {
                doPost("<%=request.getContextPath()%>/admin/marketing/userCoupon/remove", {id: id}, function (data) {
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