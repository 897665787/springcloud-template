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
    <%@include file="../common/validate.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <jsp:include page="user_detail_nav.jsp">
            <jsp:param name="nav" value="6"/>
        </jsp:include>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">会员到期时间：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <c:choose>
                                <c:when test="${user.vip == 0}">
                                    <input name="vipExpire" type="text" class="form-control" value="不是会员" readonly>
                                </c:when>
                                <c:otherwise>
                                    <input name="vipExpire" type="text" class="form-control" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${user.vipExpire}"/>" readonly>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_vip_update')})">
                        <div class="col-xs-4 col-md-3 col-lg-2 no-padder m-b-md text-right">
                            <label class="control-label">更新（单位天，负数为减少）：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-2  m-b-md">
                            <input id="changeDuration" name="changeDuration" type="number" class="form-control">
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-1  m-b-md">
                            <button class="btn btn-success" type="button" onclick="updateDuration()">确认</button>
                        </div>
                        </sec:authorize>
                    </div>
                </form>
                <div class="panel panel-default m-b-none">
                    <table class="table text-center table-bordered table-striped m-b-none">
                        <thead>
                        <tr>
                            <th>套餐名称</th>
                            <th>套餐时长（天）</th>
                            <th>变化前</th>
                            <th>变化后</th>
                            <th>操作者</th>
                            <th>操作时间</th>
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
                                <td>${item.packageDesc}</td>
                                <td>${item.packageDuration}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.changeBefore == null}">
                                            非会员
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatDate pattern="yyyy-MM-dd" value="${item.changeBefore}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.changeAfter == null}">
                                            非会员
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatDate pattern="yyyy-MM-dd" value="${item.changeAfter}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${item.creator}</td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.completeTime}"/></td>
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
<script>
    function updateDuration() {
        var params = {};
        params.id = '${user.id}';
        params.changeDuration = $("#changeDuration").val();
        if (params.changeDuration == undefined || params.changeDuration == 0) {
            alert("时长必填且不能为0");
            return;
        }
        doPost('<%=request.getContextPath()%>/admin/user/vip/update',
            params,
            function (data) {
                if (data.status) {
                    bootoast({message: "更新成功！"});
                    setTimeout(function () {
                        window.location.reload();
                    }, 680);
                } else {
                    alert(data.message);
                }
            }
        );
    }
</script>
</html>
