<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: gustinlau
  Date: 10/30/17
  Time: 3:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    if(request.getParameter("id")==null){
        throw new Exception("imageUploader.jsp需要id参数");
    }
    if(request.getParameter("name")==null){
        throw new Exception("imageUploader.jsp需要name参数");
    }
    if(request.getParameter("folder")==null){
        throw new Exception("imageUploader.jsp需要folder参数");
    }
%>

<div id="previewDiv_${param.id}" class="img-preview"></div>
<input id="input_${param.id}" class="hidden" type="file"
       onchange="uploadPreview_${param.id}(this)"
       accept="image/png,image/jpg,image/jpeg,image/bmp,image/gif"/>
<input id="url_${param.id}" type="hidden" name="${param.name}"/>

<script>
    var images_${param.id} = [];
    var $previewDiv_${param.id} = $("#previewDiv_${param.id}");
    var $input_${param.id} = $("#input_${param.id}");
    var $url_${param.id} = $("#url_${param.id}");
    var lock_${param.id} = false;

    <%--更新选择器--%>
    function updatePreviewDiv_${param.id}() {
        var html = "";
        $.each(images_${param.id}, function (index, image) {
            html += "<label>" +
                "<img src='" + image + "'/>" +
                "<a class='delete' href='#' onclick='deleteUpload_${param.id}(" + index + ");return false;'>删除" +
                "</a>" +
                "</label>";
        });
        if (images_${param.id}.length === 0) {
            html += "<label>" +
                "<a id='addImage_${param.id}' class='add' href='#' onclick='selectImage_${param.id}();return false;'>" +
                "<i class='fa fa-plus'></i>" +
                "</a>" +
                "</label>";
        }
        $previewDiv_${param.id}.html(html);
    }
    <%--选择图片--%>
    function selectImage_${param.id}() {
        if (!lock_${param.id})
            $input_${param.id}.click();
    }
    <%--初始化--%>
    $(function () {
        updatePreviewDiv_${param.id}();
    });

    function uploadPreview_${param.id}(file) {
        if (file.files && file.files[0]) {
            lock_${param.id} = true;
            $("#addImage_${param.id}").html("<i class='fa fa-spinner fa-pulse'></i>");

            imageUpload("<%=request.getContextPath()%>/admin/common/api/file/upload", '${param.folder}', file.files[0], function (data) {
                lock_${param.id} = false;
                if (data.status) {
                    var url = data.data[0];
                    images_${param.id}.push(url);
                    $url_${param.id}.val(url);
                } else {
                    alert(data.message);
                }
                updatePreviewDiv_${param.id}();
            }, function (res) {
                alert("请求失败：" + res.statusText + "\n错误码：" + res.status);
                lock_${param.id} = false;
            });
            $input_${param.id}.val("");
        }
    }

    <%--删除图片--%>
    function deleteUpload_${param.id}(index) {
        images_${param.id}.splice(index, 1);
        updatePreviewDiv_${param.id}();
    }
</script>