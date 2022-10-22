<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: gustinlau
  Date: 11/1/17
  Time: 4:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>意见反馈</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
<body>
<%@include file="../common/header.jsp" %>
<%-- index参数的设置要在content_nav.jsp包含之前。jsp:include不可用，具体原因：未解之谜 --%>
<c:set var="index" value="marketing_feedback"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">意见反馈</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">内容：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="dynamic[content]" type="text" class="form-control" placeholder="模糊查询"
                                   value="${search.dynamic.content}">
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label">状态：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3 m-b-md">
                            <select name="status" class="form-control" data-value="${search.status}">
                                <option value="">全部</option>
                                <xs:dictOptions key="feedbackStatus"/>
                            </select>
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
                            <th>反馈人</th>
                            <th>手机号</th>
                            <th>内容</th>
                            <th>反馈时间</th>
                            <th>状态</th>
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
                                <td>${item.name}</td>
                                <td>${item.mobile}</td>
                                <td>${item.content}</td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd"
                                                     value="${item.createTime}"/></td>
                                <td><xs:dictDesc key="feedbackStatus" value="${item.status}"/></td>
                                <td>
                                    <c:if test="${item.status eq 0}">
                                        <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_feedback_solve')})">
                                        <a href="#" class="btn btn-success btn-xs"
                                           onclick="simpleUpdateListItem('${item.id}');return false">
                                            解决
                                        </a>
                                        </sec:authorize>
                                    </c:if>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_feedback_delete')})">
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
<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_feedback_solve')})">
<script>
    function simpleUpdateListItem(id) {
        doPost("<%=request.getContextPath()%>/admin/marketing/feedback/solve",{id:id},function (data) {
            if(data.status){
                bootoast({message: "标记成功！"});
                window.location.reload(true);
            }else{
                alert(data.message);
            }
        })
    }
</script>
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_feedback_delete')})">
<%@include file="../common/deleteConfirm.jsp" %>
<script>
    function deleteListItem(id) {
        showDeleteModel(null, function () {
            doPost("<%=request.getContextPath()%>/admin/marketing/feedback/remove", {id: id}, function (data) {
                if (data.status) {
                    setTimeout(function () {
                        bootoast({message: "删除成功！"});
                        window.location.reload(true);
                    }, 380);
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
