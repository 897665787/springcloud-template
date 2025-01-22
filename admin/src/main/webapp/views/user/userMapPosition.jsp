<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by JQ棣 on 2018/11/20.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>定位</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/validate.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
</head>
<style>
    .over_hidden {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
</style>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="user_userMapPosition"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
		<jsp:include page="user_detail_nav.jsp">
			<jsp:param name="nav" value="10"/>
		</jsp:include>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm">
                    <div class="form-group">
						<input name="userId" type="hidden" value="${search.userId}" data-value="${search.userId}">
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">位置：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input name="dynamic[address]" type="text" class="form-control" placeholder="模糊查询" value="${search.dynamic.address}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">开始时间：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input type="text" name="dynamic[createTimeBegin]" class="form-control datepicker" readonly value="${search.dynamic.createTimeBegin}">
						</div>
						<div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
							<label class="control-label">结束时间：</label>
						</div>
						<div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
							<input type="text" name="dynamic[createTimeEnd]" class="form-control datepicker" readonly value="${search.dynamic.createTimeEnd}">
						</div>
                    </div>
                    <div class="form-group m-t-n-md">
                        <div class="col-xs-12">
                            <input class="btn btn-info pull-right" value="搜索" type="submit">
                            <input class="btn btn-default pull-right  m-r-sm" value="重置" type="button"
                                   onclick="$('#searchForm').xsClean()">
                        </div>
                    </div>
                </form>
                <div class="col-xs-8">
                    <div class="col-xs-12 no-padder panel panel-default m-b-none">
                        <table class="table text-center table-bordered table-striped m-b-none">
                            <thead>
                            <tr>
                                <th>时间</th>
                                <th>位置</th>
                                <th>经纬度</th>
                                <th>备注</th>
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
                                <tr onclick="setPositionMark(this);">
                                    <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td class="over_hidden" title="${item.address}">${item.address}</td>
                                    <td class="lnglat">${item.position()}</td>
                                    <td class="over_hidden">${item.remark}</td>
                                    <td>
                                            <%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userContact_update')})">--%>
                                        <a href="#" onclick="updateListItem('${item.id}');return false;"
                                           class="btn btn-info btn-xs">
                                            编辑
                                        </a>
                                            <%--</sec:authorize>--%>
                                            <%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userContact_delete')})">--%>
                                        <a href="#" class="btn btn-danger btn-xs"
                                           onclick="deleteListItem('${item.id}');return false">
                                            删除
                                        </a>
                                            <%--</sec:authorize>--%>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <xs:pagination pageModel="${pageModel}"/>
                </div>
                <div id="map_container" class="col-xs-4 no-padder" style="height: calc(100vh - 280px)"></div>
            </div>
        </div>
    </div>
</div>



<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userMapPosition_update')})">--%>
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
                        <input type="hidden" name="userId">
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">位置：</label>
							</div>
							<div class="col-xs-9">
								<input name="address" type="text" maxlength="255" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label required">纬度：</label>
							</div>
							<div class="col-xs-9">
								<input name="lng" type="text" maxlength="255" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label">经度：</label>
							</div>
							<div class="col-xs-9">
								<input name="lat" type="text" maxlength="255" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-xs-3 text-right">
								<label class="control-label">备注：</label>
							</div>
							<div class="col-xs-9">
                               <textarea name="remark" class="form-control" maxlength="65535"
                                         style="padding: 5px; resize: none; height: 100px;"></textarea>
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
            onfocusout: function(element) { $(element).valid(); },
			rules: {
				userId: {
					required: true,
					notEmpty: true,
					maxlength: 128
				},
				address: {
					required: true,
					notEmpty: true,
					maxlength: 255
				},
				lng: {
					required: true,
					notEmpty: true,
					maxlength: 255
				},
				lat: {
					maxlength: 255
				},
				remark: {
					maxlength: 65535
				}
			},
			messages: {
				userId: {
					required: "用户id不能为空",
					notEmpty: "用户id不能为空",
					maxlength: "用户id最多128个字"
				},
				address: {
					required: "位置地址不能为空",
					notEmpty: "位置地址不能为空",
					maxlength: "位置地址最多255个字"
				},
				lng: {
					required: "纬度不能为空",
					notEmpty: "纬度不能为空",
					maxlength: "纬度最多255个字"
				},
				lat: {
					maxlength: "经度最多255个字"
				},
				remark: {
					maxlength: "备注最多65535个字"
				}
			},
            submitHandler: function () {
                $updateSubmit.attr("disabled", true);
                var params = $updateForm.xsJson();

                doPost("<%=request.getContextPath()%>/admin/user/userMapPosition/update", params,
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
            doPost("<%=request.getContextPath()%>/admin/user/userMapPosition/get", {id: id}, function (data) {
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
    </script>
<%--</sec:authorize>--%>

<%--<sec:authorize access="hasAnyRole(${xs:getPermissions('user_userMapPosition_delete')})">--%>
    <%@include file="../common/deleteConfirm.jsp" %>
    <script>
        function deleteListItem(id) {
            showDeleteModel(null, function () {
                doPost("<%=request.getContextPath()%>/admin/user/userMapPosition/remove", {
                    id: id,
                    userId: '${user.id}'
                }, function (data) {
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
<%--</sec:authorize>--%>

<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.11&key=${mapKey}"></script>
<script>
    //使备注文本框高度根据文本内容自适应
    $(function () {
        $(".remark_box").each(function () {
            $(this).css("height", this.scrollHeight);
            console.log("resize");
        });
    });

    var map = null;
    var marker = null;

    function setPositionMark(element) {
        var oldPosition = [0,0];
        if (marker != null) {
            oldPosition = [marker.getPosition().getLng(),marker.getPosition().getLat()];
        }
        var newPosition = $(element).find("td.lnglat:eq(0)").text().trim().split(",");
        if (oldPosition[0]==newPosition[0] && oldPosition[1]==newPosition[1]) {
            return false;
        }

        marker = new AMap.Marker({
            position: newPosition  // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
        });

        map = new AMap.Map('map_container', {
            center: newPosition,
            zoom: 11
        });
        map.add(marker);
    }
</script>
</body>
</html>

