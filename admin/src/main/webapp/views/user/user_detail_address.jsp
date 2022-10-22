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
	<title>用户详情</title>
	<%@include file="../common/head.jsp" %>
	<%@include file="../common/validate.jsp" %>
</head>
<style>
    .box_label {
        padding: 4px 6px;
        font-size: 12px;
        color: #fff;
        border-radius: 3px;
        display: inline-block;
        max-width: 150px;
        height: 24px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin: 8px auto;
    }

    .sm_padder_box {
        padding-left: 10px !important;
        padding-right: 10px !important;
    }

    .address_box {
        background-color: white;
        margin: 0 5px 20px 5px;
        border: 0 solid hsla(0,0%,95%,.6);
        box-shadow: 0 0 15px 3px #AAAAAA;
    }

    .box_head {
        background-color: #8FA8E6;
        height: 40px;
        color: white;
    }

    .box_title {
        line-height: 40px;
        font-weight: bold;
    }

    .box_address_detail {
        height: 50px;
        padding-top: 10px;
    }
</style>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_manage"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
	<div class="app-content-body">
		<jsp:include page="user_detail_nav.jsp">
			<jsp:param name="nav" value="2"/>
		</jsp:include>
		<div class="wrapper-md row">
            <div class="col-xs-12">
                <form class="form-horizontal">
                    <c:if test="${user.addressList.size() eq 0}">
                        <div class="col-xs-12 col-md-6 col-lg-4 sm_padder_box">
                            <div class="col-xs-12 address_box">
                                <div class="row box_head">
                                    <div class="col-xs-12  text-center">
                                        <label class="box_title">无数据</label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-xs-12 text-left">
                                        <label class="control-label"> </label>
                                    </div>
                                    <div class="col-xs-12 text-left">
                                        <label class="control-label"> </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:forEach var="address" items="${user.addressList}">
                        <div class="col-xs-12 col-md-6 col-lg-4 sm_padder_box">
                            <div class="col-xs-12 address_box">
                                <div class="row box_head">
                                    <div class="col-xs-12">
                                        <label class="box_title">${address.mobile}&nbsp;&nbsp;&nbsp;&nbsp;${address.contacts}</label>
                                        <c:if test="${address.major eq 1}">
                                            <label class="box_label bg-primary pull-right">默认地址</label>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-xs-12 text-left">
                                        <label class="control-label">${address.region}</label>
                                    </div>
                                    <div class="col-xs-12 text-left box_address_detail">
                                        <span class="control-label">${address.address}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </form>
            </div>
		</div>
	</div>
</div>
</body>
</html>
