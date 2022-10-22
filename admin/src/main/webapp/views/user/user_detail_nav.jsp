<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by kunye on 2018/10/12
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="bg-light lter b-b padder-md">
    <a href="<%=request.getContextPath()%>/admin/user/get?id=${user.id}" class="xs-nav text-base ${param.nav eq 1?'xs-active':''}">用户详情</a>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_address')})">
    <a href="<%=request.getContextPath()%>/admin/user/address?id=${user.id}" class="xs-nav text-base ${param.nav eq 2?'xs-active':''}">用户地址</a>
    </sec:authorize>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_follow_fans')})">
    <a href="<%=request.getContextPath()%>/admin/user/follow-fans?id=${user.id}" class="xs-nav text-base ${param.nav eq 3?'xs-active':''}">关注/粉丝</a>
    </sec:authorize>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_creditLog')})">
    <a href="<%=request.getContextPath()%>/admin/user/creditLog?id=${user.id}" class="xs-nav text-base ${param.nav eq 4?'xs-active':''}">积分明细</a>
    </sec:authorize>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_wallet')})">
    <a href="<%=request.getContextPath()%>/admin/user/wallet?id=${user.id}" class="xs-nav text-base ${param.nav eq 5?'xs-active':''}">用户钱包</a>
    </sec:authorize>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_vip')})">
        <a href="<%=request.getContextPath()%>/admin/user/vip?id=${user.id}" class="xs-nav text-base ${param.nav eq 6?'xs-active':''}">用户会员</a>
    </sec:authorize>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_invite')})">
        <a href="<%=request.getContextPath()%>/admin/user/invite?id=${user.id}" class="xs-nav text-base ${param.nav eq 7?'xs-active':''}">用户邀请</a>
    </sec:authorize>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_contactBook')})">
        <a href="<%=request.getContextPath()%>/admin/user/userContactBook?userId=${user.id}" class="xs-nav text-base ${param.nav eq 8?'xs-active':''}">通讯录</a>
    </sec:authorize>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_contact')})">
        <a href="<%=request.getContextPath()%>/admin/user/userContact?userId=${user.id}" class="xs-nav text-base ${param.nav eq 9?'xs-active':''}">联系人</a>
    </sec:authorize>
    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_map_position')})">
        <a href="<%=request.getContextPath()%>/admin/user/userMapPosition?userId=${user.id}" class="xs-nav text-base ${param.nav eq 10?'xs-active':''}">定位</a>
    </sec:authorize>

    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user_get_card')})">
        <a href="<%=request.getContextPath()%>/admin/user/card?id=${user.id}" class="xs-nav text-base ${param.nav eq 11?'xs-active':''}">提现资料</a>
    </sec:authorize>
    <a href="<%=request.getContextPath()%>/admin/user/list" class="btn btn-default pull-right" style="margin-top: 10px">返回</a>
</div>
