<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<title>主页</title>
</head>
<body>
	欢迎来到首页！
	<c:set var="index" value="111111111111111111111" />
	${ index }

	<a href="<%=request.getContextPath()%>/my">我的</a>
	<a href="<%=request.getContextPath()%>/auth/login">登录</a>
</body>
</html>