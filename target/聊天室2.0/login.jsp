<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆</title>
    <script src="js/jquery.min.js"></script>
    <script src="js/login.js"></script>

</head>
<body>
<form action="/com/message?action=login" method="post" onsubmit="check()">
    <label>用户名</label>
    <input type="text" name="username" id="username">
    <input type="submit" value="登陆">
</form>
</body>
</html>
