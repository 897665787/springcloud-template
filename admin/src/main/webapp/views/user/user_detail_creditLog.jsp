<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by kunye on 2018/05/28.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
	<title>用户详情</title>
	<%@include file="../common/head.jsp" %>
	<%@include file="../common/datepicker.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
	<div class="app-content-body">
		<jsp:include page="user_detail_nav.jsp">
			<jsp:param name="nav" value="4"/>
		</jsp:include>
		<div class="wrapper-md row">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                	<input name="id" type="hidden" value="${ user.id }" data-ignore="true">
                    <div class="form-group">
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">收入/支出：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="type" class="form-control">
								<option value="">全部</option>
								<option value="1"<c:if test="${ user.type == 1 }"> selected</c:if>>收入</option>
								<option value="2"<c:if test="${ user.type == 2 }"> selected</c:if>>支出</option>
							</select>
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">日期：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-7 m-b-md">
							<div class="row">
								<div class="col-xs-10 col-md-4 col-lg-4 ">
									<input type="text" name="dynamic[createTimeBegin]" class="form-control datepicker" value="${user.dynamic.createTimeBegin}" readonly>
								</div>
								<label class="pull-left control-label" style="width: 15px">至</label>
								<div class="col-xs-10 col-md-4 col-lg-4 ">
									<input type="text" name="dynamic[createTimeEnd]" class="form-control datepicker" value="${user.dynamic.createTimeEnd}" readonly>
								</div>
								<a class="pull-left control-label text-info js-date-quick" data-days="6">近7天</a>
								<a class="pull-left control-label m-l-sm text-info js-date-quick"
								   data-days="29">近30天</a>
							</div>
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
                <div class="panel panel-default m-b-none">
                    <table class="table text-center table-bordered table-striped m-b-none">
                        <thead>
                            <tr>
								<th>ID</th>
								<th>收入/支出</th>
								<th>备注</th>
								<th>日期</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:if test="${pageModel.list.size() eq 0}">
                            <tr>
                                <td colspan="4">无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${pageModel.list}" var="item">
                            <tr>
								<td>${item.id}</td>
								<td class="font-bold text-md ${item.credit > 0 ? 'text-success' : 'text-danger'}">
								${item.credit > 0 ? '+' : ''}${item.credit}
								</td>
								<td>${item.remark}</td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createTime}"/></td>
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
</body>
</html>
