<%@ page import="com.model.UserInfo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    List<String> list = UserInfo.getLists();
    int len=0;
    if(list!=null){
        len=list.size();
    }
    String msg="<li>在线列表(<span>"+len+"</span>)</li>";
    for(String s:list){
        msg+="<li class='color'><a href='#' onclick=\"set("+"'"+s+"'"+")\">"+s+"</a></li>";
    }
    out.write(msg);
%>
</body>
</html>
