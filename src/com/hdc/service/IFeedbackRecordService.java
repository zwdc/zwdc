package com.hdc.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;

/**
 * 反馈接口
 * @author ZML
 *
 */
public interface IFeedbackRecordService {

	/**
	 * 获取分页数据
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> getListPage(Parameter param, Page<FeedbackRecord> page) throws Exception;
	/**
	 * 获取所有列表
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> getAllList() throws Exception;
	/**
	 * 添加
	 * @param feedback
	 * @return
	 * @throws Exception
	 */
	public Serializable doAdd(FeedbackRecord feedback) throws Exception;
	
	/**
	 * 更新
	 * @param feedback
	 * @throws Exception
	 */
	public void doUpdate(FeedbackRecord feedback) throws Exception;
	/**
	 * 反馈
	 * @param feedback
	 * @throws Exception
	 */
	public Message doUpdate(FeedbackRecord feedback,MultipartFile[] file,HttpServletRequest request)  throws Exception;
	
	/**
	 * 根据反馈id查询反馈信息
	 * @param id
	 * @return
	 * @throws Exception
	 */	
	public FeedbackRecord findById(Integer id) throws Exception;
	
	/**
	 * 根据taskInfoId查询反馈信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> findByTaskId(Integer id) throws Exception;
	
	/**
	 * 通过指定时间区间查询
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> findByDate(Date beginDate, Date endDate) throws Exception;
	
	/**
	 * 根据项目表查询
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> findByProjectId(Integer projectId) throws Exception;
	
	/**
	 * 查询所有状态不等于accept的
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public List<FeedbackRecord> findNoAccept(String projectId) throws Exception;
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Integer id) throws Exception;
	
	/**
	 * 保存反馈 完成任务
	 * @param feedback
	 * @param taskId
	 * @param file
	 * @param request
	 * @throws Exception
	 */
	public void doCompleteTask(
			FeedbackRecord feedback, 
			@RequestParam(value = "taskId", required = false) String taskId,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request) throws Exception;
	/**
	 * 启动反馈审批流程 
	 * @param feedback
	 * @throws Exception
	 */
	public void doStartProcess(FeedbackRecord feedback) throws Exception;
	
	/**
	 * 审批
	 * @param feedbackId
	 * @param isPass
	 * @param taskId
	 * @param processInstanceId
	 * @param comment
	 * @throws Exception
	 */
	public void doApproval(Integer feedbackId, boolean isPass, String taskId, String comment) throws Exception;
	
	/**
	 * 完成任务
	 * @param feedbackId
	 * @param taskId
	 * @throws Exception
	 */
	public void doCompleteTask(Integer feedbackId,  String taskId) throws Exception;
	
	/**
	 * 根据附件ID删除某一个附件
	 * @param id 附件的ID
	 * @throws Exception
	 */
	public void doRemoveAttrByID(Integer id) throws Exception;
	
	
}
