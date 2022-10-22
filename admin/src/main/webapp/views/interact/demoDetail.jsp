<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>Demo详情</title>
    <%@include file="../common/head.jsp" %>
    <script src="<%=request.getContextPath()%>/assets/js/cookie.js"></script>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="interact_demo"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
		<div class="bg-light lter b-b padder-md">
			<a class="xs-nav text-base">Demo详情</a>
			<sec:authorize access="hasAnyRole(${xs:getPermissions('interact_comment')})">
			<a class="xs-nav text-base">评论区 (${demo.commentNum})</a>
			</sec:authorize>
			<a onclick="delCookie(cookie_key);xs_goBack();" class="btn btn-default pull-right" style="margin-top: 10px">返回</a>
		</div>
        <div class="js-panel wrapper-md row">
            <div class="col-xs-12">
                <form class="form-horizontal">
					<div class="form-group">
                    	<div class="col-xs-1 no-padder text-right">
                        	<label class="control-label">评论数</label>
                    	</div>
                    	<div class="col-xs-3">
                        	<input name="commentNum" type="number" class="form-control" value="${demo.commentNum}" readonly>
                    	</div>
                    	<div class="col-xs-1 no-padder text-right">
                        	<label class="control-label">点赞数</label>
                    	</div>
                    	<div class="col-xs-3">
                        	<input name="praiseNum" type="number" class="form-control" value="${demo.praiseNum}" readonly>
                    	</div>
					</div>
					<div class="form-group">
                    	<div class="col-xs-1 no-padder text-right">
                        	<label class="control-label">收藏数</label>
                    	</div>
                    	<div class="col-xs-3">
                        	<input name="collectNum" type="number" class="form-control" value="${demo.collectNum}" readonly>
                    	</div>
					</div>
                </form>
            </div>
        </div>
        <sec:authorize access="hasAnyRole(${xs:getPermissions('interact_comment')})">
		<div class="js-panel wrapper-md row">
			<div class="col-xs-12">
				<jsp:include page="../interact/comment.jsp">
					<jsp:param name="module" value="2"/>
					<jsp:param name="relativeId" value="${demo.id}"/>
				</jsp:include>
			</div>
		</div>
		</sec:authorize>
    </div>
</div>
<script>
	var cookie_key = 'tab_demo_key';
	$(function () {
		var cookie_value = getCookie(cookie_key);
		if(!cookie_value){
			cookie_value = 0;
		}
		selectPanel(cookie_value);
        $(".xs-nav").on("click", function () {
            var index = $(this).index();
            selectPanel(index);
        })
    })
    
    function selectPanel(index){
		$(".xs-nav").removeClass("xs-active");
		$(".xs-nav").eq(index).addClass("xs-active");
        $(".js-panel").addClass("none");
        $(".js-panel").eq(index).removeClass("none");
        setCookie(cookie_key,index,'1d');
	}
	
</script>
</body>
</html>
