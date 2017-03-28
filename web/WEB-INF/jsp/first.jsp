<%--
  Created by IntelliJ IDEA.
  User: zhangqiang
  Date: 2016/12/12
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>First</title>

    <link href="/datatables/datatables.min.css"/>
    <link href="/ui_plugins/chosen.min.css"/>
    <link href="/ui_plugins/jquery-ui.css"/>

    <script src="/plugins/jquery/jquery.min.js"></script>
    <script src="/datatables/datatables.min.js"></script>
    <script src="/ui_plugins/chosen.jquery.js"></script>
    <script src="/ui_plugins/jquery-ui.js"></script>
    <script src="/js/first.js"></script>
<body>
<form id="helloForm" action="/mvc/toHello" hidden="true">
</form>
<table>
    <tr>
        <td><span>ajax请求：</span></td>
        <td><input id="btn_getUserById" type="button" value="getUserByID"></td>
        <td><input id="btn_getSomeUsers" type="button" value="getSomeUsers"></td>
    </tr>
    <tr>
        <td><span>form提交：</span></td>
        <td><input id="submit" type="button" value="sayHello"/></td>
        <td></td>
    </tr>
</table>
<table id="displayTable">
    <thead>
    <tr>
        <th style="width: 50px">ID</th>
        <th style="width: 80px">用户名</th>
        <th style="width: 70px">密码</th>
        <th style="width: 50px">年龄</th>
    </tr>
    </thead>
    <tbody id="tbody"></tbody>
</table>
</body>
</html>
