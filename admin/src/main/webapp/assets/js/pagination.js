!
    function($, window, document, undefined) {

        var pluginName = 'pagination';

        var n = function(a, e) {
            this.ele = a,
            this.defaults = {
                currentPage: 0,//当前页数
                totalPage: 0,//总页数
                totalRecord: 0,//总记录数
                isShow: !0,//是否显示首尾页
                isTogo: !0,//是否显示跳转
                count: 9,//分页器显示个数
                homePageText: "<i class=\"xs-pagination-icon xs-pagination-double-left\"></i>",//首页文本
                endPageText: "<i class=\"xs-pagination-icon xs-pagination-double-right\"></i>",//尾页文本
                prevPageText: "<i class=\"xs-pagination-icon xs-pagination-left\"></i>",//上一页文本
                nextPageText: "<i class=\"xs-pagination-icon xs-pagination-right\"></i>",//下一页文本
                url: "",//分页触发请求
                method: "get",//请求方法
                params: {},//请求参数
                tableId: "#pluTableId",//列表ID
                trId: "#pluTrId",//列表项模板ID
                callbackAfter: function () {},//分页点击触发后事件
                callbackCustom: undefined//自定义触发事件
            },
            this.opts = $.extend({}, this.defaults, e),
            this.current = this.opts.currentPage,
            this.total = this.opts.totalPage,
            this.init()
        };
        n.prototype = {
            init: function() {
                this.render(),
                this.eventBind()
            },
            render: function() {
                var t = this.opts,
                    a = this.current,
                    e = this.total,
                    i = this.getPagesTpl(),
                    n = this.ele.empty();
                this.isRender = !0,
                    this.pageRecord = '<div class="pagination-record">共'+t.totalRecord+'条记录</div>',
                    this.homePage = '<li class="xs-f xs-icon"><a class="ui-pagination-page-item" data-current="1" title="第一页">' + t.homePageText + "</a></li>",
                    this.prevPage = '<li class="xs-f xs-icon"><a class="ui-pagination-page-item" data-current="' + (a - 1) + '" title="上一页">' + t.prevPageText + "</a></li>",
                    this.nextPage = '<li class="xs-f xs-icon"><a class="ui-pagination-page-item" data-current="' + (a + 1) + '" title="下一页">' + t.nextPageText + "</a></li>",
                    this.endPage = '<li class="xs-f xs-icon"><a class="ui-pagination-page-item" data-current="' + e + '" title="最后一页">' + t.endPageText + "</a></li>",
                    this.totalPage = '<span class="pagination-total-count">'+a+'/'+e+'</span>',
                    this.extraPage = '<span class="pagination-change"></span>' +
                        '<input id="xsPage" type="number" min="1" step="1" max="'+e+'">' +
                        '<span class="pagination-change-page"></span>' +
                        '<a class="ui-pagination-page-submit submit">确定</a>',
                    this.checkPage(),
                this.isRender && n.html(this.pageRecord+"<ul>" + this.homePage + this.prevPage + i + this.nextPage + this.endPage + "</ul><div class=\"pagination-extra\">"+this.totalPage+this.extraPage+"</div>")
            },
            checkPage: function() {
                var t = this.opts,
                    a = this.total,
                    e = this.current;
                t.isShow || (this.homePage = this.endPage = ""),
                t.isTogo || (this.extraPage = ""),
                1 === e && (this.homePage=this.homePage.replace("xs-f","disabled"), this.prevPage=this.prevPage.replace("xs-f", "disabled")),
                e === a && (this.endPage=this.endPage.replace("xs-f","disabled"), this.nextPage=this.nextPage.replace("xs-f","disabled")),
                1 === a && (this.homePage=this.homePage.replace("xs-f","disabled"), this.prevPage=this.prevPage.replace("xs-f", "disabled"),this.endPage=this.endPage.replace("xs-f","disabled"), this.nextPage=this.nextPage.replace("xs-f","disabled")),
                a <= 1 && (this.isRender = !1)
            },
            getPagesTpl: function() {
                var t = this.opts,
                    a = this.total,
                    e = this.current,
                    i = "",
                    n = t.count;
                if (a <= n) for (g = 1; g <= a; g++) i += g === e ? '<li class="active"><a class="ui-pagination-page-item" data-current="' + g + '">' + g + "</a></li>": '<li><a class="ui-pagination-page-item" data-current="' + g + '">' + g + "</a></li>";
                else {
                    var s = n / 2;
                    if (e <= s) for (g = 1; g <= n; g++) i += g === e ? '<li class="active"><a class="ui-pagination-page-item" data-current="' + g + '">' + g + "</a></li>": '<li><a class="ui-pagination-page-item" data-current="' + g + '">' + g + "</a></li>";
                    else {
                        var r = Math.floor(s),
                            h = e + r,
                            o = e - r,
                            c = n % 2 == 0;
                        h > a && (c ? (o -= h - a - 1, h = a + 1) : (o -= h - a, h = a)),
                        c || h++;
                        for (var g = o; g < h; g++) i += g === e ? '<li class="active"><a class="ui-pagination-page-item active" data-current="' + g + '">' + g + "</a></li>": '<li><a class="ui-pagination-page-item" data-current="' + g + '">' + g + "</a></li>"
                    }
                }
                return i
            },
            setPage: function(p) {
                var t = p.current,
                    a = p.total,
                    b = p.totalRecord;
                return t === this.current && a === this.total && b === this.opts.totalRecord ? this.ele: (this.current = t, this.total = a, this.opts.totalRecord = b, this.render(), this.ele)
            },
            getPage: function() {
                return {
                    current: this.current,
                    total: this.total
                }
            },
            eventBind: function() {
                var a = this,
                    e = this.callback;
                this.ele.off("click").on("click", ".ui-pagination-page-item",
                    function() {
                        var i = $(this).data("current");
                        i > 0 && i <= a.total && a.current != i && (a.current = i, a.render(), e && "function" == typeof e && e.call(a, i))
                    })

                this.ele.on("click", ".ui-pagination-page-submit",
                    function() {
                        var i = Number($("#xsPage").val());
                        i > 0 && i <= a.total && a.current != i && (a.current = i, a.render(), e && "function" == typeof e && e.call(a, i))
                    })

            },
            trigger: function (t) {
                var a = this,
                    e = this.callback;
                if(typeof t == 'undefined') t = 1;
                e && "function" == typeof e && e.call(a, t)
            },
            callback: function(current) {
                var _this = this, a = this.opts, e = this.opts.callbackCustom;
                if(e && "function" == typeof e){
                    e(current);
                    return;
                }
                a.params.page = current;
                $.ajax({
                    type: a.method,
                    url: a.url,
                    data: a.params,
                    dataType: 'json',
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader('Accept', 'application/json');
                    },//这里设置header
                    success: function (data) {
                        if (data.status) {
                            _this.ele.pagination("setPage", {
                                current: current,
                                total: Math.ceil(data.data.total/10),
                                totalRecord: data.data.total
                            });
                            if(a.trId.substr(0,1)==='#') a.trId = a.trId.substr(1);
                            renderData(a.tableId, data.data.list, a.trId);
                            a.callbackAfter(data.data);
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
        };

        function logError (message) {
            if (window.console) {
                window.console.error(message);
            }
        }

        $.fn.pagination = function(options) {

            var result = void 0;//效果同undefined，除了防止被重写外，还可以减少字节，void 0代替undefined省3个字节。

            // 在这里编写相应的代码进行处理
            var _this = this.data(pluginName);

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
                    result = fn.apply(_this, args);
                }
            } else {
                // 初始化
                _this = new n(this, $.isPlainObject(options) && options);
                // 缓存插件
                this.data(pluginName, _this);

                _this.trigger();
            }

            return result !== undefined ? result : this;
        }


    } (jQuery, window, document);


/**
 * 渲染列表数据
 * @param {String} ele 列表标识
 * @param {Array} data 数据
 * @param {String} temp 列表项模板ID，注意不用加 "#"
 * @param {Object} opt （可选）列表项处理配置 {itemName:function(itemVal){...;return realVal}}
 */
function renderData(ele, data, temp, opt) {
    var html = "";
    if(typeof data === 'undefined' || data.length==0){
        var cols = $(ele+" thead tr th").length;
        html = "<tr><td class='text-muted' colspan='"+cols+"'>暂无数据</td></tr>";
    }else {
        $.each(data, function (i, item) {
            if(typeof opt !== 'undefined'){
                for(var ite in item){
                    opt[ite] && "function" == typeof opt[ite] && (item[ite] = opt[ite](item[ite]))
                }
            }
            html += template(temp, item);
        });
    }
    $(ele+" tbody").html(html);
}