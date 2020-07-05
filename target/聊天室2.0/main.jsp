<%@ page import="java.util.List" %>
<%@ page import="com.model.UserInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<html>

<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="js/jquery.min.js"></script>
    <script charset="GB2312" src="js/main.js"></script>
    <link href="css/main.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <header>
        <h3>欢迎<span><%=session.getAttribute("username")%></span>使用在线聊天<br>登陆时间:<span><%=session.getAttribute("loginTime")%></span></h3>
    </header>
    <main>
        <div class="online">
            <ul class="online_ul">

            </ul>
        </div>
        <div id="content_messages" class="content" onload="window.scrollTo(0,document.body.scrollHeight);">

        </div>
    </main>
    <footer>
        <form>
            <label>[<%=session.getAttribute("username")%>] 对：</label>
            <input type="text" name="receive" readonly="readonly" id="receive">
            <label>表情</label>
            <select name="face" id="face" size="1">
                <option value="微笑">微笑</option>
                <option value="无表情">无表情</option>
                <option value="笑呵呵">笑呵呵</option>
                <option value="苦笑">苦笑</option>
                <option value="鼓掌">鼓掌</option>
            </select>
            <input type="checkbox" onclick="checkScrollScreen()">
            <select name="color" size="1" id="color">
                <option style="color: red" value="red">红色热情</option>
                <option style="color: aqua" value="aqua">蓝色开朗</option>
                <option style="color: black" value="black">黑色</option>
            </select>
            <input type="text" name="content" size="70" id="content">
            <input style="margin: 10px 0px" type="button" value="发送" onclick="send('<%=session.getAttribute("username")%>')"><br>
            <input type="button" value="退出" onclick="exit()">
        </form>
    </footer>
</div>
</body>
</html>