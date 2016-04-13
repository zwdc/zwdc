package com.hdc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfo;

public interface ITaskInfoService {

	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<TaskInfo> getListPage(Parameter param, Page<TaskInfo> page, Map<String, Object> map) throws Exception;
	
	/**
	 * 通过id获取TaskInfo
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaskInfo findById(Integer id) throws Exception;
	
	/**
	 * 保存
	 * @param taskInfo
	 * @throws Exception
	 */
	public Serializable doAdd(TaskInfo taskInfo) throws Exception;
	
	/**
	 * 修改
	 * @param taskInfo
	 * @throws Exception
	 */
	public void doUpdate(TaskInfo taskInfo) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
	
	/**
	 * 启动任务分配流程 
	 * @param taskInfo
	 * @throws Exception
	 */
	public void doStartProcess(TaskInfo taskInfo) throws Exception;
	
	/**
	 * 审批
	 * @param salesId
	 * @param isPass
	 * @param taskId
	 * @param processInstanceId
	 * @param comment
	 * @throws Exception
	 */
	public void doApproval(Integer salesId, boolean isPass, String taskId, String comment) throws Exception;
	
	/**
	 * 完成任务
	 * @param taskInfo
	 * @param taskId
	 * @throws Exception
	 */
	public void doCompleteTask(TaskInfo taskInfo, String taskId) throws Exception;
}
