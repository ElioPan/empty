(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6efd189f"],{"0f7e":function(e,t,r){},"7f7f":function(e,t,r){var i=r("86cc").f,a=Function.prototype,o=/^\s*function ([^ (]*)/,l="name";l in a||r("9e1e")&&i(a,l,{configurable:!0,get:function(){try{return(""+this).match(o)[1]}catch(e){return""}}})},9147:function(e,t,r){"use strict";var i=r("0f7e"),a=r.n(i);a.a},c0b1:function(e,t,r){e.exports=r.p+"assets/img/moren_person.png"},dd27:function(e,t,r){"use strict";r.r(t);var i=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"white p20"},[i("el-form",{ref:"form",staticClass:"page_form",attrs:{model:e.form,rules:e.rules,"label-width":"100px"}},[i("el-form-item",{attrs:{label:"请假类型:",prop:"type"}},[i("el-select",{attrs:{placeholder:"请选择"},model:{value:e.form.type,callback:function(t){e.$set(e.form,"type",t)},expression:"form.type"}},e._l(e.getType,function(e){return i("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})}),1)],1),i("el-form-item",{attrs:{label:"开始时间:",prop:"beginTime"}},[i("el-date-picker",{attrs:{type:"date",placeholder:"选择日期","value-format":"yyyy-MM-dd"},model:{value:e.form.beginTime,callback:function(t){e.$set(e.form,"beginTime",t)},expression:"form.beginTime"}})],1),i("el-form-item",{attrs:{label:"结束时间:",prop:"endTime"}},[i("el-date-picker",{attrs:{type:"date",placeholder:"选择日期","value-format":"yyyy-MM-dd"},model:{value:e.form.endTime,callback:function(t){e.$set(e.form,"endTime",t)},expression:"form.endTime"}})],1),i("el-form-item",{attrs:{label:"时长(小时):",prop:"timeArea"}},[i("el-input",{staticClass:"width-22",attrs:{placeholder:"请输入时长(必填)"},model:{value:e.form.timeArea,callback:function(t){e.$set(e.form,"timeArea",t)},expression:"form.timeArea"}})],1),i("el-form-item",{staticClass:"tesh"},[i("p",[e._v("时长讲自动计入考勤统计")]),i("p",[e._v("请假同步考勤，节省80%统计成本。"),i("a",[e._v("了解详情>>")])])]),i("el-form-item",{attrs:{label:"请假事由:",prop:"reason"}},[i("el-input",{attrs:{type:"textarea"},model:{value:e.form.reason,callback:function(t){e.$set(e.form,"reason",t)},expression:"form.reason"}})],1),i("el-form-item",{attrs:{label:"图片:",prop:"desc"}},[i("el-upload",{attrs:{action:e.uploadUrl,"list-type":"picture-card","on-preview":e.handlePictureCardPreview,"on-success":e.uploadSuccess,"on-progress":e.onUploadProgress,"on-remove":e.handleRemove}},[i("i",{staticClass:"el-icon-plus"})])],1),i("el-form-item",{attrs:{label:"发给谁:",prop:"targetList"}},[e._l(e.personArr,function(t){return e.personArr.length>0?i("el-col",{staticClass:"tac",attrs:{span:2}},[i("a",{on:{click:e.copyTo}},[i("img",{attrs:{src:r("c0b1"),alt:""}})]),i("p",[e._v(e._s(t.name))])]):e._e()}),i("el-col",{staticClass:"yuan tac",attrs:{span:2}},[i("a",{on:{click:e.copyTo}},[e._v("+")]),i("p",[e._v("添加人")])])],2),i("el-form-item",[i("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.submitForm("form")}}},[e._v("提交")])],1)],1),i("el-dialog",{attrs:{title:"请选择抄送人",visible:e.checkToDialog,width:"30%","show-close":""},on:{"update:visible":function(t){e.checkToDialog=t}}},[i("el-row",{attrs:{gutter:24}},[i("el-col",{attrs:{span:12}},[i("el-input",{attrs:{placeholder:"输入关键字进行过滤"},model:{value:e.filterText,callback:function(t){e.filterText=t},expression:"filterText"}}),i("el-tree",{ref:"tree",staticClass:"filter-tree",attrs:{data:e.getUserList,props:e.defaultProps,"show-checkbox":"","node-key":"id","default-expand-all":"","default-checked-keys":e.backfillArr,"filter-node-method":e.filterNode},on:{"check-change":e.handleCheckChange}})],1),i("el-col",{attrs:{span:12}})],1),i("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{attrs:{type:"primary"},on:{click:function(t){e.checkToDialog=!1}}},[e._v("确 定")])],1)],1),i("el-dialog",{attrs:{visible:e.dialogVisible},on:{"update:visible":function(t){e.dialogVisible=t}}},[i("img",{attrs:{width:"100%",src:e.dialogImageUrl,alt:""}})])],1)},a=[],o=(r("7f7f"),{name:"AddLeave",data:function(){var e=function(e,t,r){var i=/^[0-9]+.?[0-9]*$/;if(!i.test(t))return r(new Error("请输入数字"));r()},t=function(e,t,r){if(t.length<1)return r(new Error("请选择抄送人"));r()};return{uploadUrl:this.UploadEnc,form:{targetList:[],taglocationappearanceImage:[]},rules:{type:[{required:!0,message:"请选择请假类型",trigger:"change"}],beginTime:[{required:!0,message:"请选择开始日期",trigger:"change"}],endTime:[{required:!0,message:"请选择结束日期",trigger:"change"}],reason:[{required:!0,message:"请填写请假事由",trigger:"blur"}],timeArea:[{required:!0,message:"请填写时长",trigger:"blur"},{validator:e,trigger:"blur"}],targetList:[{required:!0,validator:t,trigger:"blur"}]},checkToDialog:!1,getUserList:[],getType:[],filterText:"",defaultProps:{children:"children",label:"name"},personArr:[],backfillArr:[],dialogImageUrl:"",dialogVisible:!1}},methods:{handleRemove:function(e,t){this.form.taglocationappearanceImage.push(this.form.taglocationappearanceImage.indexOf(e.url),1)},handlePictureCardPreview:function(e){this.dialogImageUrl=e.url,this.dialogVisible=!0},uploadSuccess:function(e,t,r){0===e.code?this.form.taglocationappearanceImage.push(e.fileName):this.$msg.e(e.msg)},onUploadProgress:function(e,t,r){},copyTo:function(){if(this.checkToDialog=!0,this.personArr.length>0)for(var e=0;e<this.personArr.length;e++)this.backfillArr.push(this.personArr[e].user_id)},handleCheckChange:function(e,t,r){!0===t?this.personArr.push(e):!1===t&&this.personArr.splice(this.personArr.indexOf(e),1)},filterNode:function(e,t){return!e||-1!==t.name.indexOf(e)},submitForm:function(e){var t=this,r=this;this.$refs[e].validate(function(e){if(!e)return!1;for(var i=0;i<t.personArr.length;i++)t.form.targetList.push(t.personArr[i].id);r.leaveApplySubmit(r.form).then(function(e){0===e.code?r.$router.push({path:"/leave_list"}):r.$msg.e(e.msg)})})},initialize:function(){var e=this;this.commonGetUser().then(function(t){0===t.code&&(e.getUserList=t.datas)}),this.dictGetDictsByTpe({type:"leave_apply_type"}).then(function(t){0===t.code&&(e.getType=t.datas)})}},mounted:function(){var e=this;this.$route.query&&this.$route.query.id&&this.leaveApplyDetail({id:this.$route.query.id}).then(function(t){if(0===t.code&&(e.form=t.leaveApply,e.form.targetList=t.targetList,e.personArr=t.targetList,t.initFileList&&t.initFileList.length>0)){e.form.taglocationappearanceImage=[];for(var r=0;r<t.initFileList.length;r++)e.form.taglocationappearanceImage.push(t.initFileList[r].filePath)}}),this.initialize()},watch:{filterText:function(e){this.$refs.tree.filter(e)}}}),l=o,s=(r("9147"),r("2877")),n=Object(s["a"])(l,i,a,!1,null,"2e3d0f91",null);t["default"]=n.exports}}]);