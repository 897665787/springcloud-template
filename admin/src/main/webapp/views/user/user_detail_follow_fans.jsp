<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by JQ棣 on 2018/11/23.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
	<title>用户详情</title>
	<%@include file="../common/head.jsp" %>
	<%@include file="../common/page.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
	<div class="app-content-body">
		<jsp:include page="user_detail_nav.jsp">
			<jsp:param name="nav" value="3"/>
		</jsp:include>
		<div class="wrapper-md row">
			<div class="col-xs-6" align="center">
	            <div class="panel panel-default m-b-none" style="max-width: 600px;">
					<table id="followTable" class="table text-center table-bordered m-b-none">
						<thead>
						<tr>
							<th>TA的关注（<span id="followCount"></span>人）</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div id="followPagination" class="xs-pagination"></div>
				</div>
            </div>
            <div class="col-xs-6" align="center">
            	<div class="panel panel-default m-b-none" style="max-width: 600px;">
					<table id="fansTable" class="table text-center table-bordered m-b-none">
						<thead>
						<tr style="text-align: left;">
							<th>TA的粉丝（<span id="fansCount"></span>人）</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div id="fansPagination" class="xs-pagination"></div>
				</div>
            </div>
		</div>
	</div>
</div>
           
<script id="userTemplate" type="text/html">
	<tr>
		<td>
			<div class="media">
    			<div class="media-left">
       				<img class="media-object img-circle" alt="42x42" src="{{avatar}}" data-holder-rendered="true" style="width: 56px; height: 56px;">
    			</div>
    			<div class="media-body media-middle">
        			<div class="media-heading font-bold" style="font-size: 13px;text-align: left;">{{nickname}}</div>
        			<div style="font-size: 12px;text-align: left;">{{createTime}}</div>
    			</div>
			</div>
		</td>
	</tr>
</script>

<script type="text/javascript">
$(function(){
	$("#followPagination").pagination({
		url: "<%=request.getContextPath()%>/admin/user/userFollow/list",
		params: {
            userId: '${user.id}'
		},
		tableId: "#followTable",
		trId: "#userTemplate",
        isTogo : false,
        count : 5,
        callbackAfter: function (data) {
            $("#followCount").html(data.total);
        }
	});

	$("#fansPagination").pagination({
		url: "<%=request.getContextPath()%>/admin/user/userFollow/list",
		params: {
            targetId: '${user.id}'
		},
		tableId: "#fansTable",
		trId: "#userTemplate",
        isTogo : false,
        count : 5,
        callbackAfter: function (data) {
            $("#fansCount").html(data.total);
        }
	});

});
</script>
</body>
</html>
