<%--
  Created by IntelliJ IDEA.
  User: JQ棣
  Date: 26/10/2017
  Time: 2:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<div class="app-header-fixed ">
    <div class="app-header navbar">
        <div class="navbar-header bg-black" style="max-height: 50px;height:50px;">
            <button class="pull-right visible-xs dk" data-toggle-class="show" data-toggle-target=".navbar-collapse">
                <i class="glyphicon glyphicon-cog"></i>
            </button>
            <button class="pull-right visible-xs" data-toggle-class="off-screen" data-toggle-target=".app-aside">
                <i class="glyphicon glyphicon-align-justify"></i>
            </button>
            <a href="<%=request.getContextPath()%>/" class="navbar-brand text-lt hidden-xs"
               style="display: block;margin: 0 auto">
                <img style="display: block;margin-top: 9px;max-height: 32px;max-width: 150px;"
                     src="<%=request.getContextPath()%>/assets/img/logo.png"/>
            </a>
        </div>
        <div class="collapse pos-rlt navbar-collapse box-shadow bg-white-only">
            <ul class="nav navbar-nav navbar-right">
                <li class="xs-flag dropdown">
                    <a class="xs-flag dropdown-toggle clear">
                        <span class="hidden-sm hidden-md"><sec:authentication property="principal.nickname"/></span>
                        <b class="caret"></b>
                    </a>
                    <ul class="xs-flag dropdown-menu animated fadeInRight w">
                        <sec:authorize access="hasAnyRole(${xs:getPermissions('staff_update')})">
                            <li>
                                <a href="javascript:editPersonalInfo()">修改信息</a>
                            </li>
                        </sec:authorize>
                        <li>
                            <a href="javascript:logout()">登出</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
<script>
    function logout() {
        doPost("<%=request.getContextPath()%>/admin/logout", {}, function (data) {
            if (data.status) {
                window.location.href = "<%=request.getContextPath()%>/admin/login";
            }
        })
    }

    function xs_goBack() {
        <c:if test="${header.referer eq null}">
        window.history.back();
        </c:if>
        <c:if test="${header.referer ne null}">
        if ('${header.referer}' == window.location.href) {
            window.history.back();
        } else {
            window.location.href = "<%=request.getHeader("Referer")%>";
        }
        </c:if>
    }
</script>
<sec:authorize access="hasAnyRole(${xs:getPermissions('staff_update')})">
    <%--修改个人信息--%>
    <%@include file="validate.jsp" %>
    <div class="modal fade" id="editPersonalInfo" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">修改个人信息</h4>
                </div>
                <div class="modal-body">
                    <form name="editPersonalInfoForm" class="form-horizontal">
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">手机</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" maxlength="11" type="text" name="mobile"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">用户名</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="username"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label">密码</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="password" style="display: none"/>
                                <input class="form-control" type="password" name="password" maxlength="20"
                                       placeholder="留空则不修改"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">昵称</label>
                            </div>
                            <div class="col-xs-9">
                                <input class="form-control" type="text" name="nickname"/>
                            </div>
                        </div>
                    </form>
                    <div class="clearfix"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-info" onclick="submitEditPersonalInfoForm()">确定</button>
                </div>
            </div>
        </div>
    </div>
    <script>

        var $editPersonalInfoForm = $("form[name='editPersonalInfoForm']");
        var editPersonalInfoValidate = $editPersonalInfoForm.validate({
            rules: {
                mobile: {
                    required: true,
                    notEmpty: true
                },
                username: {
                    required: true,
                    notEmpty: true
                },
                password: {
                    minlength: 6,
                    maxlength: 20
                },
                nickname: {
                    required: true,
                    notEmpty: true
                }
            },
            messages: {
                mobile: {
                    required: "手机不能为空",
                    notEmpty: "手机不能为空"
                },
                username: {
                    required: "用户名不能为空",
                    notEmpty: "用户名不能为空"
                },
                password: {
                    minlength: "密码至少为6位",
                    maxlength: "密码至多位20位"
                },
                nickname: {
                    required: "昵称不能为空",
                    notEmpty: "昵称不能为空"
                }
            },
            submitHandler: function (form) {
                var params = $(form).xsJson();
                if (params.password == "") params.password = undefined;
                doPost("<%=request.getContextPath()%>/admin/security/secStaff/info/update", params, function (data) {
                    if (data.status) {
                        bootoast({message: "更新成功！"});
                        window.location.reload(true);
                    } else {
                        alert(data.message);
                    }
                });
            }
        });

        $('#editPersonalInfo').on('hide.bs.modal', function (e) {
            editPersonalInfoValidate.resetForm();
            $("#editPersonalInfo").find(".text-danger").removeClass("text-danger");
        });

        function editPersonalInfo() {
            doPost("<%=request.getContextPath()%>/admin/security/secStaff/info/get", {id: '<sec:authentication property="principal.id"/>'}, function (data) {
                if (data.status) {
                    var _data = data.data;
                    $editPersonalInfoForm.find("input[name='id']").val(_data.id);
                    $editPersonalInfoForm.find("input[name='mobile']").val(_data.mobile);
                    $editPersonalInfoForm.find("input[name='username']").val(_data.username);
                    $editPersonalInfoForm.find("input[name='nickname']").val(_data.nickname);
                    $("#editPersonalInfo").modal('show');
                } else {
                    alert(data.message);
                }
            });
        }

        function submitEditPersonalInfoForm() {
            $editPersonalInfoForm.submit();
        }
    </script>
</sec:authorize>