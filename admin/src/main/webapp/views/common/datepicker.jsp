<%--
  Created by IntelliJ IDEA.
  User: JQ棣
  Date: 26/10/2017
  Time: 10:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="<%=request.getContextPath()%>/assets/js/bootstrap-datepicker.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/app.datepicker.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/datepicker.min.css">
<script type="text/javascript">
    $(function () {
        $('.datepicker').datepicker().on('changeDate', function(ev){
            this.focus();
            this.blur();
        });
    });
</script>