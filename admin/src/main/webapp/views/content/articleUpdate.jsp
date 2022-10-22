<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by IntelliJ IDEA.
  User: gustinlau
  Date: 10/30/17
  Time: 1:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>Title</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/tinymce.jsp" %>
    <%@include file="../common/treeview.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<%-- index参数的设置要在content_nav.jsp包含之前。jsp:include不可用，具体原因：未解之谜 --%>
<c:set var="index" value="content_article"/>
<%@include file="../common/content_nav.jsp" %>
<c:if test="${article eq null}">
    <script>
        alert("文章不存在");
        window.history.back();
    </script>
</c:if>
<div class="app-content ">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">编辑文章</h1>
            <a href="#" onclick="xs_goBack()"  class="btn btn-default pull-right">返回</a>
            <button class="btn btn-info pull-right" style="margin-right: 5px"
                    onclick="submitForm()">
                保存
            </button>
        </div>
        <div class="wrapper-md row">
            <div class="col-xs-12">
                <form class="form-horizontal" id="createForm">
                    <input type="hidden" name="id" value="${article.id}" >
                    <div class="form-group">
                        <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                            <label class="control-label required">标题：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="title" type="text" class="form-control" value="${article.title}"/>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label required">顺序：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="seq" type="number" class="form-control" value="${article.seq}"/>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label required">分类：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input type="text" name="category.name" class="form-control" readonly value="${article.category.name}"
                                   onclick="showCategoryModel()"/>
                            <input type="hidden" name="category.id" value="${article.category.id}">
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                            <label class="control-label required">类型：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <select name="type" class="form-control" onchange="typeChange(event)" data-value="${article.type}">
                                <xs:dictOptions key="articleType"/>
                            </select>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label required">是否上架：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3 m-b-md">
                            <select name="display" class="form-control" data-value="${article.display}">
                                <xs:dictOptions key="onoff"/>
                            </select>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label">是否热门：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3 m-b-md">
                            <select name="hot" class="form-control" data-value="${article.hot}">
                                <xs:dictOptions key="yesno"/>
                            </select>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                            <label class="control-label">seo标题：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="seoTitle" type="text" class="form-control" value="${article.seoTitle}"/>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                            <label class="control-label">seo关键字：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="seoKeywords" type="text" class="form-control" value="${article.seoKeywords}"/>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                            <label class="control-label">seo描述：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="seoDescription" type="text" class="form-control" value="${article.seoDescription}"/>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">图标：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3">
                            <xs:imageUploader identifier="articleIcon" name="image" folder="article"/>
                            <c:if test="${article.image ne null && article.image ne ''}">
                                <script>
                                    $(function () {
                                        putImageIntoImageUploader("articleIcon","${article.image}");
                                    });
                                </script>
                            </c:if>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label">简介：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-7  m-b-md">
                            <textarea name="intro" type="text" class="form-control" rows="3">${article.intro}</textarea>
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1  no-padder m-b-md text-right">
                            <label class="control-label">作者：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="author" type="text" class="form-control" value="${article.author}"/>
                        </div>
                    </div>
                    <div id="richText" style="position: relative">
                        <textarea id="container" style="width:80%;height: 500px">${article.content}</textarea>
                        <div id="extra"
                             style="position: absolute;width: 20%;height: 500px;right: 0;top: 0;padding: 0 10px;box-sizing: border-box">
                            <a class="btn btn-success btn-addon block m-b" href="javascript:void(0)"
                               onclick="openImageController()">
                                <i class="fa fa-file-image-o"></i> 插入图片
                            </a>
                            <a class="btn btn-success btn-addon block m-b" href="javascript:void(0)"
                               onclick="openVideoController()">
                                <i class="fa fa-film"></i> 插入视频
                            </a>
                            <a class="btn btn-success btn-addon block m-b" href="javascript:void(0)"
                               onclick="openAudioController()">
                                <i class="fa fa-music"></i> 插入音频
                            </a>
                            <a class="btn btn-success btn-addon block" href="javascript:void(0)"
                               onclick="openAccessoryController()">
                                <i class="fa  fa-file-zip-o"></i> 插入附件
                            </a>
                        </div>
                    </div>
                    <div id="link" style="display: none">
                        <div class="col-xs-4 col-md-2 col-lg-1   no-padder m-b-md text-right">
                            <label class="control-label required">链接：</label>
                        </div>
                        <div class="col-xs-8 col-md-10 col-lg-11 m-b-md">
                            <input name="url" type="text" class="form-control" value="${article.type eq '1'?article.content:''}"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="categoryModel" data-backdrop="static" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">文章分类</h4>
            </div>
            <div class="modal-body">
                <div id="tree">
                    <p style="text-align: center">加载中...</p>
                </div>
            </div>
        </div>
    </div>
</div>
<script id="treeData" type="text/plain">${categoryTree}</script>
<script type="text/javascript">
    tinymce.init({
        selector: '#container',
        language: 'zh_CN',
        plugins: "image media imagetools preview table hr link textcolor colorpicker lineheight letterspacing",
        removed_menuitems: 'image, media, visualaid, preview',
        toolbar1: 'undo redo | fontsizeselect | blockquote hr | removeformat | link unlink',
        toolbar2: 'bold italic underline forecolor backcolor | outdent indent | alignleft aligncenter alignright alignjustify | lineheight letterspacing | bullist numlist preview',
        // 图片工具
        imagetools_base: true,
        // 字体大小
        fontsize_formats: '12px 14px 15px 16px 18px 20px 24px',
        resize: false,
        branding: false,
        elementpath: false
    });
    var options = {
        tokenApi: "<%=request.getContextPath()%>/admin/editor/token",
        tokenMethod: "POST",
        encrypt: true,
        encryptKey: "${AES_KEY}",
        params: {token: '<sec:authentication property="principal.id"/>'}
    };

    function openImageController() {
        TinyMCEManager.imageManager.open(options, 'container');
    }

    function openVideoController() {
        TinyMCEManager.videoManager.open(options, 'container');
    }

    function openAudioController() {
        TinyMCEManager.audioManager.open(options, 'container');
    }

    function openAccessoryController() {
        TinyMCEManager.accessoryManager.open(options, 'container');
    }
</script>
<script>
    var $richText = $("#richText");
    var $link = $("#link");
    var $form = $("#createForm");
    var $categoryModel = $("#categoryModel");

    function showCategoryModel() {
        $categoryModel.modal("show");
    }

    function typeChange(e) {
        if ($(e.target).val() == 0) {
            $link.css("display", "none");
            $richText.css("display", "block");
        } else {
            $richText.css("display", "none");
            $link.css("display", "block");
        }
    }

    function submitForm() {
        var param = $form.xsJson();
        if(param.type == '0'){
            param.content = tinymce.activeEditor.getContent();
        }else if(param.type == '1'){
            param.content = param.url;
        }
        doPost('<%=request.getContextPath()%>/admin/content/article/article/update', param, function (data) {
                if (data.status) {
                    bootoast({message: "更新成功！"});
                    window.location.href = '${backUrl}';
                } else {
                    alert(data.message);
                }
            });
    }

    $(function () {
        var treeData = JSON.parse($("#treeData").text());
        $('#tree').treeview({
            data: treeData,
            onNodeSelected: function (event, data) {
                $form.xsSetInput("category.name", data.name);
                $form.xsSetInput("category.id", data.id);
                $categoryModel.modal("hide");
            }
        });
        $("#tree").treeview('selectNode','${article.category.id}');
        $("select[name='type']").trigger("change");
    });


</script>
</body>
</html>
