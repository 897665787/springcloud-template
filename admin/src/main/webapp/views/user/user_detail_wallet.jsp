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
            <jsp:param name="nav" value="5"/>
        </jsp:include>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">iOS钱包：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="iosWallet" type="number" class="form-control" value="${user.iosWallet}" readonly>
                        </div>
                        <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_wallet_update')})">
                            <div class="col-xs-4 col-md-3 col-lg-2 no-padder m-b-md text-right">
                                <label class="control-label">更新（负数为减少）：</label>
                            </div>
                            <div class="col-xs-8 col-md-4 col-lg-2  m-b-md">
                                <input id="iosWallet" name="changeFee" type="number" class="form-control">
                            </div>
                            <div class="col-xs-8 col-md-4 col-lg-1  m-b-md">
                                <button class="btn btn-success" type="button" onclick="updateWallet(1)">确认</button>
                            </div>
                        </sec:authorize>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">Android钱包：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="androidWallet" type="number" class="form-control" value="${user.androidWallet}" readonly>
                        </div>
                        <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_wallet_update')})">
                            <div class="col-xs-4 col-md-3 col-lg-2 no-padder m-b-md text-right">
                                <label class="control-label">更新（负数为减少）：</label>
                            </div>
                            <div class="col-xs-8 col-md-4 col-lg-2  m-b-md">
                                <input id="androidWallet" name="changeFee" type="number" class="form-control">
                            </div>
                            <div class="col-xs-8 col-md-4 col-lg-1  m-b-md">
                                <button class="btn btn-success" type="button" onclick="updateWallet(2)">确认</button>
                            </div>
                        </sec:authorize>
                    </div>
                </form>
                <div class="panel panel-default m-b-none">
                    <table class="table text-center table-bordered table-striped m-b-none">
                        <thead>
                        <tr>
                            <th>平台</th>
                            <th>金额</th>
                            <th>事件</th>
                            <th>变化前</th>
                            <th>变化后</th>
                            <th>操作者</th>
                            <th>操作时间</th>
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
                                <td><xs:descriptionDesc clazz="user.wallet.WalletHistory" property="platform" value="${item.platform}"/></td>
                                <td>
                                    <c:if test="${item.type == 2}">
                                        -
                                    </c:if>
                                    <fmt:formatNumber pattern="0.##" value="${item.fee}"/>
                                </td>
                                <td>${item.eventDesc}</td>
                                <td><fmt:formatNumber pattern="0.##" value="${item.changeBefore}"/></td>
                                <td><fmt:formatNumber pattern="0.##" value="${item.changeAfter}"/></td>
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
    function updateWallet(platform) {
        var params = {};
        params.id = '${user.id}';
        params.changeFee = platform == 1 ? $("#iosWallet").val() : $("#androidWallet").val();
        params.platform = platform;
        if (params.changeFee == undefined || params.changeFee == 0) {
            alert("金额必填且不能为0");
            return;
        }
        doPost('<%=request.getContextPath()%>/admin/user/wallet/update',
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
