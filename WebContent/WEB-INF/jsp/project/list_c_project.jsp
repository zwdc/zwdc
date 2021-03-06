<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>待签收任务交办列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/kindeditor/kindeditor-min.js"></script>
  	<script type="text/javascript" src="${ctx}/js/kindeditor.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/project_c.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div id="toolbar" style="padding:2px 0">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="claim();">签收</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="refuse();">签收异议</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="approval();">填写拟办意见</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="details();">详情</a>
				</td>
				<td style="padding-left:5px">
					<input id="searchbox" type="text"/>
				</td>
				<td style="padding-left:5px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="invoiceSearch();">高级查询</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="searchMenu" style="display: none;">
		<div data-options="name:'title'">任务名称</div>
		<div data-options="name:'receiveInvoiceName'">任务来源</div>
		<div data-options="name:'currentPrice'">急缓程度</div>
	</div>
	<table id="project_datagrid" title="任务交办列表"></table>
  </body>
</html>
