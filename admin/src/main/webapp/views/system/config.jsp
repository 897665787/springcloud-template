<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xxw
  Date: 9/10/2018
  Time: 11:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>配置管理</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/treeview.jsp" %>
    <%@include file="../common/cropper.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<%-- index参数的设置要在content_nav.jsp包含之前。jsp:include不可用，具体原因：未解之谜 --%>
<c:set var="index" value="system_config"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">配置管理</h1>
        </div>
        <div class="wrapper-md">
            <div class="row">
                <div>
                    <ul class="nav nav-tabs">
                        <c:forEach items="${configCategoryParents}" var="item">
                            <li <c:if test="${item.id==categoryParentId}">class="active"</c:if>>
                                <a href="#home" aria-controls="home" data-toggle="tab"
                                   onclick="window.location.href='<%=request.getContextPath()%>/admin/system/config?categoryParentId=${item.id}'">
                                        ${item.name}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                    <div class="tab-content" style="margin-top: 10px">
                        <div class="panel-group" role="tablist" aria-multiselectable="true">
                            <c:forEach items="${configCategories}" var="category" varStatus="status">
                            	<div class="panel panel-default">
                                    <div class="panel-heading" role="tab">
                                        <h4 class="panel-title" style="display: inline-block;">
                                            <a role="button" data-toggle="collapse" href="#${category.id}"
                                               aria-expanded="true" aria-controls="collapseOne">
                                                    ${category.name}
                                            </a>
                                        </h4>
                                    </div>
                                    <div id="${category.id}" class="panel-collapse collapse in" role="tabpanel">
                                        <div class="panel-body">
                                            <div style="margin-bottom: 15px">
                                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_config_create')})">
                                                <a href="#" onclick="showCreateModal('${category.id}');return false"
                                                   class="btn btn-success">新增</a>
                                                    </sec:authorize>
                                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_config_update')})">
                                                    <button class="btn btn-info" type="button" onclick="save(${category.id})">保存</button>
                                                </sec:authorize>
                                            </div>
                                            <c:forEach items="${category.configs}" var="config">
                                                <form class="form-inline" style="margin-bottom: 20px">
                                                	<label class="control-label">
                                                        <h5 style="display: inline-block">${config.name}</h5>
                                                    </label>
	                                                <c:choose>
	                                                    <c:when test="${config.type==1}">
	                                                        <input id="${config.key}" type="number" class="form-control" style="width: 80px;" value="${config.value}">
	                                                    </c:when>
	                                                    <c:when test="${config.type==2}">
	                                                    	<label class="control-label">
		                                                    	<label class="i-switch i-switch-md m-t-xs">
		                                                            <input id="${config.key}" type="checkbox" ${config.value eq '1'?'checked':''}><i></i>
		                                                        </label>
		                                                    </label>
	                                                    </c:when>
	                                                    <c:when test="${config.type==3}">
	                                                        <input id="${config.key}" type="text" class="form-control" style="width: 300px;" value="${config.value}">
	                                                    </c:when>
	                                                    <c:when test="${config.type==5}">
	                                                        <input id="${config.key}" type="hidden" value="${config.value}" img>
	                                                    </c:when>
	                                                    <c:otherwise>
	                                                        <textarea id="${config.key}" type="text" class="form-control" style="width: 500px;height: 200px">${config.value}</textarea>
	                                                    </c:otherwise>
	                                                </c:choose>
                                                        ${config.comment}
                                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_config_delete')})">
                                                    <a href="#" class="btn btn-danger btn-xs"
                                                       onclick="deleteListItem('${config.id}');return false">
                                                        删除
                                                    </a>
                                                </sec:authorize>
                                                </form>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    $("input[img]").imageCrop()

    function save(categoryId) {
        var configs = [];
        $.each($("#" + categoryId).find("input[type!='checkbox']"), function(i,input){
            if ($(input).attr("id") != undefined) {
                config = {};
                config["key"] = $(input).attr("id");
                config["value"] = $(input).val();
                configs.push(config)
            }
        });
        $.each($("#" + categoryId).find("input[type='checkbox']"), function(i,checkbox){
            if ($(checkbox).attr("id") != undefined) {
                config = {};
                config["key"] = $(checkbox).attr("id");
                config["value"] = $(checkbox).is(":checked")?1:0;
                configs.push(config)
            }
        });
        $.each($("#" + categoryId).find("textarea"), function(i,textarea){
            if ($(textarea).attr("id") != undefined) {
                config = {};
                config["key"] = $(textarea).attr("id");
                config["value"] = $(textarea).val();
                configs.push(config)
            }
        });
        doJsonPost('<%=request.getContextPath()%>/admin/system/config/batchUpdate',
            JSON.stringify(configs),
            function (data) {
                if (data.status) {
                    bootoast({message: "保存成功！",timeout: 2});
                } else {
                    alert(data.message);
                }
            });
    }
</script>
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_config_create')})">
    <div class="modal fade" id="createModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">新增</h4>
                </div>
                <form name="createForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                            	<label class="control-label required">名称：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="name" type="text" maxlength="255" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">键：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="key" type="text" maxlength="191" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">值：</label>
                            </div>
                            <div class="col-xs-9" id="saveValue">
                                <input name="value" type="number" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">类型：</label>
                            </div>
                            <div class="col-xs-9">
                                <select name="type" class="form-control" data-ignore="true" onchange="changeType(this)">
                                    <xs:descriptionOptions clazz="system.Config" property="type"/>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label">备注：</label>
                            </div>
                            <div class="col-xs-9">
                                <textarea name="comment" rows="4" class="form-control"></textarea>
                            </div>
                        </div>
                        <input type="hidden" name="categoryId" id="categoryId">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button id="createSubmit" type="submit" class="btn btn-success">确定</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        function changeType(select) {
            var type = $("select").val();
            if (type == 1) {
                $("#saveValue").html("<input name=\"value\" type=\"number\" class=\"form-control\">");
            }
            else if (type == 2) {
                $("#saveValue").html("<label class=\"i-switch i-switch-md m-t-xs\"><input id=\"saveSwitch\" name=\"value\" type=\"checkbox\"><i></i></label>");
            }
            else if (type == 3) {
                $("#saveValue").html("<input name=\"value\" type=\"text\" class=\"form-control\")>");
            }
            else if (type == 4) {
                $("#saveValue").html("<textarea name=\"value\" rows=\"4\" class=\"form-control\"></textarea>");
            }
            else {
                $("#saveValue").html("<input id=\"saveImage\" name=\"value\" type=\"hidden\" img>");
                $("#saveImage").imageCrop()
            }
        }

        var $createForm = $("form[name=createForm]");
        var $createSubmit = $("#createSubmit");
        var createValidator = $createForm.validate({
            rules: {
                name: {
                    required: true,
                    notEmpty: true,
                    maxlength: 32
                },
                key: {
                    required: true,
                    notEmpty: true,
                    maxlength: 32
                }
            },
            messages: {
                name: {
                    required: "名称不能为空",
                    notEmpty: "名称不能为空",
                    maxlength: "名称最多32个字"
                },
                key: {
                    required: "键不能为空",
                    notEmpty: "键不能为空",
                    maxlength: "键最多32个字"
                }
            },
            submitHandler: function () {
                $createSubmit.attr("disabled", true);
                var params = $createForm.xsJson();
                if (params.type == 2) {
                    params["value"] = $("#saveSwitch").is(":checked")?1:0;
                }
                doPost("<%=request.getContextPath()%>/admin/system/config/save",
                    params,
                    function (data) {
                        $createSubmit.attr("disabled", false);
                        if (data.status) {
                            $("#createModel").modal("hide");
                            setTimeout(function () {
                                bootoast({message: "新增成功！"});
                                window.location.reload(true);
                            }, 380);

                        } else {
                            alert(data.message);
                        }
                    }, function (XMLHttpRequest, textStatus) {
                        $createSubmit.attr("disabled", false);
                        alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                    });
            }
        });

        function showCreateModal(categoryId) {
            $("#categoryId").val(categoryId);
            $("#createModel").modal("show");
        }
        $(function () {
            $("#createModel").on('hide.bs.modal', function () {
                $createForm.xsClean();
                createValidator.resetForm();
            })
        });
    </script>
</sec:authorize>
<sec:authorize access="hasAnyRole(${xs:getPermissions('system_config_delete')})">
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteListItem(id) {
            showDeleteModel(null, function () {
                doPost("<%=request.getContextPath()%>/admin/system/config/remove", {id: id}, function (data) {
                    if (data.status) {
                        bootoast({message: "删除成功！"});
                        setTimeout(function () {
                            window.location.reload(true);
                        }, 680);
                    } else {
                        alert(data.message);
                    }
                })
            })
        }
    </script>
</sec:authorize>
</html>
