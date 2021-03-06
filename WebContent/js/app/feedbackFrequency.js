/**
 * 反馈频度
 */

var feedback_datagrid;
var feedback_form;
var feedback_dialog;

$(function() {
	//数据列表
	feedback_datagrid = $('#feedback_datagrid').datagrid({
        url: ctx+"/feedbackFrequency/getList",
        width : 'auto',
		height : fixHeight(1),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field: 'name', title: '频度名称', width: fixWidth(0.3), align: 'left', halign: 'center', sortable: true},
              {field: 'type', title: '频度类型', width: fixWidth(0.2), align: 'left', halign: 'center',
            	  formatter:function(value,row){
            		  if(value == 1) {
            			  return "单次或月频度";
            		  } else if(value == 2) {
            			  return "周频度"
            		  } else if(value == 3) {
            			  return "单次或月频度";
            		  }
            	  }
            	  
              },
              {field: 'createDate', title: '创建日期', width: fixWidth(0.2), align: 'center', sortable: true,
            	  formatter:function(value,row){
            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
				  }
              },
              {field: 'id', title: '反馈频度ID', width: fixWidth(0.2), align: 'center', sortable: true}
    	    ] 
        ],
        toolbar: "#toolbar"
    });
    
	$("#searchbox").searchbox({ 
		menu:"#searchMenu", 
		prompt :'模糊查询',
	    searcher:function(value,name){   
	    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
            var obj = eval('('+str+')');
            feedback_datagrid.datagrid('reload',obj); 
	    }
	});
});

//修正宽高
function fixHeight(percent) {   
	return parseInt($(this).height() * percent);
}

function fixWidth(percent) {   
	return parseInt(($(this).width() - 50) * percent);
}

//初始化表单
function formInit(row) {
	feedback_form = $('#fbFrequencyForm').form({
        url: ctx+"/feedbackFrequency/saveOrUpdate",
        onSubmit: function () {
	        $.messager.progress({
	            title: '提示信息！',
	            text: '数据处理中，请稍后....'
	        });
	        var isValid = $(this).form('validate');
	        if (!isValid) {
	            $.messager.progress('close');
	        } else {
	        	var type = $("input[name='type']:checked").val();
	        	if(typeof(type) == "undefined") {
	        		$.messager.progress('close');
	        		$.messager.alert('温馨提示','至少选择一种反馈类型！','info');
	        		return false;
	        	}
	        }
	        return isValid;
	    },
	    success: function (data) {
	        $.messager.progress('close');
	        var json = $.parseJSON(data);
	        if (json.status) {
	        	feedback_dialog.dialog('destroy');
	        	feedback_datagrid.datagrid('load');
	        	
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
    });
}

//添加
function add(row) {
	var _url = ctx+"/feedbackFrequency/toMain";
	if(row) {
		_url = ctx+"/feedbackFrequency/toMain?id="+row.id;
	}
	feedback_dialog = $('<div/>').dialog({
    	title : "频度信息",
		top: 20,
		width : fixWidth(0.6),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	feedback_form.submit();
                }
            },
            {
                text: '重置',
                iconCls: 'icon-reload',
                handler: function () {
                	feedback_form.form('clear');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	feedback_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	feedback_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	add(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del() {
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的数据?', function (result) {
            if (result) {
                $.ajax({
            		async: false,
            		cache: false,
                    url: ctx + '/feedbackFrequency/delete/'+row.id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                        	feedback_datagrid.datagrid('load');
                        }
                        $.messager.show({
        					title : data.title,
        					msg : data.message,
        					timeout : 1000 * 2
        				});
                    }
                });
            }
        });
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function details(){
    var row = feedback_datagrid.datagrid('getSelected');
    if (row) {
    	showDetails(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function showDetails(row) {
	source_dialog = $('<div/>').dialog({
    	title : "频度详情",
		top: 20,
		width : fixWidth(0.8),
		height : 'auto',
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/feedbackFrequency/details/"+row.id,
        buttons: [
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	source_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	source_dialog.dialog('destroy');
        }
    });
}

