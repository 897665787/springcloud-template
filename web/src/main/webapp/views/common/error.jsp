<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: JQ棣
  Date: 05/09/2017
  Time: 5:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1,  user-scalable=no"/>
    <title>Oops!!${message}</title>
</head>
<body>
<div id="404">
    <a href="<%=request.getContextPath()%>">
        <img style="width: 200px;display: block;margin: 40px auto 0 auto"
             src="<%=request.getContextPath()%>/assets/img/logo.png" alt="">
    </a>
    <h1 style="font-size:160px;font-weight: bold;text-align: center;color: #33AC79;margin-top: 40px;margin-bottom: 30px;text-shadow:5px 5px 10px #ddd;">
        ${code}</h1>
    <h1 style="font-size:30px;font-weight: normal;text-align: center;color: #888;">抱歉！</h1>
    <h2 style="font-size: 20px;font-weight: normal;text-align: center;color: #888;">${message}</h2>

    <p style="text-align: center;color: #888"><span id="second" style="padding: 0 10px">5秒后</span><a
            href="javascript:goback()" style="color: #33AC79;">返回</a></p>
</div>
<script src="<%=request.getContextPath()%>/assets/js/jquery.min.js"></script>
<script>
    var $second = $("#second");
    var second = 5;
    $(function () {
        countDown();
    });

    function countDown() {
        if (second > 0) {
            second--;
            $second.html(second + "秒后");
            setTimeout(function () {
                countDown();
            }, 1000);
        } else {
            goback();
        }
    }
    function goback() {
        <c:choose>
            <c:when test="${code == 403}">
                window.location.href = "<%=request.getContextPath()%>";
            </c:when>
            <c:otherwise>
                window.history.back();
            </c:otherwise>
        </c:choose>
    }
</script>
</body>
</html>
