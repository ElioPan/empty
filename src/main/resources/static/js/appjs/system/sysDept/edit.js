$().ready(function() {
    selectServicesLoad();
	validateRule();
});

function selectServicesLoad() {
    var typeId = $("#typeId").val();
    var html = "";
    $.ajax({
        url : '/custom/dictionary/objectList',
        data: { typeValue: "dept_type"},
        success : function(data) {
            //加载数据
            for (var i = 0; i < data.length; i++) {
                if(typeId == data[i].id){
                    html += '<option value=' + data[i].id + ' selected="selected">' + data[i].name + '</option>'
                }else
                    html += '<option value=' + data[i].id + '>' + data[i].name + '</option>'
            }
            $("#type").append(html);
            $("#type").chosen({
                search_contains: true,
                maxHeight : 200
            });
        }
    });
}

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/system/sysDept/update",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入名字"
			}
		}
	})
}