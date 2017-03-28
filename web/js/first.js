/**
 * Created by zhangqiang on 2016/12/14.
 */

/**
 * JSON(JavaScript Object Notation)
 * JSON是 JavaScript 原生格式，这意味着在 JavaScript 中处理
 * JSON数据不须要任何特殊的 API 或工具包。
 */

// 一个json对象的样子 {"key1":"value1","key2":"value2"}
// 一个json字符串的样子 '{"key1":"value1","key2":"value2"}'

/**
 * json对象 => json字符串
 */
var jsonString = JSON.stringify({"key1": "value1", "key2": "value2"});
/**
 * json字符串 => json对象
 */
// 方式1
var jsonObject1 = eval('(' + jsonString + ')');

// 方式2
var jsonObject2 = JSON.parse(jsonString);

/**
 * 文档就绪函数
 * 作用：为了防止文档资源在完全加载完成之前运行jquery代码，如：隐藏一个还
 * 未加载出来的元素，获得未完全加载图像的大小
 */
$(document).ready(function () {
    // 用jquery给btn添加点击事件
    $("#btn_getUserById").click(function () {
        getUserById();
    });
    $("#btn_getSomeUsers").click(function () {
        getSomeUsers();
    });
    $("#submit").click(function () {
        $("#helloForm").submit();
    });
});

/**
 * ajax获取对象集合
 */
function getSomeUsers() {
    $.ajax({
        // ajax 请求的springMVC地址
        url: '/mvc/getSomeUsers',
        // ajax 请求方式
        type: 'post',
        // ajax 直接传递json对象
        data: {beginIndex: 1, endIndex: 4},
        // 注：不需要下面的代码，写下面的代码会出错
        // contentType: 'application/json; charset=utf-8',
        // ajax 预期服务器返回的数据类型
        dataType: 'json',
        // ajax 成功回调
        success: function (jsonArray) {

            var html = '';
            // 清空<tbody>标签内的元素
            $("#tbody").html(html);
            // 遍历数组有两个参数,index是遍历jsonArray的下标,json是遍历出的对象
            $.each(jsonArray, function (index, json) {
                html += '<tr>';
                // 遍历json对象只有一个参数，即json对象的key
                $.each(json, function (key) {
                    html += '<td align="center"><span>' + json[key] + '</span></td>';
                });
                html += '</tr>';
            });
            $("#tbody").append(html);
        }
    });
}

function getUserById() {
    $.ajax({
        url: '/mvc/getUserById?id=2',
        // 注：如果SpringMVC中使用注解@RequestBody，此处必须传递json字符串
        data: JSON.stringify({id: 1}),
        // 注：如果上面传递的是json字符串，必须标注ajax请求参数的参数类型
        contentType: 'application/json; charset=utf-8',
        type: 'post',
        dataType: 'json',
        success: function (json) {
            var html = '';
            $("#tbody").html(html);
            html += '<tr>';
            $.each(json, function (key) {
                html += '<td align="center">' +json[key] +'</td>';
            });
            html += '</tr>';
            $("#tbody").append(html);
        }
    });
}