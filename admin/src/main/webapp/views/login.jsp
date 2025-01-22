<%--
  Created by IntelliJ IDEA.
  User: JQ棣
  Date: 2017-04-07
  Time: 23:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>登录</title>
    <%@include file="./common/head.jsp"%>
    <%@include file="./common/validate.jsp"%>
    <style>
        label.error{
            position: absolute;
            top: 0;
            right: 12px;
            line-height: 48px;
            margin: 0;
            color: #f05050;
        }
    </style>
</head>
<body>
<div class="app app-header-fixed ">
    <div class="container w-xxl w-auto-xs">
        <div class="text-center m-t-lg">
            <img style="width: 215px" src="<%=request.getContextPath()%>/assets/img/logo.png"/>
        </div>
        <div class="m-b-lg">
            <div class="text-center">
                <h3>管理后台</h3>
            </div>
            <form name="form" class="form-validation" method="post">
                <div id="errorText" class="text-danger wrapper text-center" style="height: 50px">

                </div>
                <div class="list-group list-group-sm">
                    <div class="list-group-item">
                        <input type="text" name="username" placeholder="用户名" value="system" class="form-control no-border"
                               required>
                    </div>
                    <div class="list-group-item">
                        <input type="password" placeholder="密　码" name="password" value="666666" class="form-control no-border"
                               required>
                    </div>
                </div>
                <button type="submit" class="btn btn-lg btn-info btn-block">登录</button>
                <div class="line line-dashed"></div>
            </form>
        </div>
    </div>
</div>
<script>
    var submitBtn=$("button[type='submit']");
    $("form[name='form']").validate({
        rules: {
            username:{
                required:true
            },
            password:{
                required:true
            }
        },
        messages: {
            username:{
                required:"用户名不能为空"
            },
            password:{
                required:"密码不能为空"
            }
        },
        submitHandler: function (form) {
            submitBtn.attr("disabled",true);
            doPost("<%=request.getContextPath()%>/admin/security/login", $(form).serialize(),
                function (data) {
                    if (data.status) {
                    	if(data.url){
                    		window.location.href = data.url
                    	} else {
	                        window.location.href = "<%=request.getContextPath()%>/admin/index"
                    	}
                    } else {
                        $("#errorText").html(data.message);
                    }
                    submitBtn.attr("disabled",false);
                }, function (XMLHttpRequest, textStatus) {
                    submitBtn.attr("disabled",false);
                    alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                });
        }
    });
</script>
</body>
</html>
