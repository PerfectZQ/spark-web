<%--
  Created by IntelliJ IDEA.
  User: zhangqiang
  Date: 2017/3/20
  Time: 17:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>详细信息</title>
    <script src="/plugins/jquery/jquery.min.js"></script>
    <script src="/js/visitInformationDetailsDisplay.js"></script>
</head>
<body>
<input id="userId" hidden="true" type="text" value="<%= request.getParameter("userId")%>"/>
<h3 id="user"></h3>
<table id="displayTable" border="1" cellpadding="5">
    <thead id="thead">
    <tr id="tr"></tr>
    </thead>
    <tbody id="tbody"></tbody>
</table>
</body>
</html>
