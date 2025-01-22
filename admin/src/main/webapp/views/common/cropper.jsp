<%--
  图片裁剪工具
  User: JQ棣
  Date: 29/8/2018
  Time: 11:30 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/plugins/cropper/cropper.min.css">
<script src="<%=request.getContextPath()%>/assets/plugins/cropper/cropper.min.js"></script>

<%--
    使用方法：
    1、引入依赖（先引入jQuery）
    <%@include file="../common/cropper.jsp" %>

    2、
    <input id="avatarImage" type="hidden" name="avatar">

    3、
    <script>
       $("#avatarImage").imageCrop({
            iscrop: 1, //是否裁剪
            width: 100,
            height: 100,
            folder: "common",
            aspectRatio: 1  //裁剪框的宽高比，如：16/9
        })
    </script>

    ps:
    可编辑回显：
    <input id="avatarImage" type="hidden" name="avatar" value="${avatar}">
    不可编辑回显：
    <input id="avatarImage" type="hidden" name="avatar" value="${avatar}" readonly>
--%>

<div class="modal" id="cropperModal" role="dialog" data-backdrop="static" style="z-index: 999999">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="img-container">
                    <img id="cropImageContainer" style="max-width: 100%;">
                </div>
            </div>
            <div class="modal-footer">
                <div id="importWait" class="pull-left text-success none"><i class='fa fa-spinner fa-pulse'></i>  图片处理中，请稍等...</div>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-success js-image-crop">确认裁剪</button>
            </div>
        </div>
    </div>
</div>

<script id="imgPreviewTemplate" type="text/html">
    <div class="img-preview">
        <label style="width:{{width}};height:{{height}};">
            <a class="add js-image-add">
                <i class="fa fa-plus" style="line-height:{{height}}"></i>
            </a>
        </label>
        <input class="js-image-upload hidden" type="file" accept="image/png,image/jpg,image/jpeg,image/bmp,image/gif"/>
    </div>
</script>

<script id="onlyreadView" type="text/html">
    <div class="img-preview">
        <label style="width:{{width}};height:{{height}};">
            <img src="{{link}}"/>
        </label>
    </div>
</script>

<script id="onlyreadViewNoData" type="text/html">
    <div class="img-preview">
        <label style="width:{{width}};height:{{height}};">
            <span class="add" style="color:#aaa;">
                <i class="fa fa-plus" style="line-height:{{height}}"></i>
            </span>
        </label>
    </div>
</script>

<script>

    ;(function ($, window, document, undefined) {
        // ecma262v5 的新东西, 强制使用严谨的代码编写.
        "use strict";

        var pluginName = 'imageCrop';

        var $modal = $('#cropperModal');
        var incre = 0; //全局自增ID
        var n = function(a, e) {
            this.ele = a,
            this.defaults = {
                iscrop: 0, //是否裁剪
                height: 100, //图片展示高度
                width: 100, //图片展示宽度
                folder: "common", //图片存放文件夹
                aspectRatio: 1 //裁剪框的宽高比，如：16/9
            },
            this.opts = $.extend({}, this.defaults, e),
            this.opts.width = this.opts.width+"px";
            this.opts.height = this.opts.height+"px";
            this._init()
        };
        n.prototype = {
            _init: function () {
                this._destroy();
                //判断是否只读
                if(this.ele.prop("readonly")||this.ele.prop("disabled")){
                    //判断是否有值
                    if(this.ele.val()!=""){
                        this.opts.link = this.ele.val();
                        this.ele.after(renderTemplate($("#onlyreadView").html(), this.opts));
                    }else{
                        this.ele.after(renderTemplate($("#onlyreadViewNoData").html(), this.opts));
                    }
                }else{
                    this.ele.after(renderTemplate($("#imgPreviewTemplate").html(), this.opts));
                    //判断是否有值
                    if(this.ele.val()!=""){
                        var html = "<img src='" + this.ele.val() + "'/>" +
                            "<a class='delete js-image-del'>删除</a>";
                        this.ele.next(".img-preview").find("label").html(html);
                    }
                    this.ele.attr("cropid", "crop_"+(incre++));
                    this._eventBind();
                }
            },
            _eventBind: function() {
                var b = this.opts,
                    $view = this.ele.next(".img-preview");

                $view.on("click", ".js-image-add", function () {
                    !$(this).hasClass("disabled") && $view.find(".js-image-upload").click();
                })

                $modal.on("click", ".js-image-crop", function () {
                    $("#importWait").removeClass("none");
                    $modal.find("button").prop("disabled", true);

                    var canvas = $("#cropImageContainer").cropper('getCroppedCanvas', {
                        fillColor: '#fff',
                        imageSmoothingQuality: 'high',
                    });
                    var _this = $("[cropid="+$modal.data("crop")+"]");
                    var filename = $view.find(".js-image-upload").val().replace(/.*(\/|\\)/, "");

                    canvas.toBlob(function (blob) {
                        var formData = new FormData();
                        formData.append("folders", b.folder);
                        formData.append("files", blob, filename);
                        uploadFile("<%=request.getContextPath()%>/admin/common/api/file/upload", formData, function (data) {
                            if ( data.status) {
                                var html = "<img src='" + data.data[0] + "'/>" +
                                    "<a class='delete js-image-del'>删除</a>";
                                $view.find("label").html(html);
                                _this.val(data.data[0]);
                            } else {
                                alert(data.message);
                                var html = "<a class=\"add js-image-add\">\n" +
                                    "           <i class=\"fa fa-plus\" style=\"line-height:"+b.height+"\"></i>\n" +
                                    "       </a>";
                                $view.find("label").html(html);
                            }
                            $modal.modal('hide');
                        });
                    }, "image/jpeg", 0.8)

                })

                $view.on("change", ".js-image-upload", function (e) {
                    if (this.files && this.files[0]) {
                        var add = $view.find(".js-image-add");
                        add.addClass("disabled");
                        //是否裁剪
                        if(b.iscrop){
                            var filePath;
                            //获取图片本地路径
                            if (URL) {
                                filePath = URL.createObjectURL(this.files[0]);
                            } else if (FileReader) {
                                var reader = new FileReader();
                                reader.onload = function (e) {
                                    filePath = reader.result;
                                };
                                reader.readAsDataURL(this.files[0]);
                            }

                            $("#cropImageContainer").prop("src", filePath);
                            //初始化cropper
                            $("#cropImageContainer").cropper({
                                viewMode: 2, //视图模式，自动填充未铺满空间
                                aspectRatio: b.aspectRatio, //裁剪框的宽高比，如：16/9
                                dragMode: 'none', //拖拽模式，可移动图片,
                                zoomOnWheel: false, //是否允许使用鼠标滚轮缩放原图
                            });
                            $modal.data("crop", $view.prev().attr("cropid"));
                            $modal.modal("show");
                            add.removeClass("disabled");
                        }else{
                            add.html("<i class='fa fa-spinner fa-pulse' style='font-size: 35px;line-height:"+b.height+"'></i>");
                            imageUpload("<%=request.getContextPath()%>/admin/common/api/file/upload", b.folder, this.files[0], function (data) {
                                add.removeClass("disabled");

                                if ( data.status) {
                                    var html = "<img src='" + data.data[0] + "'/>" +
                                        "<a class='delete js-image-del'>删除</a>";
                                    $view.find("label").html(html);
                                    $view.prev().val(data.data[0]);
                                } else {
                                    alert(data.message);
                                    var html = "<a class=\"add js-image-add\">\n" +
                                        "           <i class=\"fa fa-plus\" style=\"line-height:"+b.height+"\"></i>\n" +
                                        "       </a>";
                                    $view.find("label").html(html);
                                }
                            });
                            //若不设成空，则重新选择相同图片时就触发不了change操作
                            $(this).val("");

                        }
                    }
                })

                $view.on("click", ".js-image-del", function () {
                    $view.prev().val("");
                    var html = "<a class=\"add js-image-add\">\n" +
                        "           <i class=\"fa fa-plus\" style=\"line-height:"+b.height+"\"></i>\n" +
                        "       </a>";
                    $view.find("label").html(html);
                })
            },
            _destroy: function () {
                var $view = this.ele.next(".img-preview");
                $view.remove();
                /*$view.off("click", ".js-image-del");
                $view.off("change", ".js-image-upload");
                $view.off("click", ".js-image-add");
                $view.off("click", ".js-image-crop");*/
            },
            test: function () {
                //该方法用于测试
                var opt = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
                alert("第一个是：" + opt.a);
                alert("第二个是：" + opt.b);
                return this;
            },
            test2: function (p) {
                //该方法用于测试
                alert("第一个是：" + p.a);
                alert("第二个是：" + p.b);
                return this;
            }
        }

        function renderTemplate (template, obj) {
            return template.replace(/[{]{2}([^}]+)[}]{2}/g, function($0, $1) {
                if(typeof obj[$1] === 'undefined') return '';
                return obj[$1];
            });
        }

        function logError (message) {
            if (window.console) {
                window.console.error(message);
            }
        }

        $modal.on('hidden.bs.modal', function () {
            $("#cropImageContainer").cropper('destroy');
            $("#importWait").addClass("none");
            $(".js-image-upload").val("");
            $modal.find("button").prop("disabled", false);
        });

        var old = $.fn.imageCrop; // 这里的 $.fn.imageCrop 有可能是之前已经有定义过的插件，在这里做无冲突处理使用。

        $.fn.imageCrop2 = $.fn.imageCrop = function(options) {

            var results = [];

            var $result = this.each(function () {

                var _this = $(this).data(pluginName);

                // 调用方法
                if (typeof options === "string") {

                    // call plugin method when first argument is a string
                    // get arguments for method
                    var args = Array.prototype.slice.call(arguments, 1);

                    if (!_this) {
                        logError('Not initialized, can not call method : ' + options);
                    }

                    var fn = _this[options];
                    //私有方法（前缀"_"）不能被外部调用
                    if (!$.isFunction(fn) || options.charAt(0) === '_'){
                        logError('No such method : ' + options);
                    } else {
                        // 执行插件的方法
                        results.push(fn.apply(_this, args));
                    }
                } else {
                    // 初始化
                    _this = new n($(this), $.isPlainObject(options) && options);
                    // 缓存插件
                    $(this).data(pluginName, _this);
                }
            })

            return (results.length) ? results : $result;
        }

        // 暴露类名, 可以通过这个为插件做自定义扩展
        $.fn.imageCrop.Constructor = n;
        // 扩展的方式
        // 设置 : $.fn.imageCrop.Constructor.newMethod = function(){}
        // 使用 : $ele.imageCrop("newMethod");

        // 无冲突处理
        $.fn.imageCrop.noConflict = function () {
            $.fn.imageCrop = old;
            return this
        };
    })(jQuery, window, document);

</script>