<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by kunye on 2018/07/05.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>用户分析</title>
    <%@include file="../common/head.jsp" %>
    <%@include file="../common/datepicker.jsp" %>
    <%@include file="../common/page.jsp" %>
    <script src="<%=request.getContextPath()%>/assets/plugins/echarts/echarts.common.min.js"></script>
<body>
<%@include file="../common/header.jsp" %>
<c:set var="index" value="stats_user"/>
<%@include file="../common/content_nav.jsp" %>

<div class="app-content ">
    <div class="app-content-body">
        <jsp:include page="user_stats_nav.jsp">
            <jsp:param name="nav" value="1"/>
        </jsp:include>
        <div class="wrapper-md">
            <div class="panel panel-default">
                <div class="panel-heading">
                    昨日关键指标
                </div>
                <div class="panel-body">
                    <div class="col-xs-6 b-r b-light">
                        <div class="text-center m-b-xs">新增人数</div>
                        <div class="text-center text-xxxl m-b-xs">
                            <fmt:formatNumber value="${summary.increCount}" maxFractionDigits="0"/>
                        </div>
                        <div class="col-xs-6 no-padder">
                            <div class="text-right m-r-sm">日</div>
                            <div class="text-right m-r-sm">周</div>
                            <div class="text-right m-r-sm">月</div>
                        </div>
                        <div class="col-xs-6 no-padder text-left">
                            <div>
                                <c:if test="${summary.increCount1 eq null}">--</c:if>
                                <c:if test="${summary.increCount1 ne null}">
                                    <c:if test="${summary.increCount1 >= 0}">
                                        <i class="glyphicon glyphicon-arrow-up text-success" style="font-size: 12px;"></i>&nbsp;${summary.increCount1}%
                                    </c:if>
                                    <c:if test="${summary.increCount1 < 0}">
                                        <i class="glyphicon glyphicon-arrow-down text-danger" style="font-size: 12px;"></i>&nbsp;${-summary.increCount1}%
                                    </c:if>
                                </c:if>
                            </div>
                            <div>
                                <c:if test="${summary.increCount7 eq null}">--</c:if>
                                <c:if test="${summary.increCount7 ne null}">
                                    <c:if test="${summary.increCount7 >= 0}">
                                        <i class="glyphicon glyphicon-arrow-up text-success" style="font-size: 12px;"></i>&nbsp;${summary.increCount7}%
                                    </c:if>
                                    <c:if test="${summary.increCount7 < 0}">
                                        <i class="glyphicon glyphicon-arrow-down text-danger" style="font-size: 12px;"></i>&nbsp;${-summary.increCount7}%
                                    </c:if>
                                </c:if>
                            </div>
                            <div>
                                <c:if test="${summary.increCount30 eq null}">--</c:if>
                                <c:if test="${summary.increCount30 ne null}">
                                    <c:if test="${summary.increCount30 >= 0}">
                                        <i class="glyphicon glyphicon-arrow-up text-success" style="font-size: 12px;"></i>&nbsp;${summary.increCount30}%
                                    </c:if>
                                    <c:if test="${summary.increCount30 < 0}">
                                        <i class="glyphicon glyphicon-arrow-down text-danger" style="font-size: 12px;"></i>&nbsp;${-summary.increCount30}%
                                    </c:if>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="text-center m-b-xs">累积人数</div>
                        <div class="text-center text-xxxl m-b-xs">
                            <fmt:formatNumber value="${summary.accuCount}" maxFractionDigits="0"/>
                        </div>
                        <div class="col-xs-6 no-padder">
                            <div class="text-right m-r-sm">日</div>
                            <div class="text-right m-r-sm">周</div>
                            <div class="text-right m-r-sm">月</div>
                        </div>
                        <div class="col-xs-6 no-padder text-left">
                            <div>
                                <c:if test="${summary.accuCount1 eq null}">--</c:if>
                                <c:if test="${summary.accuCount1 ne null}">
                                    <c:if test="${summary.accuCount1 >= 0}">
                                        <i class="glyphicon glyphicon-arrow-up text-success" style="font-size: 12px;"></i>&nbsp;${summary.accuCount1}%
                                    </c:if>
                                    <c:if test="${summary.accuCount1 < 0}">
                                        <i class="glyphicon glyphicon-arrow-down text-danger" style="font-size: 12px;"></i>&nbsp;${-summary.accuCount1}%
                                    </c:if>
                                </c:if>
                            </div>
                            <div>
                                <c:if test="${summary.accuCount7 eq null}">--</c:if>
                                <c:if test="${summary.accuCount7 ne null}">
                                    <c:if test="${summary.accuCount7 >= 0}">
                                        <i class="glyphicon glyphicon-arrow-up text-success" style="font-size: 12px;"></i>&nbsp;${summary.accuCount7}%
                                    </c:if>
                                    <c:if test="${summary.accuCount7 < 0}">
                                        <i class="glyphicon glyphicon-arrow-down text-danger" style="font-size: 12px;"></i>&nbsp;${-summary.accuCount7}%
                                    </c:if>
                                </c:if>
                            </div>
                            <div>
                                <c:if test="${summary.accuCount30 eq null}">--</c:if>
                                <c:if test="${summary.accuCount30 ne null}">
                                    <c:if test="${summary.accuCount30 >= 0}">
                                        <i class="glyphicon glyphicon-arrow-up text-success" style="font-size: 12px;"></i>&nbsp;${summary.accuCount30}%
                                    </c:if>
                                    <c:if test="${summary.accuCount30 < 0}">
                                        <i class="glyphicon glyphicon-arrow-down text-danger" style="font-size: 12px;"></i>&nbsp;${-summary.accuCount30}%
                                    </c:if>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="btn-group m-b-sm">
                <a class="js-tab btn btn-success btn-sm" data-value="1">新增人数</a>
                <a class="js-tab btn btn-default btn-sm" data-value="2">累积人数</a>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <form class="form-horizontal" id="diagramForm" name="diagramForm">
                        <div class="row">
                            <div class="col-xs-1 no-padder text-right">
                                <label class="control-label">统计频度：</label>
                            </div>
                            <div class="col-xs-1">
                                <select name="freq" class="form-control" data-value="1">
                                    <xs:dictOptions key="statFrequency"/>
                                </select>
                            </div>
                            <div class="col-xs-1 no-padder text-right">
                                <label class="control-label">查询时间：</label>
                            </div>
                            <div class="col-xs-5">
                                <div class="row">
                                    <div class="col-xs-4">
                                        <input type="text" name="startTime" class="form-control datepicker1" readonly>
                                    </div>
                                    <label class="pull-left control-label">至</label>
                                    <div class="col-xs-4">
                                        <input type="text" name="endTime" class="form-control datepicker1" readonly>
                                    </div>
                                    <div class="col-xs-2 no-padder">
                                        <button id="diagramSubmit" class="btn btn-success" type="submit">查询</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="m-t">
                    <div id="main" style="height:450px;"></div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span id="detail" class="m-l"></span>
                </div>
                <table id="detailTable" class="table text-center table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>时间</th>
                            <th>新增人数</th>
                            <th>累积人数</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
                <div class="panel-footer">
                    <div id="pagination" class="xs-pagination"></div>
                    <div class="clearfix"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script id="detailTemplate" type="text/html">
    <tr>
        <td>{{label}}</td>
        <td>{{incre}}</td>
        <td>{{accu}}</td>
    </tr>
</script>
<script>
    var myChart = echarts.init(document.getElementById('main'));
    var labels = [];//坐标数据
    var detailData = [];//统计详情列表数据
    var increDatasets = [];//新增人数数据
    var accuDatasets = [];//累积人数数据

    function resetEcharts(){
        var tab = $(".js-tab.btn-success").data("value");
        var datasets;
        var serieName;
        if(tab==1){
            datasets = increDatasets;
            serieName = "新增人数";
        }else if(tab==2){
            datasets = accuDatasets;
            serieName = "累积人数";
        }
        var option = {
            color: ['#3398DB'],
            tooltip : {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            grid: {
                left: '4%',
                right: '4%',
                bottom: '10%',
                top: '10%',
                containLabel: true
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            xAxis: {
                type: 'category',
                data: labels
            },
            yAxis: {
                type: 'value',
                name: '人数',
                boundaryGap: [0,0.01],
                axisLabel: {
                    formatter: '{value}'
                }
            },
            series: [
                {
                    type: 'line',
                    name: serieName,
                    data: datasets,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        //缩放还原
        myChart.dispatchAction({
            type: 'dataZoom',
            // 缩放的开始位置的百分比，0 - 100
            start: 0,
            // 缩放的结束位置的百分比，0 - 100
            end: 100
        });
    }

    $(".js-tab").on("click", function () {
        var tab = $(this).data("value");
        $(".js-tab").removeClass("btn-success");
        $(".js-tab").addClass("btn-default");
        $(this).removeClass("btn-default");
        $(this).addClass("btn-success");
        resetEcharts();
    })

    var $diagramForm = $("form[name=diagramForm]");
    var $diagramSubmit = $("#diagramSubmit");
    var diagramValidator = $diagramForm.validate({
        rules: {
            freq: {
                required: true
            },
            startTime: {
                required: true
            },
            endTime: {
                required: true
            }
        },
        messages: {
            freq: {
                required: ""
            },
            startTime: {
                required: ""
            },
            endTime: {
                required: ""
            }
        },
        submitHandler: function () {
            $diagramSubmit.attr("disabled", true);
            doGet("<%=request.getContextPath()%>/admin/stats/user/diagram", $diagramForm.serialize(),
                function (data) {
                    $diagramSubmit.attr("disabled", false);
                    if (data.status) {
                        labels = data.data.labels;
                        detailData = data.data.detail;
                        accuDatasets = data.data.accuDatasets;
                        increDatasets = data.data.increDatasets;
                        //统计详情列表
                        $("#pagination").pagination({
                            isTogo: true,
                            callbackCustom: function (current) {
                                $("#pagination").pagination("setPage", {
                                    current: current,
                                    total: Math.ceil(detailData.length/10),
                                    totalRecord: detailData.length
                                });
                                var offset = (current-1)*10;
                                renderData("#detailTable", detailData.slice(offset, offset+10), "detailTemplate");
                            }
                        });
                        resetEcharts();
                        $("#detail").html($("[name=startTime]").val()+" 至 "+$("[name=endTime]").val());
                    } else {
                        alert(data.message);
                    }
                }
            );
        }
    });
</script>
</body>
</html>
