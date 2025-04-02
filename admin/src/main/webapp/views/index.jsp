<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Staff: JQ棣
  Date: 2019-01-11
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理后台</title>
    <%@include file="./common/head.jsp" %>
</head>
<body>
<%@include file="./common/header.jsp" %>
<c:set var="index" value="index"/>
<%@include file="./common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body no-padder">
        <div class="wrapper-md">
            <div class="col-lg-12 col-md-12 col-xs-12 no-padder">
                <div class="panel panel-default">
                    <div class="panel-heading pos-rlt b-b b-light">
                        公告栏
                    </div>
                    <div class="panel-body xs-scrollbar" style="min-height: 400px;overflow: auto;">
                        <p>
                            <strong>当前版本v1.0.0</strong><small>（2019-1-30）</small>
                            <a class="text-info" data-toggle="modal" data-target="#logModel">查看历史版本</a>
                        </p>
                        <h5>更新内容：</h5>
                        <ul class="padder-md">
                            <li class="m-xs">项目上线</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%-- 此处填写历史版本，按时间倒叙 --%>
<div class="modal fade" id="logModel" data-backdrop="static" role="dialog">
    <div class="modal-dialog" role="document" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">历史版本</h4>
            </div>
            <div class="modal-body" style="padding-top: 8px;">
                <ul>
                    <li class="m-xs">
                        <h5><strong>v1.2.0</strong><small class="padder">2019-04-09</small></h5>
                        <ul class="padder-md">
                            <li class="m-xs">更新内容示例1</li>
                            <li class="m-xs">更新内容示例2</li>
                            <li class="m-xs">更新内容示例3</li>
                        </ul>
                    </li>
                    <li class="m-xs">
                        <h5><strong>v1.1.0</strong><small class="padder">2019-02-14</small></h5>
                        <ul class="padder-md">
                            <li class="m-xs">更新内容示例1</li>
                            <li class="m-xs">更新内容示例2</li>
                            <li class="m-xs">更新内容示例3</li>
                        </ul>
                    </li>
                    <li class="m-xs">
                        <h5><strong>v1.0.0</strong><small class="padder">2019-01-11</small></h5>
                        <ul class="padder-md">
                            <li class="m-xs">项目上线</li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

</body>
</html>

