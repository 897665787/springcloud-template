<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>意见反馈</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="marketing_feedback"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">反馈</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">姓名：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="name" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.name}">
                    	</div>
                    	<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                        	<label class="control-label">内容：</label>
                    	</div>
                    	<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                        	<input name="content" type="text" class="form-control" placeholder="模糊查询"
                               	value="${search.content}">
                    	</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">是否解决：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<select name="status" class="form-control" data-value="${search.status}">
								<option value="">全部</option>
								<xs:descriptionOptions clazz="marketing.Feedback" property="status"/>
							</select>
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
                        		<th>姓名</th>
                        		<th>手机</th>
                        		<th>标题</th>
                        		<th>内容</th>
                        		<th>是否解决</th>
								<th>创建/更新时间</th>
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
                            	<td>${item.name}</td>
                            	<td>${item.mobile}</td>
                            	<td>${item.title}</td>
                            	<td>${item.content}</td>
                                <td><xs:descriptionDesc clazz="marketing.Feedback" property="status" value="${item.status}"/></td>
                                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.createTime}"/><br/><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${item.updateTime}"/></td>
                                <td>
                                    <c:if test="${item.status eq 0}">
                                        <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_feedback_solve')})">
                                        <a href="#" class="btn btn-success btn-xs"
                                           onclick="updateListItem('${item.id}',1);return false">
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
        function updateListItem(id,status) {
        	var params = {id: id, status: status};
            doPost("<%=request.getContextPath()%>/admin/marketing/feedback/update", params,
                    function (data) {
                        if (data.status) {
                            bootoast({message: "更新成功！"});
                            setTimeout(function () {
                                window.location.reload(true);
                            }, 680);
                        } else {
                            alert(data.message);
                        }
                    }, function (XMLHttpRequest, textStatus) {
                        alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                    });
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