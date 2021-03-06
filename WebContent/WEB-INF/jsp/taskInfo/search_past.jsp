<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">  
  function showMe(curr){
	  var val=$(curr).combobox('getValue');
	  var trData=$(curr).closest('tr').find('td:last');
	  
	  if(val=='createTaskDate' ||val=='endHandleDate'){		 
		  trData.find('input').remove();
		  trData.find('span').remove();
		  var input="<input  name='searchVals' class='easyui-datebox' required='required' style='width: 150px;'>";
	      trData=trData.append(input);
	  }else{
		  trData.find('input').remove();
		  trData.find('span').remove();
		  var input="<input  name='searchVals' class='easyui-textbox' required='required' style='width: 150px;'>";
		  trData=trData.append(input); 	 
	  }
	  $.parser.parse(trData);  //为了刷新页面，否则easyUI的样式加载不上
	  targetObj.find('td:last span input[class=textbox-value]').attr("name","searchVals");
  }
  </script>
<form id="taskInfoPastSearch" method="post">
<div class="table-responsive">
    <table class="table table-bordered table-hover table-condensed">
    	<tr class="active">
			<td>条件</td>
			<td>字段名</td>
			<td>条件</td>
			<td>值</td>
		</tr>
		<tr>
			<td>
				<select name="searchAnds" class="easyui-combobox" style="width:80px;" data-options="editable:false,panelHeight:'auto'"> 
					<option value="and">并且</option>
					<option value="or">或者</option>
				</select>
			</td>
			<td>
				<select name="searchColumnNames" class="easyui-combobox" style="width:80px;" data-options="editable:false,panelHeight:'auto'">
					<option value="title">关键词</option>
					<option value="taskSource">任务来源</option>
					<option value="createTaskDate">立项时间</option>
					<option value="endHandleDate">办结时间</option>
					<option value="hostGroup">牵头单位</option>
					<option value="urgency">急缓程度</option>
					<option value="major">主管市长</option>
				</select>
			</td>
			<td>
				<select name="searchConditions" class="easyui-combobox" style="width:80px;" data-options="editable:false,panelHeight:'auto'">
					<option value="=">等于</option>
					<option value="<>">不等于</option>
					<option value="<">小于</option>
					<option value=">">大于</option>
					<option value="like">模糊</option>
				</select>
			</td>
			<td><input id="searchVals" name="searchVals" class="easyui-textbox" required="required" style="width: 150px;">
				<!-- <a style="display: none;" href="javascript:void(0);" onclick="searchRemove(this);">删除</a> -->
				<button type="button" style="display: none;" class="close" data-dismiss="alert" aria-label="Close" onclick="searchRemove(this);"><div aria-hidden="true">&times;</div></button>
			</td>
		</tr>
    </table>
</div>
</form>
    
