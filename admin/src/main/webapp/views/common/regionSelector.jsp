<%--
  Description: 地区选择组件
  User: WDBB
  Datetime: 2019/3/7 19:06
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    .choose_page {
        max-width: 530px;
        max-height: 380px;
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
        z-index: 2000;
        display: none;
        border: 1px solid #DCDCDC;
        box-shadow: 0 2px 6px 0 rgba(0,0,0,0.10);
    }

    .choose_page .page_body{
        width: 100%;
        max-height: 380px;
        position: absolute;
        right: 0; bottom: 0; left: 0;
        margin: auto;
        background: white;
        background-size: 100% 100%;
        z-index: 2000;
        padding: 0 10px;
    }

    .choose_page .page_body .level_nav {
        height: 44px;
        font-size: 14px;
        color: #333333;
        letter-spacing: 0.15px;
    }

    .level_nav .nav {
        height: 100%;
        display: inline-flex;
        margin-right: 30px;
        align-items: flex-end;
        padding-bottom: 5px;
        cursor: pointer;
    }

    .level_nav .nav.active {
        color: #2F72DD !important;
        display: inline-flex;
        position: relative;
    }

    .level_nav .nav.active:after {
        content: "";
        background: #2F72DD;
        width: 100%;
        height: 2px;
        position: absolute;
        bottom: -1px;
    }


    .choose_page .page_body .list_box {
        height: 350px;
        padding-top: 10px;
        border-top: 1px solid #DCDCDC;
        overflow-y: auto;
    }

    .choose_page .page_body .list_box .list_row {
        display: inline-block;
        height: 40px;
        line-height: 40px;
        text-align: center;
        font-size: 14px;
        color: #333333;
        letter-spacing: 0.15px;
        background: #f5f5f5;
        margin: 5px;
        padding: 0 10px;
        float: left;
        cursor: pointer;
    }

    .choose_page .page_body .list_box .list_row:hover {
        background: #2F72DD;
        color: white;
    }

    .choose_page .page_body .list_box .list_row.active {
        background: #2F72DD;
        color: white;
    }

    .region_clean {
        position: relative;
    }

    .region_clean:hover+.region_clean_btn {
        visibility: visible !important;
    }

    .region_clean_btn {
        content: '×';
        width: 20px;
        height: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: #666;
        position: absolute;
        right: 20px;
        top: 0;
        bottom: 0;
        margin: auto;
        z-index: 999;
        cursor: pointer;
    }

    .region_clean_btn:hover {
        visibility: visible !important;
    }
</style>

<div class="choose_page" id="choose_page">
    <div class="page_body">
        <div class="level_nav">
            <div class="nav level_province active">请选择</div>
            <div class="nav level_city" style="display: none;">请选择</div>
            <div class="nav level_district" style="display: none;">请选择</div>
        </div>
        <div class="list_box province"></div>
        <div class="list_box city" style="display: none;"></div>
        <div class="list_box district" style="display: none"></div>
    </div>
</div>

<script id="provinceTemplate" type="text/template">
    <div class="list_row" data-id="{{id}}">{{name}}</div>
</script>

<script id="cityTemplate" type="text/template">
    <div class="list_row" data-id="{{id}}" data-upId="{{pId}}">{{name}}</div>
</script>

<script id="districtTemplate" type="text/template">
    <div class="list_row" data-id="{{id}}" data-upId="{{pId}}">{{name}}</div>
</script>


<script src="<%=request.getContextPath()%>/assets/js/mustache.js"></script>
<script>
    let mapJson = "";
    let finalOpts = "";

    $.fn.loadRegionSelector = function (options){

        let defaultOptions = {
            className: "regionSelector",
            splitSign: " ",
            cleanBtn: false
        };
        let opts = $.extend({}, defaultOptions, options);
        finalOpts = opts;

        let left, top;
        let dealWithLevel = 3;
        let defaultText = "请选择";
        let focusInput;
        let chosenRegion =  {
            provinceId:"0", cityId:"0", districtId:"0", province:"", city:"", district:""
        };

        $("." + opts.className).each(function () {
            $(this).css("cursor", "pointer");
            $(this).attr("readonly", true);
            if (opts.cleanBtn) {
                $(this).addClass("region_clean");
                $(this).after('<span class="region_clean_btn" style="visibility: hidden;">×</span>');
            }
        });

        $("." + opts.className).click(function () {
            focusInput = this;

            let height = parseInt($(this).height());
            let paddingY = parseInt($(this).css("padding-top")) + parseInt($(this).css("padding-bottom"));
            top = $(this).offset().top + height + paddingY + 4;
            left = $(this).offset().left;

            let bodyWidth = document.body.offsetWidth;
            let selectorWidth = $("#choose_page").width();
            if (left + selectorWidth > bodyWidth) {
                $("#choose_page").css("left", bodyWidth - selectorWidth - 10);
            } else {
                $("#choose_page").css("left", left);
            }
            $("#choose_page").css("top", top);

            let level = $(this).attr("data-dealLevel");
            if (isNotBlank(level)) {
                dealWithLevel = parseInt(level);
            } else {
                $(this).attr("data-dealLevel", dealWithLevel);
            }
            showRegionChoosePage();
        });

        $(document).bind("click",function(e){
            let target = $(e.target);
            if (!target.hasClass(opts.className) && target.closest("#choose_page").length === 0) {
                hideRegionChoosePage();
            }
        });

        $(".region_clean_btn").click(function () {
            focusInput = $(this).prev();
            cleanChosen();
            hideRegionChoosePage();
            $(focusInput).attr("data-selected", "");
            $(focusInput).val("");
        });

        function showRegionChoosePage() {
            $(".level_nav>.nav").removeClass("active");
            let ids = $(focusInput).attr("data-selected");
            let texts = $(focusInput).val();
            if (isBlank(ids) || isBlank(texts)) {
                $(focusInput).attr("data-selected", "");
                cleanChosen(1);
                showByLevel(1, true);
            } else {
                ids = ids.split(",");
                let regionTexts = $(focusInput).val().split(opts.splitSign);

                chosenRegion.provinceId = ids[0].trim();
                chosenRegion.province = regionTexts[0].trim();
                $(".level_nav>.nav.level_province").text(chosenRegion.province);
                $(".list_box.province>.list_row").each(function () {
                    let $this = $(this);
                    if ($this.attr("data-id") == chosenRegion.provinceId) {
                        $this.addClass("active");
                    } else {
                        $this.removeClass("active")
                    }
                });

                if (dealWithLevel > 1) {
                    chosenRegion.cityId = ids[1].trim();
                    chosenRegion.city = regionTexts[1].trim();
                    $(".level_nav>.nav.level_city").text(chosenRegion.city);
                    $(".list_box.city>.list_row").each(function (i, e) {
                        if($(e).attr("data-upId").trim() === ids[0]){
                            $(e).show();
                            if ($(e).attr("data-id") == chosenRegion.cityId) {
                                $(e).addClass("active");
                            } else {
                                $(e).removeClass("active")
                            }
                        }else{
                            $(e).hide();
                        }
                    });

                    if (dealWithLevel > 2) {
                        chosenRegion.districtId = ids[2].trim();
                        chosenRegion.district = regionTexts[2].trim();
                        $(".level_nav>.nav.level_district").text(chosenRegion.district);

                        getDistrictList(ids[1], ids[2]);
                    }
                }

                showByLevel(dealWithLevel, true);
            }

            $("#choose_page").show();
        }

        function hideRegionChoosePage() {
            $(".level_nav>.nav").removeClass("active");
            $(".level_nav>.nav").hide();
            $("#choose_page").hide();
            $(".list_row.active").each(function () {
                let $this = $(this);
                $this.removeClass("active");
            });
        }

        function choseProvince(ele) {
            let eleId = $(ele).attr("data-id").trim();
            if (chosenRegion.provinceId !== eleId) {
                cleanChosen(2);
            }
            chosenRegion.provinceId = eleId;
            chosenRegion.province = $(ele).text().trim();
            $(".level_nav>.nav").removeClass("active");
            $(".level_nav>.nav.level_province").text(chosenRegion.province);

            if (dealWithLevel > 1) {
                $(".level_nav>.nav.level_city").addClass("active");
                $(".level_nav>.nav.level_city").show();
                $(".list_box.city>.list_row").each(function (i, e) {
                    if($(e).attr("data-upId").trim() === eleId){
                        $(e).show();
                    }else{
                        $(e).hide();
                    }
                });
                showByLevel(2);
            } else {
                $(focusInput).attr("data-selected", chosenRegion.provinceId);
                $(focusInput).val(chosenRegion.province);
                hideRegionChoosePage();
            }
        }

        function choseCity(ele) {
            let eleId = $(ele).attr("data-id").trim();
            if (chosenRegion.cityId !== eleId) {
                cleanChosen(3);
            }
            chosenRegion.cityId = $(ele).attr("data-id").trim();
            chosenRegion.city = $(ele).text().trim();
            $(".level_nav>.nav").removeClass("active");
            $(".level_nav>.nav.level_city").text(chosenRegion.city);

            if (dealWithLevel > 2) {
                $(".level_nav>.nav.level_district").addClass("active");
                $(".level_nav>.nav.level_district").show();
                getDistrictList(chosenRegion.cityId);
                showByLevel(3);
            } else {
                let text = [chosenRegion.province, chosenRegion.city];
                let ids = [chosenRegion.provinceId, chosenRegion.cityId];
                $(focusInput).attr("data-selected", ids.join(","));
                $(focusInput).val(text.join(opts.splitSign));
                hideRegionChoosePage();
            }
        }

        function choseDistrict(ele) {
            chosenRegion.districtId = $(ele).attr("data-id").trim();
            chosenRegion.district = $(ele).text().trim();
            $(".level_nav>.nav.level_district").text(chosenRegion.district);
            let text = [chosenRegion.province, chosenRegion.city, chosenRegion.district];
            let ids = [chosenRegion.provinceId, chosenRegion.cityId, chosenRegion.districtId];
            $(focusInput).attr("data-selected", ids.join(","));
            $(focusInput).val(text.join(opts.splitSign));
            hideRegionChoosePage();
        }

        function cleanChosen(level) {
            if (isBlank(level)) {
                level = 1;
            }
            $(".level_nav>.nav").removeClass("active");
            if (level === 1) {
                $(".level_nav>.nav").text(defaultText);
                chosenRegion =  {
                    provinceId:0, cityId:0, districtId:0, province:"", city:"", district:""
                };
            } else if (level === 2) {
                chosenRegion.cityId=0;
                chosenRegion.city = "";
                chosenRegion.districtId=0;
                chosenRegion.district = "";
                $(".level_nav>.nav.level_city").text(defaultText);
                $(".level_nav>.nav.level_district").text(defaultText);
            } else if (level === 3) {
                chosenRegion.districtId = 0;
                chosenRegion.district = "";
                $(".level_nav>.nav.level_district").text(defaultText);
            }
            showByLevel(level, true);
        }

        $(function () {
            $.ajax({
                url: "<%=request.getContextPath()%>/admin/system/region/tree",
                timeout: 5000,
                type: 'post',
                data: "",
                dataType: 'json',
                success: function (data) {
                    if (data.status) {
                        mapJson = data.data;
                        let provinceList = mapJson;
                        let provinceTemplateHtml = $("#provinceTemplate").html().trim();
                        let cityTemplateHtml = $("#cityTemplate").html().trim();
                        let $provinceBox = $(".list_box.province");
                        let $cityBox = $(".list_box.city");

                        $.each(provinceList, function (i, province) {
                            $($provinceBox).append(Mustache.render(provinceTemplateHtml, province));
                            $.each(province.children, function (i, city) {
                                $($cityBox).append(Mustache.render(cityTemplateHtml, city));
                            });
                        });

                        $("#choose_page .list_box.province>.list_row").click(function () {
                            choseProvince(this);
                        });

                        $("#choose_page .list_box.city>.list_row").click(function () {
                            choseCity(this);
                        });

                        //地图数据请求出来了,就要遍历做页面加载时的数据回显
                        mapDataShowOut();
                    } else {
                        alert(data.message);
                    }
                },
                error: function (data) {
                    alert('服务异常');
                }
            });

        });



        function getDistrictList(cityId, selectedId) {
            $.each(mapJson, function (i, province) {
                if (cityId.toString().startsWith(province.id.toString())) {
                    $.each(province.children, function (i, city) {
                        if (city.id == cityId) {
                            let districtList = city.children;
                            let districtTemplateHtml = $("#districtTemplate").html().trim();
                            let $districtBox = $(".list_box.district");
                            $($districtBox).empty();
                            $.each(districtList, function (i,item) {
                                $($districtBox).append(Mustache.render(districtTemplateHtml, item));
                            });
                            $("#choose_page .list_box.district>.list_row").click(function () {
                                choseDistrict(this);
                            });
                            if (isNotBlank(selectedId)) {
                                $(".list_box.district>.list_row").each(function () {
                                    let $this = $(this);
                                    if ($this.attr("data-id") == selectedId) {
                                        $this.addClass("active");
                                    } else {
                                        $this.removeClass("active")
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    };

    function cleanMapData(name) {
        let $input = $("." + finalOpts.className);
        if (isNotBlank(name)) {
            $input = $("." + finalOpts.className + "[name='" + name + "']");
        }
        $($input).each(function () {
            $(this).attr("data-selected", "");
            $(this).val("");
        });
    }

    //获取选取的数据
    function getMapDataValue(name) {
        let $input = $("." + finalOpts.className + "[name='" + name + "']");
        let selected = $($input).attr("data-selected");
        let text = $($input).val();
        let selectedRegion =  {
            provinceId:null, cityId:null, districtId:null, province:"", city:"", district:""
        };

        let level = $(this).attr("data-dealLevel");
        if (isBlank(level)) {
            level = 3;
        } else {
            level = parseInt(level);
        }
        if (isNotBlank(selected)) {
            let ids = selected.split(",");
            selectedRegion.provinceId = ids[0];
            if (level > 1 && ids.length > 1) {
                selectedRegion.cityId = ids[1];
                if (level > 2 && ids.length > 2) {
                    selectedRegion.districtId = ids[2];
                }
            }
        }

        if (isNotBlank(text)) {
            let texts = text.split(finalOpts.splitSign);
            selectedRegion.province = texts[0];
            if (level > 1 && texts.length > 1) {
                selectedRegion.city = texts[1];
                if (level > 2 && texts.length > 2) {
                    selectedRegion.district = texts[2];
                }
            }
        }

        return selectedRegion;
    }

    //设定选择的数据并回显
    function setMapDataValue(name, value) {
        $("." + finalOpts.className + "[name='" + name + "']").attr("data-selected", value);
        mapDataShowOut();
    }

    //数据回显
    function mapDataShowOut(name){
        let $showOutInput = $("." + finalOpts.className);
        if (isNotBlank(name)) {
            $showOutInput = $("." + finalOpts.className + "[name='" + name + "']");
        }
        console.log($showOutInput);
        //数据回显
        $($showOutInput).each(function () {
            let defaultValue = $(this).attr("data-selected");
            let level = $(this).attr("data-dealLevel");
            if (isBlank(level)) {
                level = 3;
            } else {
                level = parseInt(level);
            }
            if (isNotBlank(defaultValue)) {
                let selecteds = defaultValue.split(",");
                let length = selecteds.length;
                let text = "";
                $.each(mapJson, function (i, province) {
                    if (province.id.toString() === selecteds[0].trim()) {
                        text += province.name;

                        if (length > 1 && level > 1) {
                            $.each(province.children, function (i, city) {
                                if (city.id.toString() === selecteds[1].trim()) {
                                    text += finalOpts.splitSign + city.name;

                                    if (length > 2 && level > 2) {
                                        $.each(city.children, function (i, district) {
                                            if (district.id.toString() === selecteds[2].trim()) {
                                                text += finalOpts.splitSign + district.name;
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
                $(this).val(text);
            }
        });
    }

    $("#choose_page .level_nav>.level_province").click(function () {
        selectLevel(this,1);
    });

    $("#choose_page .level_nav>.level_city").click(function () {
        selectLevel(this,2);
    });

    $("#choose_page .level_nav>.level_district").click(function () {
        selectLevel(this,3);
    });



    function selectLevel(ele,level) {
        $(".level_nav>.nav").removeClass("active");
        $(ele).addClass("active");
        showByLevel(level);
    }

    function showByLevel(level, operateNav) {
        if (level === 1) {
            $(".list_box").hide();
            $(".list_box.province").show();
            $(".level_nav>.nav.level_province").addClass("active");
        } else if (level === 2) {
            $(".list_box").hide();
            $(".list_box.city").show();
            $(".level_nav>.nav.level_city").addClass("active");
        } else if (level === 3) {
            $(".list_box").hide();
            $(".list_box.district").show();
            $(".level_nav>.nav.level_district").addClass("active");
        }

        if (isNotBlank(operateNav) && operateNav) {
            if (level === 1) {
                $(".level_nav>.nav.level_province").show();
                $(".level_nav>.nav.level_city").hide();
                $(".level_nav>.nav.level_district").hide();
            } else if (level === 2) {
                $(".level_nav>.nav.level_province").show();
                $(".level_nav>.nav.level_city").show();
                $(".level_nav>.nav.level_district").hide();
            } else if (level === 3) {
                $(".level_nav>.nav.level_province").show();
                $(".level_nav>.nav.level_city").show();
                $(".level_nav>.nav.level_district").show();
            }
        }
    }
</script>
