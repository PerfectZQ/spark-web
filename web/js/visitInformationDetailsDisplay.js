/**
 * Created by zhangqiang on 2017/3/20.
 */
$(document).ready(function () {
    var userId = $("#userId").val();
    getDetailVisitInformation(userId);
    getOneVisitVector(userId);
});

/**
 * 根据userId获取教师详细文档集（每个教师家访学生的相关文字信息），以表格的形式展现在页面
 * @param userId 教师的唯一标识
 */
function getDetailVisitInformation(userId) {
    $.ajax({
        url: '/textAnalyse/getDetailVisitInformation',
        data: JSON.stringify({userId: userId}),
        // 注：如果上面传递的是json字符串，必须标注ajax请求参数的参数类型
        contentType: 'application/json; charset=utf-8',
        type: 'post',
        dataType: 'json',
        success: function (jsonArray) {
            var flag = true;
            var theadHtml = '';
            var tbodyHtml = '';
            var userId = '';
            $("#thead #tr").html(theadHtml);
            $("#tbody").html(tbodyHtml);
            $.each(jsonArray, function (index, json) {
                userId = json['userId'];
                var detailInfo = json['detailInfo'];
                tbodyHtml += '<tr>';
                $.each(detailInfo, function (key) {
                    var column = key;
                    var text = detailInfo[column];
                    if (flag) {
                        theadHtml += '<th align="left"><span style="width: 300px;display: inline-block">' + column + '</span></th>';
                    }
                    tbodyHtml += '<td align="left"><span>' + text + '</span></td>';
                });
                tbodyHtml += '</tr>';
                flag = false;
            });
            $("#tbody").append(tbodyHtml);
            $("#thead #tr").append(theadHtml);
            $("#user").empty().append(userId);
        }
    });
}

/**
 * 根据userId获取通过TF-IDF生成的教师的差异性向量
 * @param userId 教师的唯一标识
 */
function getOneVisitVector(userId) {
    $.ajax({
        url: '/textAnalyse/getOneVisitVectorById',
        data: {userId: userId},
        type: 'post',
        dataType: 'json',
        success: function (json) {
            var points = json['points'];
            var columnNames = json['columnNames'];
            var clusterId = json['clusterId'];
        }
    });
}