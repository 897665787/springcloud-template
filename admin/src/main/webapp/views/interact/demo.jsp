<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>Demo管理</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="interact_demo"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">Demo管理</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">查询条件：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="dynamic[content]" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.dynamic.content}">
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
                                <th>发布者</th>
                        		<th>点赞数</th>
                        		<th>评论数</th>
                        		<th>收藏数</th>
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
                                <td>${item.userId}</td>
                            	<td>${item.praiseNum}</td>
                            	<td>${item.commentNum}</td>
                            	<td>${item.collectNum}</td>
                                <td>
                                    <a href="<%=request.getContextPath()%>/admin/interact/demo/detail?id=${item.id}"
                                       class="btn btn-warning btn-xs">详情</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
