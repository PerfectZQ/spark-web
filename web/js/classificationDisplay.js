/**
 * Created by zhangqiang on 2017/3/23.
 */

$(document).ready(function () {
    getClusterResult();
    getSumOfEveryCluster();
});

var columnNameMap_zh = {
    familyEnvironment: '家庭环境',
    feedbackAdvice: 'ADVICE',
    growEnvironment: '成长环境',
    parentAdvice: '家长建议',
    parentVisitFeeling: 'FEELING',
    studyEnvironment: '学习环境',
    teacherVisitFeeling: '教师家访感受'
};

/**
 * 获取分类后各个类簇的信息（中心点，类簇名字），生成各个类粗的蛛网图
 */
function getClusterResult() {
    $.ajax({
        // 请求mongodb ClusterResult
        url: '/textAnalyse/getClusterResult',
        // ajax 请求方式
        type: 'post',
        // 数据请求格式
        dataType: 'json',
        // ajax 成功回调
        success: function (jsonArray) {
            var series = [];
            var radars = [];
            // 分类名字
            var clusterNames = [];
            $.each(jsonArray, function (index, json) {
                var clusterName = json['name'];
                clusterNames.push(clusterName);
                var clusterId = json['clusterId'];
                var columnNames = json['columnNames'].split(",");
                var values = json['centerPoint'].split(',');
                var indicators = [];
                for (var i = 0; i < values.length; i++) {
                    values[i] = parseFloat(values[i]);
                    var columnName = columnNameMap_zh[columnNames[i].substring(0, columnNames[i].length - 10)];
                    indicators.push({text: columnName, max: 1})
                }
                radars[index] = {
                    indicator: indicators,
                    // radar中心所在的位置
                    center: [(2 * (index + 1) - 1) * 100 / (jsonArray.length * 2) + '%', '65%'],
                    // center: [(2 * (index % 3 + 1) - 1) * 100 / 6 + '%', (index / 3 * 30 + 35) + '%'],
                    radius: 80
                };
                series[index] =
                    {
                        type: 'radar',
                        // 对应的radar下标
                        radarIndex: index,
                        tooltip: {
                            trigger: 'item'
                        },
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: [
                            {
                                value: values,
                                //要和legend中的data数组对应才行
                                name: clusterName
                            }
                        ]
                    };
            });
            var option = {
                title: {
                    text: '差异性向量展示'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    x: 'center',
                    data: clusterNames
                },
                radar: radars,
                series: series
            };
            var myChart = echarts.init(document.getElementById('main'));
            myChart.setOption(option);
            var html = '';
            $("#tr").html(html);
            // 重命名
            for (var i = 0; i < clusterNames.length; i++) {
                html += '<td width="' + 100 / clusterNames.length + '%" align="center" id="td' + i + '"><span onclick="displayRenameInput(' + i + ')">' + clusterNames[i] + '</span></td>';
            }
            $("#tr").html(html);
        }
    });
}

/**
 * 获取教师家访真实性统计结果（各个分类的总数），生成南丁格尔玫瑰图
 */
function getSumOfEveryCluster() {
    $.ajax({
        url: '/textAnalyse/getSumOfEveryCluster',
        type: 'post',
        dataType: 'json',
        success: function (jsonArray) {
            var names = [];
            var values = [];
            $.each(jsonArray, function (index, json) {
                names.push(json['name']);
                values.push(json['sum']);
            });
            var data = [];
            for (var i = 0; i < values.length; i++) {
                data.push({value: values[i], name: names[i]});
            }
            var option = {
                title: {
                    text: '教师家访真实性统计结果',
                    subtext: '南丁格尔玫瑰图',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{b}<br/> {c} ({d}%)"
                },
                legend: {
                    x: 'center',
                    y: 'bottom',
                    data: names
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {
                            show: true,
                            type: ['pie', 'funnel']
                        },
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                series: [{
                    // name: '半径模式',
                    type: 'pie',
                    radius: [30, 110],
                    center: ['25%', '50%'],
                    roseType: 'radius',
                    data: data
                }]
            };
            var myChart = echarts.init(document.getElementById('pie'));
            myChart.setOption(option);
        }
    });
}


/**
 * 显示重命名文本框
 * @param id 第i个图
 */
function displayRenameInput(id) {
    var html = '<input id="renameInput" type="text" onblur="renameCluster(' + id + ')"/>';
    $('#td' + id).empty();
    $('#td' + id).html(html);

}

/**
 * 重命名类簇
 * @param id
 */
function renameCluster(id) {
    var text = $("#renameInput").val();
    if (text == '') {
        alert('名字不能为空！')
        return false;
    }
    $.ajax({
        url: '/textAnalyse/renameClusterId',
        data: {clusterId: id, name: text},
        type: 'post',
        success: function () {
            // 刷新视图
            getClusterResult();
            getSumOfEveryCluster();
        }
    });

}