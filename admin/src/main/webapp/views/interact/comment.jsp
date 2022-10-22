<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="xs" uri="http://code.template.com/tags/jsp/xs" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/page.jsp" %>
<%@include file="../common/datepicker.jsp" %>
<form class="form-horizontal" id="tempCommentForm">
    <div class="form-group">
        <div class="col-xs-1 no-padder m-b-md text-right">
            <label class="control-label">评论人：</label>
        </div>
        <div class="col-xs-3  m-b-md">
            <input name="dynamic[nickname]" type="text" class="form-control" placeholder="模糊查询"/>
        </div>
        <div class="col-xs-1 no-padder m-b-md text-right">
            <label class="control-label">评论人手机：</label>
        </div>
        <div class="col-xs-3  m-b-md">
            <input name="dynamic[mobile]" type="text" class="form-control" placeholder="模糊查询">
        </div>
        <div class="col-xs-1 no-padder m-b-md text-right">
            <label class="control-label">是否隐藏：</label>
        </div>
        <div class="col-xs-3  m-b-md">
            <select name="hided" class="form-control">
                <option value="">全部</option>
                <xs:descriptionOptions clazz="interact.Comment" property="hided" value="${search.hided}"/>
            </select>
        </div>
        <div class="col-xs-1 no-padder m-b-md text-right">
            <label class="control-label">内容：</label>
        </div>
        <div class="col-xs-3  m-b-md">
            <input name="dynamic[content]" type="text" class="form-control" placeholder="模糊查询">
        </div>
        <div class="col-xs-1 no-padder m-b-md text-right">
            <label class="control-label">评论时间：</label>
        </div>
        <div class="col-xs-5 m-b-md">
            <div class="row">
                <div class="col-xs-5 ">
                    <input type="text" name="dynamic[startTime]" class="form-control datepicker" readonly>
                </div>
                <label class="pull-left control-label" style="width: 15px">至</label>
                <div class="col-xs-5">
                    <input type="text" name="dynamic[endTime]" class="form-control datepicker" readonly>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-xs-3 pull-right">
            <a class="btn btn-info pull-right" onclick="commentRefreshList();">搜索</a>
            <a class="btn btn-default pull-right m-r-sm" onclick="$('#tempCommentForm').xsClean();commentRefreshList();">重置</a>
        </div>
    </div>
</form>
<div class="panel panel-default m-b-none">
    <table id="pluTableId" class="table text-center table-bordered table-striped m-b-none">
        <thead>
        <tr>
            <th width="600">评论内容</th>
            <th>评论人</th>
            <th>评论人手机</th>
            <th>点赞数</th>
            <th>回复数</th>
            <th>状态</th>
            <th>评论时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
    </table>
</div>
<div id="commentPagination" class="xs-pagination"></div>

<script id="pluTrId" type="text/html">
    <tr>
        <td>{{content}}</td>
        <td>{{user.nickname}}</td>
        <td>{{user.mobile}}</td>
        <td>{{praiseNum}}</td>
        <td>{{commentNum}}</td>
        <td>
            {{if hided==1}}
            <span class="text-danger">隐藏</span>
            {{else}}
            <span class="text-success">展示</span>
            {{/if}}
        </td>
        <td>{{commentTime}}</td>
        <td>
            <sec:authorize access="hasAnyRole(${xs:getPermissions('interact_comment_hided')})">
                {{if hided==0}}
                <button class="btn btn-danger btn-xs" onclick="updateHided('{{id}}',1)">隐藏</button>
                {{else}}
                <button class="btn btn-success btn-xs" onclick="updateHided('{{id}}',0)">展示</button>
                {{/if}}
            </sec:authorize>
            <sec:authorize access="hasAnyRole(${xs:getPermissions('interact_comment_query_reply')})">
                <a data-toggle="modal" href="<%=request.getContextPath()%>/admin/interact/comment/reply?relativeId={{id}}" data-target="#commentReplyModel"
                   class="btn btn-info btn-xs">查看回复</a>
            </sec:authorize>
        </td>
    </tr>
</script>

<script>
    function commentRefreshList() {
        var params = $("#tempCommentForm").xsJson();
        params.module = ${param.module};
        params.relativeId = ${param.relativeId};
        $("#commentPagination").pagination({
            url: "<%=request.getContextPath()%>/admin/interact/comment/list",
            params: params
        });
    }

    commentRefreshList();
</script>

<sec:authorize access="hasAnyRole(${xs:getPermissions('interact_comment_hided')})">
    <script>
        function updateHided(id, status) {
            doPost('<%=request.getContextPath()%>/admin/interact/comment/hided',
                {
                    id: id,
                    hided: status
                }, function (data) {
                    if (data.status) {
                        bootoast({message: "操作成功！",timeout:2});
                        commentRefreshList();
                    } else {
                        alert(data.message);
                    }
                });
        }
    </script>
</sec:authorize>

<sec:authorize access="hasAnyRole(${xs:getPermissions('interact_comment_query_reply')})">
    <div class="modal fade" id="commentReplyModel" data-backdrop="static" role="dialog">
        <div class="modal-dialog" style="width: 60%;">
            <div class="modal-content">
            </div>
        </div>
    </div>
    <script>
        $(function () {
            $("#commentReplyModel").on('hide.bs.modal', function () {
                $(this).removeData("bs.modal");
            });
        });
    </script>
</sec:authorize>
</body>
</html>
