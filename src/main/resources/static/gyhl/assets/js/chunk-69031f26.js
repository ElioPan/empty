(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-69031f26"],{"170e":function(t,e,n){},"39fa":function(t,e,n){"use strict";var a=n("170e"),i=n.n(a);i.a},e3ea:function(t,e,n){"use strict";n.r(e);var a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"white p20"},[n("el-button",{attrs:{size:"mini"}},[t._v("新建")]),n("el-button",{attrs:{size:"mini"}},[t._v("删除")]),n("el-button",{attrs:{size:"mini"}},[t._v("我发起的")]),n("el-button",{attrs:{size:"mini"}},[t._v("我审批的")]),n("el-button",{attrs:{size:"mini"}},[t._v("抄送我的")]),n("el-button",{attrs:{size:"mini"}},[t._v("我的团队")]),n("el-input"),n("el-table",{staticClass:"theme mt-20",staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.ckAll}},[n("el-table-column",{attrs:{type:"selection",width:"50"}}),n("el-table-column",{attrs:{prop:"createuser",width:"100",label:"填报人"}}),n("el-table-column",{attrs:{prop:"typename",width:"100",label:"请假类型"}}),n("el-table-column",{attrs:{prop:"timearea",width:"120",label:"请假市场(小时)"}}),n("el-table-column",{attrs:{prop:"reason","show-overflow-tooltip":"",label:"请假事由"}}),n("el-table-column",{attrs:{prop:"begintime","show-overflow-tooltip":"",label:"请假开始时间"}}),n("el-table-column",{attrs:{prop:"endtime","show-overflow-tooltip":"",label:"请假结束时间"}}),n("el-table-column",{attrs:{prop:"statusname",label:"状态"}}),n("el-table-column",{attrs:{label:"操作",width:"210"},scopedSlots:t._u([{key:"default",fn:function(e){return[n("el-button",{attrs:{size:"mini"},on:{click:function(n){return t.del(e.row.id)}}},[t._v("删除")]),n("el-button",{attrs:{size:"mini"},on:{click:function(n){return t.$router.push({path:"/add_leave",query:{id:e.row.id}})}}},[t._v("详情\n                    ")]),e.row.assignid===t.userId?n("el-button",{attrs:{size:"mini"},on:{click:function(n){return t.examine(e.row.id)}}},[t._v("审批")]):t._e()]}}])})],1),n("el-pagination",{attrs:{"current-page":t.pg.pageno,"page-sizes":[10,20,30,40,50,100],"page-size":t.pg.pagesize,layout:"prev, pager, next,sizes",total:t.allTotal,small:!0},on:{"update:currentPage":function(e){return t.$set(t.pg,"pageno",e)},"update:current-page":function(e){return t.$set(t.pg,"pageno",e)},"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}})],1)},i=[],l={data:function(){return{pg:{pageno:1,pagesize:10,userId:"",username:"",deptId:"",approveUserId:""}}},methods:{initialize:function(){},handleSizeChange:function(t){this.pg.pagesize=t,this.initialize()},handleCurrentChange:function(t){this.pg.pageno=t,this.initialize()}},mounted:function(){this.initialize()}},o=l,r=(n("39fa"),n("2877")),s=Object(r["a"])(o,a,i,!1,null,"4bc350d5",null);e["default"]=s.exports}}]);