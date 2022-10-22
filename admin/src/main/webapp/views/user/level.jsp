<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by wjc on 2018/06/21.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>等级管理</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
    <%@include file="../common/colorpicker.jsp" %>
</head>

<style>
    .color-block{width: 100px; height: 24px; display: inline-block;}
</style>

<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_level"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">等级管理</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                        <div class="col-xs-1 no-padder m-b-md text-right">
                            <label class="control-label">等级：</label>
                        </div>
                        <div class="col-xs-2  m-b-md">
                            <input name="level" type="text" class="form-control" value="${search.level}">
                        </div>
                        <div class="col-xs-1 no-padder m-b-md text-right">
                            <label class="control-label">称号：</label>
                        </div>
                        <div class="col-xs-2  m-b-md">
                            <input name="dynamic[title]" type="text" class="form-control" placeholder="模糊搜索" value="${search.dynamic.title}">
                        </div>
                        <div class="col-xs-1 no-padder m-b-md text-right">
                            <label class="control-label">经验值：</label>
                        </div>
                        <div class="col-xs-5 m-b-md">
                            <div class="row">
                                <div class="col-xs-5">
                                    <input name="dynamic[minExp]" type="text" class="form-control" value="${search.dynamic.minExp}">
                                </div>
                                <label class="pull-left control-label" style="width: 15px">至</label>
                                <div class="col-xs-5">
                                    <input name="dynamic[maxExp]" type="text" class="form-control" value="${search.dynamic.maxExp}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <sec:authorize access="hasAnyRole(${xs:getPermissions('user_level_create')})">
                                <a href="#" onclick="showCreateModal();return false"
                                   class="btn btn-success pull-left">新增</a>
                            </sec:authorize>
                            <input class="btn btn-info pull-right" value="搜索" type="submit">
                            <input class="btn btn-default pull-right  m-r-sm" value="重置" type="button"
                                   onclick="$('#searchForm').xsClean()">
                        </div>
                    </div>
                </form>
                <div class="panel panel-default m-b-none">
                    <table class="table text-center table-bordered table-striped m-b-none">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th width="250px">颜色</th>
                            <th>等级</th>
                            <th>称号</th>
                            <th>经验值</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${pageModel.list.size() eq 0}">
                            <tr>
                                <td colspan="5">无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${pageModel.list}" var="item">
                            <tr>
                                <td>${item.id}</td>
                                <td><span class="color-block" style="background-color: ${item.color};"></span></td>
                                <td>${item.level}</td>
                                <td>${item.title}</td>
                                <td>${item.exp}</td>
                                <td>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_level_update')})">
                                        <a href="#" onclick="updateListItem('${item.id}');return false;"
                                           class="btn btn-info btn-xs">
                                            编辑
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('user_level_delete')})">
                                        <a href="#" class="btn btn-danger btn-xs"
                                           onclick="deleteListItem('${item.id}');return false">
                                            删除
                                        </a>
                                    </sec:authorize>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <xs:pagination pageModel="${pageModel}"/>
            </div>
        </div>
    </div>
</div>

<sec:authorize access="hasAnyRole(${xs:getPermissions('user_level_create')})">
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
                                <label class="control-label required">等级：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="level" type="text" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">称号：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="title" type="text" maxlength="50" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">经验值：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="exp" type="text" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">颜色：</label>
                            </div>
                            <div class="col-xs-9">
                                <div class="input-group col-xs-12">
                                    <input type="text"  class="form-control" readonly
                                           style="background-color: #000000;color: #ffffff;">
                                    <div id="createColorSelector" class="colorSelector input-group-addon" >
                                        <span style="background-color: #000000"></span>
                                        <input name="color" type="hidden" data-value="#000000" value="#000000"/>
                                    </div>
                                </div>
                            </div>
                        </div>
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
        var $createForm = $("form[name=createForm]");
        var $createSubmit = $("#createSubmit");

        $('#createColorSelector').ColorPicker({
            color: '#000000',
            onShow: function (colpkr) {
                $(colpkr).fadeIn(500);
                return false;
            },
            onHide: function (colpkr) {
                $(colpkr).fadeOut(500);
                return false;
            },
            onChange: function (hsb, hex, rgb) {
                $('#createColorSelector span').css('backgroundColor', '#' + hex);
                $('#createColorSelector').parent().children("input:first").css('backgroundColor', '#' + hex);
                $('#createColorSelector').children("input:first").val('#' + hex);
            }
        });

        var createValidator = $createForm.validate({
            onfocusout: function (element) {
                $(element).valid();
            },
            rules: {
                title: {
                    required: true,
                    notEmpty: true,
                    maxlength: 50
                },
                level: {
                    required: true,
                    number: true,
                    digits: true
                },
                exp: {
                    required: true,
                    number: true,
                    digits: true
                }
            },
            messages: {
                title: {
                    required: "称号不能为空",
                    notEmpty: "称号不能为空",
                    maxlength: "称号最多50个字"
                },
                level: {
                    required: "等级不能为空",
                    number: "等级必须为数字",
                    digits: "等级必须为非负整数"
                },
                exp: {
                    required: "经验值不能为空",
                    number: "经验值必须为数字",
                    digits: "经验值必须为非负整数"
                }
            },
            submitHandler: function () {
                $createSubmit.attr("disabled", true);
                doPost("<%=request.getContextPath()%>/admin/user/level/save",
                    $createForm.serialize(),
                    function (data) {
                        $createSubmit.attr("disabled", false);
                        if (data.status) {
                            $("#createModel").modal("hide");
                            bootoast({message: "新增成功！"});
                            setTimeout(function () {
                                window.location.reload(true);
                            }, 680);
                        } else {
                            alert(data.message);
                        }
                    }, function (XMLHttpRequest, textStatus) {
                        $createSubmit.attr("disabled", false);
                        alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                    });
            }
        });

        function showCreateModal() {
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


<sec:authorize access="hasAnyRole(${xs:getPermissions('user_level_update')})">
    <div class="modal fade" id="updateModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">编辑</h4>
                </div>
                <form name="updateForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
                        <input type="hidden" name="id">
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">等级：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="level" type="text" class="form-control">
                                <div class="text-warning m-t-xs"><i class="fa fa-warning"></i>&nbsp;修改可能导致用户异常，请谨慎操作</div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">称号：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="title" type="text" maxlength="50" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">经验值：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="exp" type="text" class="form-control">
                                <div class="text-warning m-t-xs"><i class="fa fa-warning"></i>&nbsp;修改可能导致用户异常，请谨慎操作</div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">颜色：</label>
                            </div>
                            <div class="col-xs-9">
                                <div class="input-group col-xs-12">
                                    <input name="value" type="text" class="form-control" readonly
                                           style="background-color: #000000;color: #ffffff;">
                                    <div id="updateColorSelector" class="colorSelector input-group-addon" >
                                        <span style="background-color: #000000"></span>
                                        <input name="color" type="hidden" data-value="#000000" value="#000000"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button id="updateSubmit" type="submit" class="btn btn-success">确定</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        var $updateForm = $("form[name=updateForm]");
        var $updateSubmit = $("#updateSubmit");

        $('#updateColorSelector').ColorPicker({
            color: '#000000',
            onShow: function (colpkr) {
                $(colpkr).fadeIn(500);
                return false;
            },
            onHide: function (colpkr) {
                $(colpkr).fadeOut(500);
                return false;
            },
            onChange: function (hsb, hex, rgb) {
                $('#updateColorSelector span').css('backgroundColor', '#' + hex);
                $('#updateColorSelector').parent().children("input:first").css('backgroundColor', '#' + hex);
                $('#updateColorSelector').children("input:first").val('#' + hex);
            }
        });

        var updateValidator = $updateForm.validate({
            onfocusout: function (element) {
                $(element).valid();
            },
            rules: {
                title: {
                    required: true,
                    notEmpty: true,
                    maxlength: 50
                },
                level: {
                    required: true,
                    number: true,
                    digits: true
                },
                exp: {
                    required: true,
                    number: true,
                    digits: true
                }
            },
            messages: {
                title: {
                    required: "称号不能为空",
                    notEmpty: "称号不能为空",
                    maxlength: "称号最多50个字"
                },
                level: {
                    required: "等级不能为空",
                    number: "等级必须为数字",
                    digits: "等级必须为非负整数"
                },
                exp: {
                    required: "经验值不能为空",
                    number: "经验值必须为数字",
                    digits: "经验值必须为非负整数"
                }
            },
            submitHandler: function () {
                $updateSubmit.attr("disabled", true);
                var params = $updateForm.xsJson();


                doPost("<%=request.getContextPath()%>/admin/user/level/update", params,
                    function (data) {
                        $updateSubmit.attr("disabled", false);
                        if (data.status) {
                            $("#updateModel").modal("hide");
                            bootoast({message: "更新成功！"});
                            setTimeout(function () {
                                window.location.reload(true);
                            }, 680);
                        } else {
                            alert(data.message);
                        }
                    }, function (XMLHttpRequest, textStatus) {
                        $updateSubmit.attr("disabled", false);
                        alert("请求失败：" + textStatus + "\n错误码：" + XMLHttpRequest.status);
                    });
            }
        });

        function updateListItem(id) {
            doPost("<%=request.getContextPath()%>/admin/user/level/get", {id: id}, function (data) {
                if (data.status) {
                    $updateForm.xsSetForm(data.data);

                    var color = data.data.color;
                    if(color != undefined){
                        $('#updateColorSelector').ColorPickerSetColor(color);
                        $('#updateColorSelector span').css('backgroundColor', color);
                        $('#updateColorSelector').parent().children("input:first").css('backgroundColor', color);
                        $('#updateColorSelector').children("input:first").val(color);
                    }

                    $("#updateModel").modal("show");
                } else {
                    alert(data.message);
                }
            });
        }

        $(function () {
            $("#updateModel").on('hide.bs.modal', function () {
                $updateForm.xsClean();
                updateValidator.resetForm();
            })
        });
    </script>
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('user_level_delete')})">
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteListItem(id) {
            showDeleteModel("删除后可能导致用户异常，请谨慎操作！", function () {
                doPost("<%=request.getContextPath()%>/admin/user/level/remove", {id: id}, function (data) {
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

</body>
</html>

