<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by JQ棣 on 2018/07/05.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<head>
    <title>用户年龄分析</title>
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
            <jsp:param name="nav" value="3"/>
        </jsp:include>
        <div class="wrapper-md">
            <div class="panel panel-default">
                <div class="panel-heading">
                    用户年龄概况
                </div>
                <div class="panel-body">
                    <div id="ageMain" style="height: 270px"></div>
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
                    <span class="m-l js-search-date"></span>
                </div>
                <table id="detailTable" class="table text-center table-bordered table-striped">
                    <thead>
                        <tr>
                            <th rowspan="2">时间</th>
                            <th colspan="7">新增人数</th>
                            <th colspan="7">累积人数</th>
                        </tr>
                        <tr>
                            <th>未知</th>
                            <th>40岁及以下</th>
                            <th>41~50岁</th>
                            <th>51~60岁</th>
                            <th>61~70岁</th>
                            <th>71~80岁</th>
                            <th>80岁以上</th>
                            <th>未知</th>
                            <th>40岁及以下</th>
                            <th>41~50岁</th>
                            <th>51~60岁</th>
                            <th>61~70岁</th>
                            <th>71~80岁</th>
                            <th>80岁以上</th>
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
        <td>{{incre1}}</td>
        <td>{{incre2}}</td>
        <td>{{incre3}}</td>
        <td>{{incre4}}</td>
        <td>{{incre5}}</td>
        <td>{{incre6}}</td>
        <td>{{incre7}}</td>
        <td>{{accu1}}</td>
        <td>{{accu2}}</td>
        <td>{{accu3}}</td>
        <td>{{accu4}}</td>
        <td>{{accu5}}</td>
        <td>{{accu6}}</td>
        <td>{{accu7}}</td>
    </tr>
</script>
<script>
    var myChart2 = echarts.init(document.getElementById('ageMain'));
    var option2 = {
        tooltip: {
            trigger: 'item'
        },
        grid: {
            left: '4%',
            right: '4%',
            top: '5%',
            bottom: '5%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            name: '人数',
            boundaryGap: [0,0.01],
            axisLabel: {
                formatter: '{value}'
            }
        },
        yAxis: {
            type: 'category',
            data: ['未知','40岁及以下','41~50岁','51~60岁','61~70岁','71~80岁','80岁以上']
        },
        series: [
            {
                type: 'bar',
                name: '年龄',
                barWidth: '45%',
                data: ${summary},
                label: {
                    show: true,
                    position: 'inside'
                },
                itemStyle: {
                    //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                    color: function (params){
                        var colorList = ['#8dddf6','#42a5f5','#4caf50','#ffc107','#ff9800','#f44336','#F62494'];
                        return colorList[params.dataIndex];
                    }
                }
            }
        ]
    };
    myChart2.setOption(option2);

    var myChart = echarts.init(document.getElementById('main'));
    var labels = [];//坐标数据
    var detailData = [];//统计详情列表数据
    var increDatasets1 = [];
    var increDatasets2 = [];
    var increDatasets3 = [];
    var increDatasets4 = [];
    var increDatasets5 = [];
    var increDatasets6 = [];
    var increDatasets7 = [];
    var accuDatasets1 = [];
    var accuDatasets2 = [];
    var accuDatasets3 = [];
    var accuDatasets4 = [];
    var accuDatasets5 = [];
    var accuDatasets6 = [];
    var accuDatasets7 = [];

    function resetEcharts(){
        var tab = $(".js-tab.btn-success").data("value");
        var datasets1;
        var datasets2;
        var datasets3;
        var datasets4;
        var datasets5;
        var datasets6;
        var datasets7;
        if(tab==1){
            datasets1 = increDatasets1;
            datasets2 = increDatasets2;
            datasets3 = increDatasets3;
            datasets4 = increDatasets4;
            datasets5 = increDatasets5;
            datasets6 = increDatasets6;
            datasets7 = increDatasets7;
        }else if(tab==2){
            datasets1 = accuDatasets1;
            datasets2 = accuDatasets2;
            datasets3 = accuDatasets3;
            datasets4 = accuDatasets4;
            datasets5 = accuDatasets5;
            datasets6 = accuDatasets6;
            datasets7 = accuDatasets7;
        }
        var option = {
            color: ['#8dddf6','#42a5f5','#4caf50','#ffc107','#ff9800','#f44336','#F62494'],
            tooltip : {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                selectedMode: 'multiple',
                data: ['未知','40岁及以下','41~50岁','51~60岁','61~70岁','71~80岁','80岁以上']
            },
            grid: {
                left: '4%',
                right: '4%',
                bottom: '10%',
                top: '15%',
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
                    name: '未知',
                    data: datasets1,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
                {
                    type: 'line',
                    name: '40岁及以下',
                    data: datasets2,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
                {
                    type: 'line',
                    name: '41~50岁',
                    data: datasets3,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
                {
                    type: 'line',
                    name: '51~60岁',
                    data: datasets4,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
                {
                    type: 'line',
                    name: '61~70岁',
                    data: datasets5,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
                {
                    type: 'line',
                    name: '71~80岁',
                    data: datasets6,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
                {
                    type: 'line',
                    name: '80岁以上',
                    data: datasets7,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    }
                },
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
            doGet("<%=request.getContextPath()%>/admin/stats/user/age/diagram", $diagramForm.serialize(),
                function (data) {
                    $diagramSubmit.attr("disabled", false);
                    if (data.status) {
                        labels = data.data.labels;
                        detailData = data.data.detail;
                        accuDatasets1 = data.data.accuDatasets1;
                        accuDatasets2 = data.data.accuDatasets2;
                        accuDatasets3 = data.data.accuDatasets3;
                        accuDatasets4 = data.data.accuDatasets4;
                        accuDatasets5 = data.data.accuDatasets5;
                        accuDatasets6 = data.data.accuDatasets6;
                        accuDatasets7 = data.data.accuDatasets7;
                        increDatasets1 = data.data.increDatasets1;
                        increDatasets2 = data.data.increDatasets2;
                        increDatasets3 = data.data.increDatasets3;
                        increDatasets4 = data.data.increDatasets4;
                        increDatasets5 = data.data.increDatasets5;
                        increDatasets6 = data.data.increDatasets6;
                        increDatasets7 = data.data.increDatasets7;
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
                        $(".js-search-date").html($("[name=startTime]").val()+" 至 "+$("[name=endTime]").val());
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
