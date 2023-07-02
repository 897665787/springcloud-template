<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<xs:menu value="${index}"/>
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