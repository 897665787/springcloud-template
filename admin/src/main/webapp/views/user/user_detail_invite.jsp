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
    <title>用户邀请</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <jsp:include page="user_detail_nav.jsp">
            <jsp:param name="nav" value="7"/>
        </jsp:include>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <h5>邀请我的</h5>
                <div class="panel panel-default m-b-none">
                    <table class="table text-center table-bordered table-striped m-b-none">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>手机</th>
                            <th>昵称</th>
                            <th>邀请时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${inviter == null}">
                                <tr>
                                    <td colspan="4">无数据</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td>${inviter.id}</td>
                                    <td>${inviter.mobile}</td>
                                    <td>${inviter.nickname}</td>
                                    <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${user.createTime}"/></td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <h5>我邀请的</h5>
                <div class="panel panel-default m-b-none">
                    <table class="table text-center table-bordered table-striped m-b-none">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>手机</th>
                            <th>昵称</th>
                            <th>邀请时间</th>
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
                                <td>${item.mobile}</td>
                                <td>${item.nickname}</td>
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
