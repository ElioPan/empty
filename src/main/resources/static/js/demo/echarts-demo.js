$(function () {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('echarts-line-chart'));
    var data1 = [];
    var data2 = [];
    //指定图表的配置项和数据
    var option = {
        title: { text: '' },
        grid:{
            x:25,
            y:45,
            x2:5,
            y2:20,
            borderWidth:1
        },
        tooltip: {},
        legend: { data:[] },
        xAxis: { data: data1 },
        yAxis: {},
        series: [{
            name: '数值',
            itemStyle:{ normal:{ color:'#2EFE64' } },
            type: 'bar',
            data: data2
    }] };
    //后台接口中获取数据
    $.ajax({
        url: "/custom/questionnaire/getCountByModule",
        dataType: 'JSON',
        type: 'POST',
        async : false,
        success: function (res) {
        var data = res.list;
        for (var i=0;i<data.length;i++){
            // data1数据为横轴数据 d
            data1.push(data[i].keyStr);
            //data1数据为纵轴数据
            data2.push(data[i].valueStr);
        }
        //使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            window.onresize = myChart.resize;
        }
    });


    var barChart = echarts.init(document.getElementById('echarts-bar-chart'));
    var data1 = [];
    var data2 = [];
    //指定图表的配置项和数据
    var baroption = {
        title: { text: '' },
        grid:{
            x:25,
            y:45,
            x2:5,
            y2:20,
            borderWidth:1
        },
        tooltip: {},
        legend: { data:[] },
        xAxis: { data: data1 },
        yAxis: {},
        series: [{
            name: '人数',
            itemStyle:{ normal:{ color:'#4ad2ff' } },
            type: 'bar',
            data: data2
        }] };
    //后台接口中获取数据
    $.ajax({
        url: "/custom/signin/getCountByLessons",
        dataType: 'JSON',
        type: 'POST',
        async : false,
        success: function (res) {
            var data = res.list;
            for (var i=0;i<data.length;i++){
                // data1数据为横轴数据 d
                data1.push(data[i].keyStr);
                //data1数据为纵轴数据
                data2.push(data[i].valueStr);
            }
            //使用刚指定的配置项和数据显示图表。
            barChart.setOption(baroption);
            window.onresize = barChart.resize;
        }
    });



});
