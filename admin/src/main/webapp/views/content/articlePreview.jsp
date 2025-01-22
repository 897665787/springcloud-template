<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: JQ棣
  Date: 10/30/17
  Time: 1:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>文章预览</title>
    <%@include file="../common/head.jsp" %>
</head>
<style>
    html, body {
        height: 100%;
    }
    .container {
        width: 1000px;
        min-height: 100%;
    }
</style>
<body>
<div class="container bg-white">
    <div class="m-b-xxl m-t-xxl">
        <h3 class="text-center">${article.title}</h3>
        <p class="text-muted text-center small">
            <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${article.createTime}"/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;来源：${article.author}
        </p>
        <div class="wrapper-lg">
            ${article.content}
        </div>
    </div>
</div>
</body>
</html>
