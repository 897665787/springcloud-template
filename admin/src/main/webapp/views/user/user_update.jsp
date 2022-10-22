<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by kunye on 2018/05/28.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>用户更新</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<c:if test="${user eq null}">
    <script>
        alert("用户不存在");
        window.history.back();
    </script>
</c:if>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">用户更新</h1>
            <a href="javascript:history.go(-1);" class="btn btn-default pull-right">返回</a>
            <button id="updateSubmit" type="button" class="btn btn-primary pull-right" style="margin-right: 5px"
                    onclick="submitForm()">
                保存
            </button>
        </div>
        <div class="wrapper-md row">
            <div class="col-xs-12">
                <form id="updateForm" class="form-horizontal">
					<input type="hidden" name="id" value="${user.id}">
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">手机号：</label>
						</div>
						<div class="col-xs-5">
							<input name="mobile" type="text" maxlength="32" class="form-control" value="${user.mobile}">
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">用户名：</label>
						</div>
						<div class="col-xs-5">
							<input name="username" type="text" maxlength="191" class="form-control" value="${user.username}">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label required">昵称：</label>
						</div>
						<div class="col-xs-5">
							<input name="nickname" type="text" maxlength="255" class="form-control" value="${user.nickname}">
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">密码：</label>
						</div>
						<div class="col-xs-5">
							<input name="password" type="text" maxlength="32" class="form-control" value="${user.password}">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">手机号区号：</label>
						</div>
						<div class="col-xs-5">
							<input name="areaCode" type="text" maxlength="255" class="form-control" value="${user.areaCode}">
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label required">状态：</label>
						</div>
						<div class="col-xs-5">
							<select name="status" class="form-control">
								<td><xs:descriptionOptions clazz="user.User" property="status" value="${user.status}"/></td>
							</select>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label required">类型：</label>
						</div>
						<div class="col-xs-5">
							<select name="type" class="form-control">
								<td><xs:descriptionOptions clazz="user.User" property="type" value="${user.type}"/></td>
							</select>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">真实姓名：</label>
						</div>
						<div class="col-xs-5">
							<input name="realname" type="text" maxlength="255" class="form-control" value="${user.realname}">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">性别：</label>
						</div>
						<div class="col-xs-5">
							<select name="sex" class="form-control">
								<xs:descriptionOptions clazz="user.User" property="sex" value="${user.sex}"/>
							</select>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">头像：</label>
						</div>
						<div class="col-xs-5">
							<xs:imageUploader identifier="updateAvatar" name="avatar" folder="avatar" width="100" height="100"/>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">生日：</label>
						</div>
						<div class="col-xs-5">
							<input name="birthday" type="text" maxlength="32" class="form-control" value="${user.birthday}">
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">身份证号码：</label>
						</div>
						<div class="col-xs-5">
							<input name="identityNumber" type="text" maxlength="32" class="form-control" value="${user.identityNumber}">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">个性签名：</label>
						</div>
						<div class="col-xs-5">
							<input name="personalSignature" type="text" maxlength="256" class="form-control" value="${user.personalSignature}">
						</div>
					</div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    <c:if test="${user.avatar!=null and user.avatar!=''}">
    	putImageIntoImageUploader("updateAvatar", "${user.avatar}");
    </c:if>
    $updateForm = $("#updateForm");
    var $updateSubmit = $("#updateSubmit");
    $updateForm.validate({
        rules: {
            mobile: {
                maxlength: 32
            },
            username: {
                maxlength: 191
            },
            nickname: {
                required: true,
                notEmpty: true,
                maxlength: 255
            },
            password: {
                maxlength: 32
            },
            unionid: {
                maxlength: 32
            },
            openid: {
                maxlength: 32
            },
            areaCode: {
                maxlength: 255
            },
            status: {
                required: true,
                number: true,
                digits: true
            },
            type: {
                required: true,
                number: true,
                digits: true
            },
            realname: {
                maxlength: 255
            },
            customerId: {
                required: true,
                notEmpty: true,
                maxlength: 32
            },
            avatar: {
                maxlength: 65535
            },
            deviceId: {
                maxlength: 64
            },
            registerType: {
                required: true,
                number: true,
                digits: true
            },
            registerPlatform: {
                required: true,
                number: true,
                digits: true
            },
            registerVersion: {
                maxlength: 8
            },
            registerIp: {
                maxlength: 64
            },
            sex: {
                number: true,
                digits: true
            },
            birthday: {
                maxlength: 32
            },
            identityNumber: {
                maxlength: 32
            },
            personalSignature: {
                maxlength: 256
            }
        },
        messages: {
            mobile: {
                maxlength: "手机号最多32个字"
            },
            username: {
                maxlength: "用户名最多191个字"
            },
            nickname: {
                required: "昵称不能为空",
                notEmpty: "昵称不能为空",
                maxlength: "昵称最多255个字"
            },
            password: {
                maxlength: "密码最多32个字"
            },
            unionid: {
                maxlength: "微信unionid最多32个字"
            },
            openid: {
                maxlength: "微信openid最多32个字"
            },
            areaCode: {
                maxlength: "手机号区号最多255个字"
            },
            status: {
                required: "状态不能为空",
                number: "状态必须为数字",
                digits: "状态必须为非负整数"
            },
            type: {
                required: "类型不能为空",
                number: "类型必须为数字",
                digits: "类型必须为非负整数"
            },
            realname: {
                maxlength: "真实姓名最多255个字"
            },
            customerId: {
                required: "客户ID不能为空",
                notEmpty: "客户ID不能为空",
                maxlength: "客户ID最多32个字"
            },
            avatar: {
                maxlength: "头像最多65535个字"
            },
            deviceId: {
                maxlength: "设备ID最多64个字"
            },
            registerType: {
                required: "注册类型不能为空",
                number: "注册类型必须为数字",
                digits: "注册类型必须为非负整数"
            },
            registerPlatform: {
                required: "注册平台不能为空",
                number: "注册平台必须为数字",
                digits: "注册平台必须为非负整数"
            },
            registerVersion: {
                maxlength: "注册版本号最多8个字"
            },
            registerIp: {
                maxlength: "注册IP最多64个字"
            },
            sex: {
                number: "性别必须为数字",
                digits: "性别必须为非负整数"
            },
            birthday: {
                maxlength: "生日最多32个字"
            },
            identityNumber: {
                maxlength: "身份证号码最多32个字"
            },
            personalSignature: {
                maxlength: "个性签名最多256个字"
            }
        }
    });

    function submitForm() {
        if ($updateForm.valid()) {
            var params = $updateForm.xsJson();
            if (params.avatar == undefined || params.avatar == null) {
                params.avatar = "";
            }
            $updateSubmit.attr("disabled", true);
            doPost('<%=request.getContextPath()%>/admin/user/update',
                params,
                function (data) {
                    $updateSubmit.attr("disabled", false);
                    if (data.status) {
                        bootoast({message: "更新成功！"});
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
