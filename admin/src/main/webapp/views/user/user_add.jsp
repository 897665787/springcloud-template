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
    <title>用户新增</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">用户新增</h1>
            <a href="javascript:history.go(-1);" class="btn btn-default pull-right">返回</a>
            <button id="createSubmit" type="button" class="btn btn-success pull-right" style="margin-right: 5px"
                    onclick="submitForm()">
                新增
            </button>
        </div>
        <div class="wrapper-md row">
            <div class="col-xs-12">
                <form id="createForm" class="form-horizontal">
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label required">昵称：</label>
						</div>
						<div class="col-xs-3">
							<input name="nickname" type="text" maxlength="20" class="form-control" placeholder="昵称最多20个字符">
						</div>
						<div class="col-xs-2 text-right">
							<label class="control-label required">手机号码：</label>
						</div>
						<div class="col-xs-3">
							<input name="mobile" type="text" maxlength="11" class="form-control" placeholder="请填11位手机号码">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">性别：</label>
						</div>
						<div class="col-xs-2">
							<select name="sex" class="form-control">
								<option value="">未知</option>
								<xs:descriptionOptions clazz="user.User" property="sex"/>
							</select>
						</div>
						<div class="col-xs-3 text-right">
							<label class="control-label required">状态：</label>
						</div>
						<div class="col-xs-2">
							<select name="status" class="form-control">
								<xs:descriptionOptions clazz="user.User" property="status" value="1"/>
							</select>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">头像：</label>
						</div>
						<div class="col-xs-3">
							<xs:imageUploader identifier="avatarId" name="avatar" folder="hlh/user" width="100" height="100"/>
						</div>
						<div class="col-xs-2 text-right">
							<label class="control-label">出生年份：</label>
						</div>
						<div class="col-xs-2">
							<input name="birthday" type="text" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">真实姓名：</label>
						</div>
						<div class="col-xs-3">
							<input name="realname" type="text" maxlength="20" class="form-control" placeholder="真实姓名最多20个字符">
						</div>
						<div class="col-xs-2 text-right">
							<label class="control-label">身份证：</label>
						</div>
						<div class="col-xs-3">
							<input name="identityNumber" type="text" maxlength="20" class="form-control">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">用户名：</label>
						</div>
						<div class="col-xs-3">
							<input name="username" type="text" maxlength="20" class="form-control" placeholder="用户名最多20个字符">
						</div>
						<div class="col-xs-2 text-right">
							<label class="control-label">密码：</label>
						</div>
						<div class="col-xs-3">
							<input name="password" type="text" class="form-control">
						</div>
					</div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    $createForm = $("#createForm");
    var $createSubmit = $("#createSubmit");
    $createForm.validate({
        rules: {
            mobile: {
                required: true,
                notEmpty: true
            },
            status: {
                required: true
            },
            nickname: {
                required: true,
                notEmpty: true
            }
        },
        messages: {
            mobile: {
                required: "手机号码不能为空",
                notEmpty: "手机号码不能为空",
            },
            status: {
                required: "状态不能为空"
            },
            nickname: {
                required: "昵称不能为空",
                notEmpty: "昵称不能为空"
            }
        }
    });

    function submitForm() {
        if ($createForm.valid()) {
            var params = $createForm.xsJson();
            $createSubmit.attr("disabled", true);
            doPost('<%=request.getContextPath()%>/admin/user/save',
                params,
                function (data) {
                    $createSubmit.attr("disabled", false);
                    if (data.status) {
                        bootoast({message: "新增成功！"});
                        setTimeout(function () {
                            history.go(-1);
                        }, 680);
                    } else {
                        alert(data.message);
                    }
                }
            );
        }
    }
</script>
</body>
</html>
