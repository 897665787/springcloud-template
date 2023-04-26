<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>{module_name}</title>
    <%@include file="../common/head.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="{module}_{modelName}"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">新增</h1>
            <button class="btn btn-success pull-right" onclick="submitForm()">确定</button>
            <a href="${backUrl}" class="btn btn-default pull-right" style="margin-right: 5px">返回</a>
        </div>
        <div class="wrapper-md row">
            <div class="col-xs-12">
                <form class="form-horizontal" id="createForm">
                    <div class="form-group">
{save_form}                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    var $createForm = $("#createForm");
    $createForm.validate({
        rules: {
{create_validate_rules}        },
        messages: {
{create_validate_messages}        }
    });

    function submitForm(status) {
        var valid = $createForm.valid();
        if(!valid){
            return;
        }

        doPost('<%=request.getContextPath()%>/admin/{module}/{modelName}/save', param, function (data) {
            if (data.status) {
                bootoast({message: "新增成功！"});
                window.location.href = '${backUrl}';
            } else {
                alert(data.msg);
            }
        });
    }
</script>
</body>
</html>