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
	<title>用户详情</title>
	<%@include file="../common/head.jsp" %>
	<%@include file="../common/validate.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
	<div class="app-content-body">
		<jsp:include page="user_detail_nav.jsp">
			<jsp:param name="nav" value="1"/>
		</jsp:include>
		<div class="wrapper-md row">
			<div class="col-xs-12">
				<form id="createForm" class="form-horizontal">
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">编号：</label>
						</div>
						<div class="col-xs-5">
							<input name="id" type="text" maxlength="32" class="form-control" value="${user.id}" readonly>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">手机号：</label>
						</div>
						<div class="col-xs-5">
							<input name="mobile" type="text" maxlength="32" class="form-control" value="${user.mobile}" readonly>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">用户名：</label>
						</div>
						<div class="col-xs-5">
							<input name="username" type="text" maxlength="191" class="form-control" value="${user.username}" readonly>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label required">昵称：</label>
						</div>
						<div class="col-xs-5">
							<input name="nickname" type="text" maxlength="255" class="form-control" value="${user.nickname}" readonly>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">密码：</label>
						</div>
						<div class="col-xs-5">
							<input name="password" type="text" maxlength="32" class="form-control" value="${user.password}" readonly>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">微信unionid：</label>
						</div>
						<div class="col-xs-5">
							<input name="unionid" type="text" maxlength="32" class="form-control" value="${user.unionid}" readonly>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">微信openid：</label>
						</div>
						<div class="col-xs-5">
							<input name="openid" type="text" maxlength="32" class="form-control" value="${user.openid}" readonly>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">手机号区号：</label>
						</div>
						<div class="col-xs-5">
							<input name="areaCode" type="text" maxlength="255" class="form-control" value="${user.areaCode}" readonly>
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
							<input name="realname" type="text" maxlength="255" class="form-control" value="${user.realname}" readonly>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label required">客户ID：</label>
						</div>
						<div class="col-xs-5">
							<input name="customerId" type="text" maxlength="32" class="form-control" value="${user.customerId}" readonly>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">头像：</label>
						</div>
						<div class="col-xs-5">
							<xs:imageUploader identifier="getAvatar" name="avatar" folder="avatar" width="100" height="100"/>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">设备ID：</label>
						</div>
						<div class="col-xs-5">
							<input name="deviceId" type="text" maxlength="64" class="form-control" value="${user.deviceId}" readonly>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label required">注册类型：</label>
						</div>
						<div class="col-xs-5">
							<select name="operationType" class="form-control">
								<xs:descriptionOptions clazz="user.User" property="operationType" value="${user.operationType}"/>
							</select>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label required">注册平台：</label>
						</div>
						<div class="col-xs-5">
							<select name="operationPlatform" class="form-control">
								<xs:descriptionOptions clazz="user.User" property="operationPlatform" value="${user.operationPlatform}"/>
							</select>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">注册版本号：</label>
						</div>
						<div class="col-xs-5">
							<input name="operationVersion" type="text" maxlength="8" class="form-control" value="${user.operationVersion}" readonly>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">注册IP：</label>
						</div>
						<div class="col-xs-5">
							<input name="operationIp" type="text" maxlength="64" class="form-control" value="${user.operationIp}" readonly>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">性别：</label>
						</div>
						<div class="col-xs-5">
							<select name="sex" class="form-control">
								<xs:descriptionOptions clazz="user.User" property="sex" value="${user.sex}"/>
							</select>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">生日：</label>
						</div>
						<div class="col-xs-5">
							<input name="birthday" type="text" maxlength="32" class="form-control" value="${user.birthday}" readonly>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">身份证号码：</label>
						</div>
						<div class="col-xs-5">
							<input name="identityNumber" type="text" maxlength="32" class="form-control" value="${user.identityNumber}" readonly>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-xs-1 text-right">
							<label class="control-label">个性签名：</label>
						</div>
						<div class="col-xs-5">
							<input name="personalSignature" type="text" maxlength="256" class="form-control" value="${user.personalSignature}" readonly>
						</div>
						<div class="col-xs-1 text-right">
							<label class="control-label">绑定微信时间：</label>
						</div>
						<div class="col-xs-5">
							<input name="bindWeChatTime" type="text" class="form-control datepicker" readonly value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${user.bindWeChatTime}"/>">
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
    <c:if test="${user.avatar!=null and user.avatar!=''}">
    	putImageIntoImageUploader("getAvatar", "${user.avatar}");
    </c:if>
</script>
</body>
</html>
