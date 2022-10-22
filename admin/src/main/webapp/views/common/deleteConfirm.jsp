<%--
  Created by IntelliJ IDEA.
  User: gustinlau
  Date: 26/10/2017
  Time: 4:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--删除操作--%>
<div class="modal fade" id="deleteModel" data-backdrop="static" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">信息确认</h4>
            </div>
            <div class="modal-body">
                <h4 class="text-danger" id="deleteModelMsg">确定删除？</h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-danger" onclick="__deleteModelConfirm__()">确定</button>
            </div>
        </div>
    </div>
</div>

<script>
    var __deleteModelConfirm__;
    function showDeleteModel(msg, confirm) {
        if (msg !== undefined && msg !== null) {
            $("#deleteModelMsg").html(msg);
        }else{
            $("#deleteModelMsg").html("确定删除？");
        }
        if (confirm !== undefined && confirm !== null) {
            __deleteModelConfirm__ = function () {
                $("#deleteModel").modal('hide');
                confirm();
            };
        }else{
            $("#deleteModel").modal('hide');
        }
        $("#deleteModel").modal('show');
    }
</script>
