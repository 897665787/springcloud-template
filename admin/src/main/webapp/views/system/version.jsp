<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by IntelliJ IDEA.
  User: gustinlau
  Date: 11/1/17
  Time: 4:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>版本管理</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
<body>
<%@include file="../common/header.jsp" %>
<%-- index参数的设置要在content_nav.jsp包含之前。jsp:include不可用，具体原因：未解之谜 --%>
<c:set var="index" value="system_version"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">版本管理</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
                        <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                            <label class="control-label">版本名称：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="dynamic[name]" type="text" class="form-control" placeholder="模糊查询"
                                   value="${search.dynamic.name}">
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                            <label class="control-label">平台：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <select name="platform" class="form-control" data-value="${search.platform}">
                                <option value="">全部</option>
                               <xs:dictOptions key="versionPlatform"/>
                            </select>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label">描述：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="dynamic[desc]" type="text" class="form-control" placeholder="模糊查询"
                                   value="${search.dynamic.desc}">
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label">状态：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3 m-b-md">
                            <select name="status" class="form-control" data-value="${search.status}">
                                <option value="">全部</option>
                                <xs:dictOptions key="onoff"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <sec:authorize access="hasAnyRole(${xs:getPermissions('system_version_create')})">
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
                            <th>版本号</th>
                            <th>名称</th>
                            <th>描述</th>
                            <th>平台</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${pageModel.list.size() eq 0}">
                            <tr>
                                <td colspan="6">无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${pageModel.list}" var="item">
                            <tr>
                                <td>${item.code}</td>
                                <td>${item.name}</td>
                                <td>${item.desc}</td>
                                <td><xs:dictDesc key="versionPlatform" value="${item.platform}"/></td>
                                <td>${item.status eq 0?"下架":"上架"}</td>
                                <td>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('system_version_update')})">
                                    <c:if test="${item.status eq 0}">
                                        <a href="#" class="btn btn-success btn-xs"
                                           onclick="simpleUpdateListItem('${item.id}',1);return false">
                                            上架
                                        </a>
                                    </c:if>
                                    <c:if test="${item.status eq 1}">
                                        <a href="#" class="btn btn-danger btn-xs"
                                           onclick="simpleUpdateListItem('${item.id}',0);return false">
                                            下架
                                        </a>
                                    </c:if>
                                    <a href="#" onclick="updateListItem('${item.id}');return false;"
                                       class="btn btn-info btn-xs">
                                        编辑
                                    </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyRole(${xs:getPermissions('system_version_delete')})">
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

<script>
  function uploadApk(type) {
      var formData=new FormData();
      formData.append("folders","apk");
      if(type==0){
          formData.append("files",$("#create_file").get(0).files[0]);
          uploadFile("<%=request.getContextPath()%>/admin/common/api/file/upload",formData,function (data) {
              if(data.status){
                  bootoast({message: "上传成功！", timeout: 1});
                  $("#create_url").val(data.data[0]);
              }else{
                  alert(data.message);
              }
          });
      }else{
          formData.append("files",$("#update_file").get(0).files[0]);
          uploadFile("<%=request.getContextPath()%>/admin/common/api/file/upload",formData,function (data) {
              if(data.status){
                  bootoast({message: "上传成功！", timeout: 1});
                  $("#update_url").val(data.data[0]);
              }else{
                  alert(data.message);
              }
          });
      }
  }
</script>

<sec:authorize access="hasAnyRole(${xs:getPermissions('system_version_create')})">
<div class="modal fade" id="createModel" data-backdrop="static" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">新增版本</h4>
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
                            <label class="control-label required">版本号：</label>
                        </div>
                        <div class="col-xs-9">
                            <input name="code" type="number" min="0" step="1" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-xs-3 text-right">
                            <label class="control-label required">平台：</label>
                        </div>
                        <div class="col-xs-9">
                            <select name="platform" class="form-control">
                                <xs:dictOptions key="versionPlatform"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-xs-3 text-right">
                            <label class="control-label required">URL：</label>
                        </div>
                        <div class="col-xs-6">
                            <input id="create_url" name="url" type="text" class="form-control">
                        </div>
                        <div class="col-xs-3">
                            <a href="#" onclick="$('#create_file').click();return false;" class="btn btn-success">上传</a>
                            <input id="create_file" type="file" style="display: none" onchange="uploadApk(0)">
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-xs-3 text-right">
                            <label class="control-label required">状态：</label>
                        </div>
                        <div class="col-xs-9">
                            <select name="status" class="form-control">
                                <xs:dictOptions key="onoff"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-xs-3 text-right">
                            <label class="control-label required">描述：</label>
                        </div>
                        <div class="col-xs-9">
                            <textarea style="height: 200px;resize: none;" name="desc" maxlength="255" type="text" class="form-control"></textarea>
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
                notEmpty: true,
                maxlength: 255
            },
            code: {
                required: true,
                digits: true
            },
            url: {
                required: true,
                notEmpty: true
            },
            desc: {
                required: true,
                notEmpty: true,
                maxlength: 255
            }
        },
        messages: {
            name: {
                required: "名称不能为空",
                notEmpty: "名称不能为空",
                maxlength: "名称最多255个字"
            },
            code: {
                required: "版本号不能为空",
                digits: "版本号只能为数字"
            },
            url: {
                required: "URL不能为空",
                notEmpty: "URL不能为空"
            },
            desc: {
                required: "描述不能为空",
                notEmpty: "描述不能为空",
                maxlength: "描述最多255个字"
            }
        },
        submitHandler: function () {
            $createSubmit.attr("disabled", true);
            doPost("<%=request.getContextPath()%>/admin/system/version/save",
                $createForm.serialize(),
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

<sec:authorize access="hasAnyRole(${xs:getPermissions('system_version_update')})">
<div class="modal fade" id="updateModel" data-backdrop="static" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">编辑版本</h4>
            </div>
            <form name="updateForm" class="form-horizontal" style="max-width: 800px">
                <div class="modal-body">
                    <input type="hidden" name="id">
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
                            <label class="control-label required">版本号：</label>
                        </div>
                        <div class="col-xs-9">
                            <input name="code" type="number" min="0" step="1" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-xs-3 text-right">
                            <label class="control-label required">平台：</label>
                        </div>
                        <div class="col-xs-9">
                            <select name="platform" class="form-control">
                                <xs:dictOptions key="versionPlatform"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-xs-3 text-right">
                            <label class="control-label required">URL：</label>
                        </div>
                        <div class="col-xs-6">
                            <input id="update_url" name="url" type="text" class="form-control">
                        </div>
                        <div class="col-xs-3">
                            <a href="#" onclick="$('#update_file').click();return false;" class="btn btn-success">上传</a>
                            <input id="update_file" type="file" style="display: none" onchange="uploadApk(1)">
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-xs-3 text-right">
                            <label class="control-label required">状态：</label>
                        </div>
                        <div class="col-xs-9">
                            <select name="status" class="form-control">
                                <xs:dictOptions key="onoff"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-xs-3 text-right">
                            <label class="control-label required">描述：</label>
                        </div>
                        <div class="col-xs-9">
                            <textarea style="height: 200px;resize: none;" name="desc" maxlength="255" type="text" class="form-control"></textarea>
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
    var updateValidator = $updateForm.validate({
        rules: {
            name: {
                required: true,
                notEmpty: true,
                maxlength: 255
            },
            code: {
                required: true,
                digits: true
            },
            url: {
                required: true,
                notEmpty: true
            },
            desc: {
                required: true,
                notEmpty: true,
                maxlength: 255
            }
        },
        messages: {
            name: {
                required: "名称不能为空",
                notEmpty: "名称不能为空",
                maxlength: "名称最多255个字"
            },
            code: {
                required: "版本号不能为空",
                digits: "版本号只能为数字"
            },
            url: {
                required: "URL不能为空",
                notEmpty: "URL不能为空"
            },
            desc: {
                required: "描述不能为空",
                notEmpty: "描述不能为空",
                maxlength: "描述最多255个字"
            }
        },
        submitHandler: function () {
            $updateSubmit.attr("disabled", true);
            doPost("<%=request.getContextPath()%>/admin/system/version/update",
                $updateForm.serialize(),
                function (data) {
                    $updateSubmit.attr("disabled", false);
                    if (data.status) {
                        $("#updateModel").modal("hide");
                        setTimeout(function () {
                            bootoast({message: "更新成功！"});
                            window.location.reload(true);
                        }, 380);

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
        doPost("<%=request.getContextPath()%>/admin/system/version/get", {id: id}, function (data) {
            if (data.status) {
                $updateForm.xsSetForm(data.data);
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

    function simpleUpdateListItem(id, status) {
        doPost('<%=request.getContextPath()%>/admin/system/version/update',
            {
                id: id,
                status: status
            }, function (data) {
                if (data.status) {
                    window.location.reload(true);
                } else {
                    alert(data.message);
                }
            });
    }

</script>
</sec:authorize>


<sec:authorize access="hasAnyRole(${xs:getPermissions('system_version_delete')})">
<%@include file="../common/deleteConfirm.jsp" %>
<script>
    function deleteListItem(id) {
        showDeleteModel(null, function () {
            doPost("<%=request.getContextPath()%>/admin/system/version/remove", {id: id}, function (data) {
                if (data.status) {
                    setTimeout(function () {
                        bootoast({message: "删除成功！"});
                        window.location.reload(true);
                    }, 380);
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
