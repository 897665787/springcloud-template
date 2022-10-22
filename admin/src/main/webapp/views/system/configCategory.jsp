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
    <title>配置分类</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/treeview.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<%-- index参数的设置要在content_nav.jsp包含之前。jsp:include不可用，具体原因：未解之谜 --%>
<c:set var="index" value="system_configCategory"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">配置分类</h1>
            <sec:authorize access="hasAnyRole(${xs:getPermissions('system_configCategory_create')})">
                <button class="btn btn-success pull-right" type="button" onclick="showCreateModal()">新增</button>
            </sec:authorize>
        </div>
        <div class="wrapper-md">
            <div class="row">
                <div class="col-xs-12 col-md-5 col-lg-3">
                    <h4>配置分类</h4>
                    <div id="tree" class="xs-scrollbar" style="background-color: #fff; max-height: 650px; overflow: auto">
                        <p style="text-align: center">加载中...</p>
                    </div>
                </div>
                <div class="col-xs-12 col-md-7 col-lg-9">
                    <form name="saveForm" class="form-horizontal" style="max-width: 800px;margin-top: 40px">
                        <input name="id" type="hidden" >
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">名称：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="name" type="text" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">键：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="key" type="text" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label">备注：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="comment" type="text" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">父级：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="parentId" type="hidden">
                                <input name="parentName" type="text" class="form-control" readonly
                                       onclick="showCategoryTree(1)">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-12 text-right">
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_configCategory_update')})">
                                    <button class="btn btn-info" type="button" onclick="updateItem()">保存</button>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole(${xs:getPermissions('system_configCategory_delete')})">
                                    <button class="btn btn-danger" type="button" onclick="deleteItem()">删除</button>
                                </sec:authorize>
                            </div>
                        </div>
                    </form>
                    <div class="clearfix"></div>
                </div>
            </div>

        </div>
    </div>
</div>


<script>
    var $form = $("form[name=saveForm]");
    var treeData;
    var categoryTreeData;
    var selectedId;
    var categoryTreeCallBackType;
    $(function () {
        updateTree(true, false);
    });

    function updateTree(reset, remove) {
        if (reset) {
            $form.xsClean();
        }
        if (remove) {
            $("#tree").treeview("remove");
            $("#categoryTree").treeview("remove");
        }
        doPost("<%=request.getContextPath()%>/admin/system/configCategory/tree", {}, function (data) {
            if (data.status) {
                treeData = data.data;
                $('#tree').treeview({
                    data: treeData,
                    onNodeSelected: function (event, node) {
                        if (selectedId !== node.id) {
                            selectedId = node.id;
                            getItem(selectedId);
                        }
                    },
                    toggle: false
                });

                if (selectedId !== undefined) {
                    $("#tree").treeview("selectNode", selectedId);
                }
            } else {
                alert(data.message);
            }
        })
        doPost("<%=request.getContextPath()%>/admin/system/configCategory/parent-drop-down-list", {}, function (data) {
            if (data.status) {
                categoryTreeData = data.data;
                categoryTreeData.unshift({
                    "name": "无",
                    "id": ""
                })
                $("#categoryTree").treeview({
                    data: categoryTreeData,
                    onNodeSelected: function (event, node) {
                        if (categoryTreeCallBackType === 0) {//新增
                            $createForm.xsSetInput("parentName", node.name);
                            $createForm.xsSetInput("parentId", node.id);
                            $("#categoryModel").modal('hide');
                            $("#categoryTree").treeview("unselectNode", $("#categoryTree").treeview("getSelected")[0].id);
                        } else {//修改
                            if (selectedId === node.id) {
                                alert("不能选自己作为父级");
                                $("#categoryTree").treeview("unselectNode", $("#categoryTree").treeview("getSelected")[0].id);
                            } else {
                                $form.xsSetInput("parentName", node.name);
                                $form.xsSetInput("parentId", node.id);
                                $("#categoryModel").modal('hide');
                                $("#categoryTree").treeview("unselectNode", $("#categoryTree").treeview("getSelected")[0].id);
                            }
                        }
                    }
                });

            } else {
                alert(data.message);
            }
        })
    }

    function getItem(id) {
        doPost("<%=request.getContextPath()%>/admin/system/configCategory/find", {id: id}, function (data) {
            if (data.status) {
                $form.xsClean();
                var object = data.data;
                $form.xsSetForm(object);
                if (object.parentId !== undefined) {
                    $form.xsSetInput("parentId", object.parentId);
                    $form.xsSetInput("parentName", object.parentName);
                } else {
                    $form.xsSetInput("parentName", "无");
                }
            } else {
                alert(data.message);
                $form.xsEnable();
            }
        })
    }

    <sec:authorize access="hasAnyRole(${xs:getPermissions('system_configCategory_update')})">
    function updateItem() {
        doPost("<%=request.getContextPath()%>/admin/system/configCategory/update",
            $form.serialize(),
            function (data) {
                if (data.status) {
                    updateTree(false, true);
                    bootoast({message: "更新成功！", timeout: 1});
                } else {
                    alert(data.message);
                }
            });
    }
    </sec:authorize>
</script>

<sec:authorize access="hasAnyRole(${xs:getPermissions('system_configCategory_delete')})">
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteItem() {
            showDeleteModel(null, function () {
                doPost("<%=request.getContextPath()%>/admin/system/configCategory/delete", {id: selectedId}, function (data) {
                    if (data.status) {
                        bootoast({message: "删除成功！"});
                        setTimeout(function () {
                            window.location.reload(true);
                        }, 680);
                    } else {
                        alert(data.message);
                    }
                });
            });
        }
    </script>
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('system_configCategory_create')})">
    <div class="modal fade" id="createModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">新增配置分类</h4>
                </div>
                <form name="createForm" class="form-horizontal" style="max-width: 800px">
                    <div class="modal-body">
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">名称：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="name" type="text" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">键：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="key" type="text" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label">备注：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="comment" type="text" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-3 text-right">
                                <label class="control-label required">父级：</label>
                            </div>
                            <div class="col-xs-9">
                                <input name="parentId" type="hidden">
                                <input type="text" name="parentName" class="form-control" readonly data-value="无" value="无"
                                       onclick="showCategoryTree(0)">
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
        var createValidator = $createForm.validate({
            rules: {
                name: {
                    required: true,
                    notEmpty: true
                },
                key: {
                    required: true,
                    notEmpty: true
                }
            },
            messages: {
                name: {
                    required: "名称不能为空",
                    notEmpty: "名称不能为空"
                },
                key: {
                    required: "Key不能为空",
                    notEmpty: "Key不能为空"
                }
            },
            submitHandler: function () {
                $createSubmit.attr("disabled", true);
                doPost("<%=request.getContextPath()%>/admin/system/configCategory/save",
                    $createForm.serialize(),
                    function (data) {
                        $createSubmit.attr("disabled", false);
                        if (data.status) {
                            $("#createModel").modal("hide");
                            setTimeout(function () {
                                bootoast({message: "新增成功！", timeout: 1});
                                updateTree(false);
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

<%--分类树--%>
<div class="modal fade" id="categoryModel" data-backdrop="static" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">配置分类</h4>
            </div>
            <div class="modal-body">
                <div id="categoryTree">
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function showCategoryTree(type) {
        categoryTreeCallBackType = type;
        $("#categoryTree").treeview('expandAll');
        $("#categoryModel").modal('show');
    }
</script>
</body>
</html>
