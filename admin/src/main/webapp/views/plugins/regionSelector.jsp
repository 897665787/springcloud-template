<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%--
  Created by wjc on 2018/05/30.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>Demo</title>
    <%@include file="../common/head.jsp" %>
</head>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="regionSelector"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content">
    <div class="app-content-body">
        <div class="bg-light lter b-b wrapper-md ">
            <h1 class="m-n font-thin h3 inline">地区选择器</h1>
        </div>
        <div class="wrapper-md">
            <div class="col-xs-12">
                <form class="form-horizontal" id="searchForm" name="searchForm">
                    <div class="form-group">
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">地区(省级示例)：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="dynamic[province]" type="text" class="form-control regionSelector1" placeholder="请选择"
                                   data-dealLevel="1" data-selected="35">
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">地区(市级示例)：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="dynamic[city]" type="text" class="form-control regionSelector1" placeholder="请选择"
                                   data-dealLevel="2" data-selected="">
                        </div>
                        <div class="col-xs-4 col-md-2 col-lg-1 no-padder m-b-md text-right">
                            <label class="control-label">地区(区级示例)：</label>
                        </div>
                        <div class="col-xs-8 col-md-4 col-lg-3  m-b-md">
                            <input name="dynamic[district]" type="text" class="form-control regionSelector1" readonly placeholder="请选择"
                                   data-dealLevel="3" data-selected="44,4401,440106" value="">
                        </div>
                    </div>
                </form>
            </div>

            <div class="col-xs-6">
                <h4>使用说明</h4>
                <br><br>
                <p>
                    <code>&lt;%@include file="../common/regionSelector.jsp" %&gt;</code>
                    <br><br>
                    <code>&lt;script&gt;</code><br>
                    <code>&nbsp;&nbsp;&nbsp;&nbsp;let options = {</code><br>
                    <code>&nbsp;&nbsp;&nbsp;&nbsp;className: "regionSelector", //使用选择器的input元素的class</code><br>
                    <code>&nbsp;&nbsp;&nbsp;&nbsp;splitSign: " ", //非必填, 展示时候的分隔符, 默认是一个空格</code><br>
                    <code>&nbsp;&nbsp;&nbsp;&nbsp;cleanBtn: true //非必填, 是否显示清除按钮, 默认是false</code><br>
                    <code>&nbsp;&nbsp;&nbsp;&nbsp;};</code><br>
                    <code>&nbsp;&nbsp;&nbsp;&nbsp;$().loadRegionSelector(options);</code><br>
                    <code>&lt;/script&gt;</code><br>
                </p>
                <br><br>
                <hr>
                <h4>数据回显（js设定值的时候用）</h4>
                <br>
                <p>
                    <code>setMapDataValue(name, value);</code>
                    <br>
                    例如：<b>setMapDataValue("dynamic[city]", "44,4401,440106")</b><br>
                    name为"dynamic[city]"的选择器，设定值为"44,4401,440106"，然后再自动回显数据, 设定的值可以是1到3级，
                    但回显的数据受该选择器的<b>data-dealLevel</b>配置限制, 就如这个例子, 设定的值是3级的, 到选择器的<b>data-dealLevel</b>配置是2级，
                    所以数据回显只有2级
                    <br><br>
                    <code>mapDataShowOut(name);</code>
                    <br>
                    例如：<b>mapDataShowOut("dynamic[city]")</b><br>
                    这个跟上一个方法差不多，不过这个只是用来回显数据，不设定值，回显也是受<b>data-dealLevel</b>配置的限制<br>
                    <b>name</b>非必填, 不传name则页面上所有的选择器都会重新回显一次
                    <br><br>
                    <code>cleanMapData(name);</code>
                    <br>
                    例如：<b>cleanMapData("dynamic[province]")</b><br>
                    清楚选择器的选择内容，跟选择器的清除按钮一样的功能<br>
                    <b>name</b>非必填, 不传name则页面上所有的选择器都会清除
                    <br><br>
                    <code>getMapDataValue(name);</code>
                    <br>
                    例如：<b>cleanMapData("dynamic[province]")</b><br>
                    获取选择的结果，市级和区级的结果有可能为空，是否为空由<b>data-dealLevel</b>配置决定<br>
                    返回结果: <br>
                    <code>{city: "广州市", cityId: "4401", district: "天河区", districtId: "440106", province: "广东省", provinceId: "44"}</code>
                </p>
            </div>
            <div class="col-xs-6">
                <h4>input配置</h4>
                <br><br>
                <p>
                    <code>&lt;input name="dynamic[regionText]" type="text" class="form-control regionSelector" data-dealLevel="1" placeholder="请选择" readonly&gt;</code>
                    <br><br>
                    <b>class="regionSelector"</b>&nbsp;&nbsp;&nbsp;&nbsp;带有这个class的input会变成选择器<br><br>
                    <b>data-dealLevel="1"</b>&nbsp;&nbsp;&nbsp;&nbsp;这个参数控制选择器的选择级别，1-选到省级，2-选到市级，3-选到区级<br><br>
                    <b>data-selected="44,4406,440606"</b>&nbsp;&nbsp;&nbsp;&nbsp;选择结果会在这里，页面加载是要回显数据也只需要设定这个属性<br><br>
                </p>

            </div>
        </div>
    </div>
</div>

<%@include file="../common/regionSelector.jsp" %>

<script>
    let options = {
        className: "regionSelector1", //使用选择器的input元素的class
        splitSign: "-", //非必填, 展示时候的分隔符, 默认是一个空格
        cleanBtn: true //非必填, 是否显示清除按钮, 默认是false
    };
    $().loadRegionSelector(options);

    setMapDataValue("dynamic[city]", "44,4401");

    mapDataShowOut("dynamic[province]");

    cleanMapData("dynamic[province]");

    console.log(getMapDataValue("dynamic[city]"));
</script>
</body>
</html>

