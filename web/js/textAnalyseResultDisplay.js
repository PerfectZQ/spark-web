/**
 * Created by zhangqiang on 2017/3/13.
 */
$(document).ready(function () {
    getAnalyseResult();
});

/**
 * 获取所有教师的差异性向量和所属类簇情况
 */
function getAnalyseResult() {
    $.ajax({
        // ajax 请求的springMVC地址
        url: '/textAnalyse/getAnalyseResult',
        // ajax 请求方式
        type: 'post',
        // 数据请求格式
        dataType: 'json',
        // ajax 成功回调
        success: function (jsonArray) {
            var isThead = true;
            var html = '';
            var theadHtml = '';
            // 清空<tbody>标签内的元素
            $("#tbody").html(html);
            $("#thead #tr").html(theadHtml);
            // 遍历数组有两个参数,index是遍历jsonArray的下标,json是遍历出的对象
            $.each(jsonArray, function (index, json) {
                html += '<tr>';
                if (isThead) {
                    theadHtml += '<th align="center"><span style="width: 280px;display: inline-block">userId</span></th>';
                    theadHtml += '<th align="center"><span style="width: 40px;display: inline-block">predict</span></th>';
                }
                // <a href="javascript:void(0)" onclick=""></a>
                html += '<td align="center"><a href="javascript:void(0)" onclick="toDisplay(\'' + json['userId'] + '\')">' + json['userId'] + '</a></td>';
                html += '<td align="center">' + parseInt(json['predictIndex']) + '</td>';
                // 遍历json对象只有一个参数，即json对象的key
                $.each(json, function (key) {
                    if ('userId' != key && 'predictIndex' != key) {
                        if (isThead) {
                            var length = key.length;
                            theadHtml += '<th align="center"><span style="width: 150px;display: inline-block">' + key.substring(0, length - 10) + '</span></th>';
                        }
                        html += '<td align="center"><span style="width: 150px;display: inline-block">' + parseFloat(json[key]).toFixed(4) + '</span></td>';
                    }
                });
                html += '</tr>';
                isThead = false;
            });
            $("#tbody").append(html);
            $("#thead #tr").append(theadHtml);
        }
    });
}

/**
 * 跳转页面，显示教师的家访文字详情
 * @param userId
 */
function toDisplay(userId) {
    $("#submitForm").attr("action", "/textAnalyse/visitInformationDetailsDisplay");
    $("#submitForm").attr("method", "post");
    $("#submitForm").html('<input name="userId" type="text" hidden="true" value="' + userId + '"/>');
    $("#submitForm").submit();
}