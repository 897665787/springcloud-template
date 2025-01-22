<%--
  User: JQ棣
  Date: 19/6/2018
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/plugins/dropdown/css/jquery.dropdown.css">
<script src="<%=request.getContextPath()%>/assets/plugins/dropdown/js/jquery.dropdown.js"></script>

<%-- 用法--%>
<%--
    <div class="dropdown-mul">
        <select class="none" name="tags" multiple data-init="${product.tags}">
            <c:forEach items="${tagOption}" var="item">
                <option value="${item.id}">${item.name}</option>
            </c:forEach>
        </select>
    </div>

    $(function () {
        //初始化多选框
        $('.dropdown-mul').dropdown({
            limitCount: 5 //限制个数
        });
    })

    回显：
    data-init="${product.tags}"
    被选中的值，以逗号分隔
--%>