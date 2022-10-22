<%--
  Created by IntelliJ IDEA.
  User: gustinlau
  Date: 26/10/2017
  Time: 2:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<div class="app-aside app-aside-fixed hidden-xs bg-black">
    <div class="aside-wrap ">
        <div class="navi-wrap">
            <nav class="navi">
                <ul id="a_nav" class="nav">
                    <li class="${index eq "index"?"active":""}">
                        <a class="auto" href="<%=request.getContextPath()%>/admin">
                            <i class="fa fa-home fa-lg text-dark-lter"></i>
                            <span>首页</span>
                        </a>
                    </li>
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('content')})">
                        <li>
                            <a class="auto">
                                <i class="fa fa-photo text-danger"></i>
                                <span>图文</span>
                            </a>
                            <ul class="nav nav-sub">
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('content_article')})">
                                    <li class="${index eq "content_article"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/content/article/article">
                                            <span>文章管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('content_articleCategory')})">
                                    <li class="${index eq "content_articleCategory"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/content/article/category">
                                            <span>文章分类</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('content_image')})">
                                    <li class="${index eq "content_image"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/content/image">
                                            <span>图片管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('content_imageCategory')})">
                                    <li class="${index eq "content_imageCategory"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/content/imageCategory">
                                            <span>图片分类</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing')})">
                        <li>
                            <a class="auto">
                                <i class="fa fa-truck text-danger"></i>
                                <span>营销</span>
                            </a>
                            <ul class="nav nav-sub">
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_hotWord')})">
                                    <li class="${index eq "marketing_hotWord"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/marketing/hotWord">
                                            <span>热搜词</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_feedback')})">
                                    <li class="${index eq "marketing_feedback"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/marketing/feedback">
                                            <span>意见反馈</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('finance')})">
                        <li>
                            <a class="auto">
                                <i class="fa fa-rouble text-warning"></i>
                                <span>财务</span>
                            </a>
                            <ul class="nav nav-sub">
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('finance_withdrawal')})">
                                    <li class="${index eq "finance_withdrawal"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/user/withdrawal">
                                            <span>提现申请</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('system')})">
                        <li>
                            <a class="auto">
                                <i class="fa fa-wrench text-warning"></i>
                                <span>系统</span>
                            </a>
                            <ul class="nav nav-sub">
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_dict')})">
                                    <li class="${index eq "system_dict"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/dict">
                                            <span>字典管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_resource')})">
                                    <li class="${index eq "system_resource"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/secResource">
                                            <span>资源管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_role')})">
                                    <li class="${index eq "system_role"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/secRole">
                                            <span>角色管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <%--<sec:authorize access="hasAnyRole(${xs:getPermissions('system_organization')})">
                                    <li class="${index eq "system_organization"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/secOrganization">
                                            <span>组织管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>--%>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_staff')})">
                                    <li class="${index eq "system_staff"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/secStaff">
                                            <span>员工管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_country')})">
                                    <li class="${index eq "system_country"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/country">
                                            <span>国家管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_province')})">
                                    <li class="${index eq "system_province"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/province">
                                            <span>省份管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_city')})">
                                    <li class="${index eq "system_city"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/city">
                                            <span>城市管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_district')})">
                                    <li class="${index eq "system_district"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/district">
                                            <span>区县管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_log')})">
                                    <li class="${index eq "system_log"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/secStaffLog">
                                            <span>系统日志</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_version')})">
                                    <li class="${index eq "system_version"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/version">
                                            <span>版本管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_appInit')})">
                                    <li class="${index eq "system_appInit"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/appInit">
                                            <span>App初始化</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_configCategory')})">
                                    <li class="${index eq "system_configCategory"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/configCategory">
                                            <span>配置分类</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_config')})">
                                    <li class="${index eq "system_config"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/system/config">
                                            <span>配置管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('user')})">
                        <li>
                            <a class="auto">
                                <i class="fa fa-user text-success"></i>
                                <span>用户</span>
                            </a>
                            <ul class="nav nav-sub">
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('user_level')})">
                                    <li class="${index eq "user_level"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/user/level">
                                            <span>等级管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('user_user')})">
                                    <li class="${index eq "user_manage"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/user/list">
                                            <span>用户管理</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_wallet')})">
                                    <li class="${index eq "marketing_wallet"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/user/walletPackage">
                                            <span>钱包套餐</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('marketing_vip')})">
                                    <li class="${index eq "marketing_vip"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/user/vipPackage">
                                            <span>Vip套餐</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('stats')})">
                        <li>
                            <a class="auto">
                                <i class="glyphicon glyphicon-stats text-success"></i>
                                <span>统计</span>
                            </a>
                            <ul class="nav nav-sub">
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('stats_user')})">
                                    <li class="${index eq "stats_user"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/stats/user">
                                            <span>用户统计</span>
                                        </a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('interact')})">
                        <li>
                            <a class="auto">
                                <i class="fa fa-twitch  text-info"></i>
                                <span>互动</span>
                            </a>
                            <ul class="nav nav-sub">
                            	<li class="${index eq "interact_demo"?"active":""}">
                                  	<a href="<%=request.getContextPath()%>/admin/interact/demo">
                                      	<span>Demo</span>
                                  	</a>
                              	</li>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('loan')})">
                        <li>
                            <a class="auto">
                                <i class="fa fa-shopping-basket text-xs text-primary"></i>
                                <span>贷款超市</span>
                            </a>
                            <ul class="nav nav-sub">
                                 <sec:authorize access="hasAnyRole(${xs:getPermissions('loan_productTips')})">
                                    <li class="${index eq "loan_productTips"?"active":""}">
                                        <a href="<%=request.getContextPath()%>/admin/loan/productTips">
                                            <span>产品提示</span>
                                        </a>
                                    </li>
                                 </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <li>
                        <a class="auto">
                            <i class="fa fa-puzzle-piece text-dark-lter"></i>
                            <span>Demo</span>
                        </a>
                        <ul class="nav nav-sub">
                            <li class="${index eq "regionSelector"?"active":""}">
                                <a href="<%=request.getContextPath()%>/admin/plugins/regionSelector">
                                    <span>地区选择组件</span>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>