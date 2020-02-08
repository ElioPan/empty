$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/custom/materiel/update",
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

		},
		messages : {

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