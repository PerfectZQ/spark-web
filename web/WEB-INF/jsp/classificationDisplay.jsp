<%--
  Created by IntelliJ IDEA.
  User: zhangqiang
  Date: 2017/3/23
  Time: 15:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>分类情况展示</title>
    <script src="/plugins/jquery/jquery.min.js"></script>
    <script src="/plugins/echarts/echarts.min.js"></script>
    <script src="/js/classificationDisplay.js"></script>
</head>
<body>
<div style="width:100%;height: 350px;">
    <div id="main" style="width:100%;height: 300px;"></div>
    <div style="width:100%;height: 50px;">
        <table width="100%">
            <tr id="tr" align="center"></tr>
        </table>
    </div>
</div>
<div id="pie" style="width: 100%;height: 350px;float: left;"></div>

</body>
</html>
