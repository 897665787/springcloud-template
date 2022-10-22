<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="cmn-hans">
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
            aria-hidden="true">&times;</span>
    </button>
    <h4 class="modal-title">查看回复</h4>
</div>
<div class="modal-body" id="modalBody" style="min-height: 300px;">
    <table class="table text-center table-bordered table-striped m-b-none">
        <thead>
        <tr>
            <th>回复用户</th>
            <th>被回复用户</th>
            <th>回复内容</th>
            <th>状态</th>
            <th>回复时间</th>
            <th>点赞数</th>
            <th>回复数</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${list.size() eq 0}">
            <tr>
                <td colspan="8">无数据</td>
            </tr>
        </c:if>
        <c:forEach items="${list}" var="item">
            <tr>
                <td>${item.user.nickname}</td>
                <td>
                	<c:if test="${item.toUser.id != null}">
                		${item.toUser.nickname}
                	</c:if>
                </td>
                <td width="30%" style="text-align: left;">${xs:reverseEmoji(item.content)}</td>
                <td id="td_isHide_${item.id}">
                    <span class="${item.hided eq 0?'text-success':'text-danger'}">
                        <xs:descriptionDesc clazz="interact.Comment" property="hided" value="${item.hided}"/>
                    </span>
                </td>
                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${item.commentTime}"/></td>
                <td>${item.praiseNum}</td>
                <td>${item.commentNum}</td>
                <td id="td_isHide_btn_${item.id}">
                    <sec:authorize access="hasAnyRole(${xs:getPermissions('interact_comment_hided')})">
                        <c:if test="${item.hided eq 0}">
                            <button class="btn btn-danger btn-xs"
                                    onclick="commentReplyUpdateListItem('${item.id}',1)">
                                <xs:descriptionDesc clazz="interact.Comment" property="hided" value="1"/>
                            </button>
                        </c:if>
                        <c:if test="${item.hided eq 1}">
                            <button class="btn btn-success btn-xs"
                                    onclick="commentReplyUpdateListItem('${item.id}',0)">
                                <xs:descriptionDesc clazz="interact.Comment" property="hided" value="0"/>
                            </button>
                        </c:if>
                    </sec:authorize>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
</div>
<sec:authorize access="hasAnyRole(${xs:getPermissions('interact_comment_hided')})">
    <script id="showHide_0" type="text/html"><xs:descriptionDesc clazz="interact.Comment" property="hided" value="0"/></script>
    <script id="showHide_1" type="text/html"><xs:descriptionDesc clazz="interact.Comment" property="hided" value="1"/></script>

    <script>
        function commentReplyUpdateListItem(id, status) {
            doPost('<%=request.getContextPath()%>/admin/interact/comment/hided',
                {
                    id: id,
                    hided: status
                }, function (data) {
                    if (data.status) {
                        var td_isHide_btn_html = "                            <button class=\"btn "+(status==0?"btn-danger":"btn-success")+" btn-xs\""
                            +"                                onclick=\"commentReplyUpdateListItem('"+id+"',"+(status+1)%2+")\">"
                            + $('#showHide_'+(status+1)%2).html()
                            +"</button>";
                        $('#td_isHide_btn_'+id).html(td_isHide_btn_html);

                        var td_isHide_html = "                    <span class=\""+(status==0?"text-success":"text-danger")+"\">"
                            + $('#showHide_'+status).html()
                            +"</span>";
                        $('#td_isHide_'+id).html(td_isHide_html);
                    } else {
                        alert(data.message);
                    }
                });
        }
    </script>
</sec:authorize>
</body>
</html>