<%--
  Created by JQ棣 on 2018/10/12
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="bg-light lter b-b padder-md">
    <a href="<%=request.getContextPath()%>/admin/stats/user" class="xs-nav text-base ${param.nav eq 1?'xs-active':''}">用户分析</a>
    <a href="<%=request.getContextPath()%>/admin/stats/user/sex" class="xs-nav text-base ${param.nav eq 2?'xs-active':''}">性别分析</a>
    <a href="<%=request.getContextPath()%>/admin/stats/user/age" class="xs-nav text-base ${param.nav eq 3?'xs-active':''}">年龄分析</a>
</div>

<script>
    $(function () {
        $("[name=freq]").on("change", function () {
            $(".datepicker1").val("");
            if($(this).val()==2){
                $(".datepicker1").datepicker('remove');
                var t = new Date();
                $("[name=endTime]").val(t.format("yyyy-MM"));
                var e = new Date();
                e.setTime(t.getTime() - 24 * 60 * 60 * 1000 * 179);
                $("[name=startTime]").val(e.format("yyyy-MM"));
                $(".datepicker1").datepicker({
                    format:'yyyy-mm',
                    startView: 1,
                    minViewMode: 'months',
                    startDate: '2018-1',
                    endDate: t,
                    clearBtn: false
                });
            }else{
                $(".datepicker1").datepicker('remove');
                var t = new Date();
                $("[name=endTime]").val(t.format("yyyy-MM-dd"));
                var e = new Date();
                e.setTime(t.getTime() - 24 * 60 * 60 * 1000 * 29);
                $("[name=startTime]").val(e.format("yyyy-MM-dd"));
                $(".datepicker1").datepicker({
                    format:'yyyy-mm-dd',
                    startView: 0,
                    minViewMode: 'days',
                    startDate: '2018-1-1',
                    endDate: t,
                    clearBtn: false
                });
            }
        });

        $("[name=freq]").trigger("change");
        //图表
        $diagramForm.submit();
    })
</script>
