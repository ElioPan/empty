$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/custom/materiel/save",
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
            serialNo: "required",
            name:"required",
            specification:"required",
            type:"required"
		},
		messages : {
            serialNo: icon + "请输入物料编号",
            name:"请输入物料名称",
            specification:"请输入物料规格",
            type:"请选择物料类别"
		}
	})
}

$("#type").chosen({
    search_contains: true,
    maxHeight : 200
});
$("#unitUom").chosen({
    search_contains: true,
    maxHeight : 200
});
$("#defaultFacility").chosen({
    search_contains: true,
    maxHeight : 200
});

$("#defaultLocation").chosen({
    search_contains: true,
    maxHeight : 200
});

$("#supportUom").chosen({
    search_contains: true,
    maxHeight : 200
});

$("#lengthUom").chosen({
    search_contains: true,
    maxHeight : 200
});

$("#weightUom").chosen({
    search_contains: true,
    maxHeight : 200
});