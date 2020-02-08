
var prefix = "/custom/taskMain"
$(function() {
	load();
    $('#data_5 .input-daterange').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });
});

function selectStatusLoad() {
    var html = "";
    $.ajax({
        url : '/custom/dictionary/objectList',
        data: { typeValue: "task_status"},
        success : function(data) {
            //加载数据
            for (var i = 0; i < data.length; i++) {
                html += '<option value=' + data[i].id + '>' + data[i].name + '</option>'
            }
            $("#status").append(html);
            $("#status").chosen({
                search_contains: true,
                maxHeight : 200
            });
            //点击事件
            $('#status').on('change', function(e, params) {
                console.log(params.selected);
                var opt = {
                    query : {
                        status : params.selected,
                    }
                }
                $('#exampleTable').bootstrapTable('refresh', opt);
            });
        }
    });
}

function load() {
    selectStatusLoad();
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/listForMap", // 服务器数据的加载地址
					//	showRefresh : true,
					//	showToggle : true,
					//	showColumns : true,
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						// queryParamsType : "limit",
						// //设置为limit则会发送符合RESTFull格式的参数
						singleSelect : false, // 设置为true将禁止多选
						// contentType : "application/x-www-form-urlencoded",
						// //发送到服务器的数据编码类型
						pageSize : 3, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						//search : true, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
					            status:$('#status').val(),
								title:$('#title').val(),
                                createBy:$('#createBy').val(),
                                heldPerson:$('#heldPerson').val(),
                                startTime:$('#startTime').val(),
                                endTime:$('#endTime').val()
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
									field : 'taskno',
									title : '任务编号'
								},
								{
									field : 'title', 
									title : '主题' 
								},
								{
									field : 'status',
									title : '处理状态'
								},
								{
									field : 'heldpersonname',
									title : '责任人'
								},
								{
									field : 'deptname',
									title : '责任部门'
								},
								{
									field : 'expiredate',
									title : '要求完成日期' 
								},
								{
									field : 'checktime',
									title : '实际验收时间'
								},
								{
									field : 'overtime',
									title : '超时时间'
								},
								{
									field : 'applypersonname',
									title : '发起人'
								},
								{
									field : 'createtime',
									title : '发起时间'
								},
								{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.id
												+ '\')"><i class="fa fa-edit"></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.id
												+ '\')"><i class="fa fa-remove"></i></a> ';
										var f = '<a class="btn btn-success btn-sm' + s_deal_h + '" href="#" title="处理"  mce_href="#" onclick="deal(\''
                                            + row.id
                                            + '\')"><i class="fa fa-wrench"></i></a> ';
                                        var g = '<a class="btn btn-info btn-sm' + s_detail_h + '" href="#" title="详情"  mce_href="#" onclick="detail(\''
                                            + row.id
                                            + '\')"><i class="fa fa-paste"></i></a> ';
                                        var h = '<a class="btn btn-info btn-sm' + s_check_h + '" href="#" title="验收"  mce_href="#" onclick="check(\''
                                            + row.id
                                            + '\')"><i class="fa fa-check"></i></a> ';
                                        if(row.statusvalue == 'waiting_deal'){
                                        	return e + d + f+ g;
										}else if(row.statusvalue == 'waiting_check'){
                                        	return f + g + h;
                                        }else if(row.statusvalue == 'already_check'){
                                            return g;
										}
										return e + d + f + g + h;
									}
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
function add() {
	var window = layer.open({
		type : 2,
		title : '新增任务单',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add' // iframe的url
	});
    layer.full(window);
}
function edit(id) {
    var window = layer.open({
		type : 2,
		title : '修改任务单',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
    layer.full(window);
}

function detail(id) {
    var window = layer.open({
        type : 2,
        title : '任务单详情',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '100%', '100%' ],
        content : prefix + '/detail/' + id // iframe的url
    });
    layer.full(window);
}

function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function deal(id) {
    var window = layer.open({
        type : 2,
        title : '处理任务单',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '100%', '100%' ],
        content : prefix + '/deal/' + id // iframe的url
    });
    layer.full(window);
}

function check(id) {
    var window = layer.open({
        type : 2,
        title : '验收任务单',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '100%', '100%' ],
        content : prefix + '/check/' + id // iframe的url
    });
    layer.full(window);
}

function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
}