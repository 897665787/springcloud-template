<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<title>concept主页</title>
</head>
<body>
	<p> concept demo
	<p> 传递来的数据值userId: ${userId}
	<input type="hidden" value="${userId}" id="userId"/>
    <p>【toUserId】：
    <div><input id="toUserId" name="toUserId" type="text" value=""></div>
    <p>【message】：
    <div><input id="contentText" name="contentText" type="text" value="hello websocket"></div>
    <p>【操作】：
    <div>
        <button type="button" onclick="sendMessage()">发送消息</button>
    </div>
    <div id="mmmm"></div>
</body>
<script type="text/javascript">
    var socket;
    if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    } else {
        console.log("您的浏览器支持WebSocket");
        //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接

        var userId = document.getElementById("userId").value;
        console.log("userId-->" + userId);
        
        // 给指定路径的客户端发送消息
        //var reqUrl = "ws://localhost:6001/concept-websocket/kefu?userId=" + userId;
        
        // 给指定用户发送消息
        var reqUrl = "ws://localhost:6001/concept-websocket/user?userId=" + userId;
        console.log("reqUrl:",reqUrl);
        socket = new WebSocket(reqUrl.replace("http", "ws"));
        //打开事件
        socket.onopen = function () {
            console.log("Socket 已打开");
            //socket.send("这是来自客户端的消息" + location.href + new Date());
        };
        //获得消息事件
        socket.onmessage = function (msg) {
            console.log("onmessage--" + msg.data);
            //发现消息进入    开始处理前端触发逻辑
            var mmmm = document.getElementById("mmmm");
            mmmm.innerHTML = mmmm.innerHTML+"<br/>"+msg.data;
        };
        //关闭事件
        socket.onclose = function () {
            console.log("Socket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
            alert("Socket发生了错误");
            //此时可以尝试刷新页面
        }
        //离开页面时，关闭socket
        //jquery1.8中已经被废弃，3.0中已经移除
        // $(window).unload(function(){
        //     socket.close();
        //});
    }

    function sendMessage() {
        if (typeof (WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
        } else {
            // console.log("您的浏览器支持WebSocket");
            var toUserId = document.getElementById('toUserId').value;
            var contentText = document.getElementById('contentText').value;
            var msg = JSON.stringify({
        	  toUserId: toUserId,
        	  message: contentText
        	});
            console.log(msg);
            socket.send(msg);
        }
    }

</script>
</html>