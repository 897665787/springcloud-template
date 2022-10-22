/**
 * 图片放大镜
 * Author : Postbird
 * Website: http://www.ptbird.cn
 * License: MIT
 */
var PostbirdImgGlass = {
    /**
     * 初始化操作
     * domSelector  - dom选择器           默认 img
     * width        - 放大后图片宽度       默认 auto
     * height       - 放大后图片高度       默认 auto
     * boxClassName - 放大镜的容器的class  默认 postbird-img-glass-box
     * boxBgColor   - 放大镜容器的背景色   默认  rgba(0,0,0,0.8)
     * animation    - 是否开启CSS3动画     默认 false IE10+ 才有效
     */
    init: function (objParam) {
        this.domSelector = objParam.domSelector || "img";
        this.width = objParam.width || "auto";
        this.height = objParam.height || "100%";
        this.boxClassName = objParam.boxClassName || 'postbird-img-glass-box';
        this.boxBgColor = objParam.bgColor || 'rgba(0,0,0,0.8)';
        this.animation = objParam.animation || true;
        this.startGlass();
        this.box = this.initImageClassContainer();
    },
    /**
     *  创建放大镜容器,并进行初始化参数和事件绑定 (在init中调用)
     *  返回创建的div
     *  包括一个 div 和 img
     */
    initImageClassContainer: function () {
        var _this = this,
            img = document.createElement('img');
        img.style.width = _this.width;
        img.style.height = _this.height;
        var box = document.createElement("div");
        box.style = _this.boxStyle;
        box.className = _this.boxClassName;
        box.appendChild(img);
        box.onclick = function () {
            _this.hideImageGlass();
        };
        document.body.insertBefore(box, document.body.childNodes[0]);
        this.initCssClass();
        return box;
    },
    /**
     * 开启放大镜 (在init中调用)
     * 使用 querySelectorAll 进行dom查找
     * 使用 addEventListener 进行事件绑定（因此需要IE9+）
     */
    startGlass: function () {
        var domList = document.querySelectorAll(this.domSelector);
        var _this = this;
        for (var i = 0, len = domList.length; i < len; i++) {
            domList[i].style.cursor = 'zoom-in';
            domList[i].addEventListener('click', function (event) {
                var target = event.target || event.srcElement;
                _this.showImageGlass(target.getAttribute('src'));
            }, false);
        }
    },
    /**
     * 显示放大镜
     */
    showImageGlass: function (src) {
        this.box.childNodes[0].src = src;
        this.box.style.display = 'block';
        if (this.animation) {
            this.addCssAnimation();
        }
    },
    /**
     * 隐藏放大镜
     */
    hideImageGlass: function () {
        this.box.style.display = 'none';
        if (this.animation) {
            this.removeCssAnimation();
        }
    },
    /**
     * 显示放大镜后 增加动画 class
     */
    addCssAnimation: function () {
        this.box.className = this.box.className + ' postbird-img-glass-box-move';
    },
    /**
     * 隐藏放大镜后 移除动画 class
     */
    removeCssAnimation: function () {
        this.box.className = this.box.className.replace(' postbird-img-glass-box-move', '');
    },
    /**
     * 向head中写入style
     * 包含基本的css和动画css
     */
    initCssClass: function () {
        var style = document.createElement('style');
        var styleContent = '.' + this.boxClassName + '{position:fixed;top:0;left:0;width:100%;height:100%;text-align:center;cursor:zoom-out;background-color:' + this.boxBgColor + ';display:none;text-align:center;overflow:hidden;z-index:999999;}';
        styleContent += '.' + this.boxClassName + ' img {position:relative;top:50%;transform:translateY(-50%);max-width:90%;}';
        styleContent += '.postbird-img-glass-box-move{animation:postbird-img-glass .5s;-webkit-animation:postbird-img-glass .5s;-moz-animation:postbird-img-glass .5s;-o-animation:postbird-img-glass .5s;animation-fill-mode:forwards;-o-animation-fill-mode:forwards;-moz-animation-fill-mode:forwards;-webkit-animation-fill-mode:forwards}@-moz-keyframes postbird-img-glass{from{opacity:0;width:100%;height:100%;transform:translateY(-200px)}to{opacity:1;width:100%;height:100%;transform:translateY(0)}}@-webkit-keyframes postbird-img-glass{from{opacity:0;width:100%;height:100%;transform:translateY(-200px)}to{opacity:1;width:100%;height:100%;transform:translateY(0)}}@-o-keyframes postbird-img-glass{from{opacity:0;width:100%;height:100%;transform:translateY(-200px)}to{opacity:1;width:100%;height:100%;transform:translateY(0)}}@keyframes postbird-img-glass{from{opacity:0;width:100%;height:100%;transform:translateY(-200px)}to{opacity:1;width:100%;height:100%;transform:translateY(0)}}';
        style.innerHTML = styleContent;
        document.head.appendChild(style);
    }
};

