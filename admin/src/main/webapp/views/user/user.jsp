<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by JQ棣 on 2018/05/28.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>用户管理</title>
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
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">用户管理</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">手机号码：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="dynamic[mobile]" type="text" class="form-control" placeholder="模糊查询" value="${search.dynamic.mobile}">
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">用户名：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="dynamic[username]" type="text" class="form-control" placeholder="模糊查询" value="${search.dynamic.username}">
                        </div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">昵称：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="dynamic[nickname]" type="text" class="form-control" placeholder="模糊查询" value="${search.dynamic.nickname}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">状态：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="status" class="form-control" data-value="${search.status}">
								<option value="">全部</option>
								<xs:descriptionOptions clazz="user.User" property="status"/>
							</select>
						</div>
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">类型：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <select name="type" class="form-control" data-value="${search.type}">
                                <option value="">全部</option>
                                <xs:descriptionOptions clazz="user.User" property="type"/>
                            </select>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">顾客ID：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="customerId" type="text" class="form-control" value="${search.customerId}">
                        </div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">注册时间：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-7 m-b-md">
							<div class="row">
								<div class="col-xs-10 col-md-4 col-lg-4 ">
									<input type="text" name="dynamic[startTime]" class="form-control datepicker" value="${search.dynamic.startTime}" readonly>
								</div>
								<label class="pull-left control-label" style="width: 15px">至</label>
								<div class="col-xs-10 col-md-4 col-lg-4 ">
									<input type="text" name="dynamic[endTime]" class="form-control datepicker" value="${search.dynamic.endTime}" readonly>
								</div>
								<a class="pull-left control-label text-info js-date-quick" data-days="6">近7天</a>
								<a class="pull-left control-label m-l-sm text-info js-date-quick"
								   data-days="29">近30天</a>
							</div>
						</div>
                    </div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_create')})">
                                <a href="<%=request.getContextPath()%>/admin/user/create"
                                   class="btn btn-success">新增</a>
                            </sec:authorize>
                            <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_export')})">
                                <a class="btn btn-primary" onclick="exportUser();">
                                    导出
                                </a>
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
								<th>昵称</th>
								<th>手机号码</th>
								<th>出生年份</th>
								<th>性别</th>
								<th>状态</th>
								<th>注册时间</th>
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
								<td>${item.nickname}</td>
								<td>${item.mobile}</td>
								<td>${item.birthday}</td>
								<td><xs:descriptionDesc clazz="user.User" property="sex" value="${item.sex}"/></td>
								<td><xs:descriptionDesc clazz="user.User" property="status" value="${item.status}"/></td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createTime}"/></td>
                                <td>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get')})">
                                        <a href="<%=request.getContextPath()%>/admin/user/get?id=${item.id}"
                                           class="btn btn-warning btn-xs">查看详情</a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_update')})">
                                        <a href="<%=request.getContextPath()%>/admin/user/update/get?id=${item.id}"
                                           class="btn btn-info btn-xs">编辑</a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_delete')})">
                                        <a href="#" class="btn btn-danger btn-xs"
                                           onclick="deleteListItem('${item.id}','${item.type}');return false">
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

<%@include file="../common/deleteConfirm.jsp" %>
<sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_delete')})">
    <script>
        function deleteListItem(id, type) {
            var str;
            if(type == '11'){
                str = "该用户同时为后台员工，删除用户的同时将删除后台员工数据，确定删除";
            }else{
                str = "确定删除";
            }
            showDeleteModel(str, function () {
                doPost("<%=request.getContextPath()%>/admin/user/remove", {id: id}, function (data) {
                    if (data.status) {
                        setTimeout(function () {
                            bootoast({message: "删除成功！"});
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_export')})">
<script>
    //导出
    function exportUser() {
        window.location.href = "<%=request.getContextPath()%>/admin/user/export?"+$("#searchForm").serialize();
    }
</script>
</sec:authorize>
</body>
</html>

