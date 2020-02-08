$().ready(function() {
    load();
});

function deal() {
    var id = $("#id").val();
    var window = parent.layer.open({
        type : 2,
        title : '处理任务单',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '100%', '100%' ],
        content : "/custom/taskMain" + '/deal/' + id // iframe的url
    });
    parent.layer.full(window);

}

function load() {
    $('#replyTable')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : "/custom/taskReply" + "/listForMap", // 服务器数据的加载地址
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize : 'outline',
                //toolbar : '#exampleToolbar',
                striped : true, // 设置为true会有隔行变色效果
                dataType : "json", // 服务器返回的数据类型
                pagination : false, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect : false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                //pageSize : 20, // 如果设置了分页，每页数据条数
                //pageNumber : 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                //sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit: params.limit,
                        offset:params.offset,
                        taskid:$('#id').val()
                    };
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns : [
                    {
                        field : 'reason',
                        title : '原因分析'
                    },
                    {
                        field : 'solution',
                        title : '解决方案'
                    },
                    {
                        field : 'replyername',
                        title : '回复人'
                    },
                    {
                        field : 'replytime',
                        title : '回复时间'
                    }]
            });
}

$("#taskType").chosen({
    search_contains: true,
    maxHeight : 200
});
$("#taskLevel").chosen({
    search_contains: true,
    maxHeight : 200
});
$("#heldPerson").chosen({
    search_contains: true,
    maxHeight : 200
});