package com.hdc.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hdc.entity.Comments;
import com.hdc.entity.FeedbackAtt;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.ProcessTask;
import com.hdc.entity.Project;
import com.hdc.entity.ProjectScore;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskSource;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.IProcessService;
import com.hdc.service.IProcessTaskService;
import com.hdc.service.IProjectScoreService;
import com.hdc.service.ITaskSourceService;
import com.hdc.util.BeanUtilsExt;
import com.hdc.util.Constants;
import com.hdc.util.Constants.BusinessForm;
import com.hdc.util.Constants.FeedbackStatus;
import com.hdc.util.UserUtil;
import com.hdc.util.upload.FileUploadUtils;
import com.hdc.util.upload.exception.InvalidExtensionException;

@Service
public class FeedbackRecordServiceImpl implements IFeedbackRecordService {

	@Autowired
	private IBaseService<FeedbackRecord> baseService;
	
	@Autowired
	private IProcessService processService;
	
	@Autowired
    private ITaskSourceService taskResourceService;
    
	@Autowired
	private IProcessTaskService processTaskService;
	
	@Autowired
	private IProjectScoreService projectScoreService;
	
	@Override
	public List<FeedbackRecord> getListPage(Parameter param,
			Page<FeedbackRecord> page) throws Exception {
		return this.baseService.findListPage("FeedbackRecord", param, null, page, true);
	}
	
	@Override
	public List<FeedbackRecord> getAllList() throws Exception {
		String hql = "from FeedbackRecord where isDelete = 0 order by createDate desc";
		return this.baseService.find(hql);
	}
	
	@Override
	public FeedbackRecord findById(Integer id) throws Exception {
		return this.baseService.getBean(FeedbackRecord.class, id);
	}

	@Override
	public List<FeedbackRecord> findByTaskId(Integer id) throws Exception {
		String hql = "from FeedbackRecord where taskInfo.id = " + id +" and isDelete = 0 order by createDate ASC";
		return this.baseService.find(hql);
	}
	
	@Override
	public List<FeedbackRecord> findByProjectId(Integer projectId)
			throws Exception {
		String hql = "from FeedbackRecord where project.id = " + projectId +" order by createDate ASC";
		return this.baseService.find(hql);
	}

	@Override
	public Serializable doAdd(FeedbackRecord feedback) throws Exception {
		return this.baseService.add(feedback);
	}
	
	//填写工作计划时的更新
	@Override
	public void doUpdate(FeedbackRecord feedback) throws Exception {
		this.baseService.update(feedback);
	}
	
	//反馈时使用的更新
	@Override
	public Message doUpdate(FeedbackRecord feedback,MultipartFile[] file,HttpServletRequest request) throws Exception {
		Message message=new Message();
		int count=0;//上传文件计数
		Integer id = feedback.getId();
		Set<FeedbackAtt> fbaList=new HashSet<FeedbackAtt>();
		FeedbackRecord fbr=this.findById(id);
		String path1=fbr.getProject().getGroup().getId().toString();
		String path2=fbr.getProject().getId().toString();
		if(path1==null || path2==null){
			message.setMessage("上传路径错误，上传失败");
			return message;
		}
		if(file!=null){							
			for(int i=0;i<file.length;i++){
				try {
					if(!file[i].isEmpty()){
						String filePath = FileUploadUtils.upload(request, file[i], 
								Constants.FILE_PATH
								        +File.separator+path1
										+File.separator+path2
										+File.separator+id);
						FeedbackAtt fba=new FeedbackAtt();
						String[] fileExName=file[i].getOriginalFilename().split("\\.");
						fba.setUrl(filePath);
						fba.setName(file[i].getOriginalFilename());;
						fba.setUploadDate(new Date());
						fba.setType(fileExName[fileExName.length-1]);
						//子类把主类加一下，子类中才会有主类的ID外键；
						fba.setFdRecord(feedback);
						fbaList.add(fba);
						count++;
					}	
				} catch (Exception e) {
					message.setStatus(Boolean.FALSE);
					message.setTitle("操作失败！");
					if(e instanceof FileSizeLimitExceededException){
						Long actual = ((FileSizeLimitExceededException) e).getActualSize();
						Long permitted = ((FileSizeLimitExceededException) e).getPermittedSize();
						message.setMessage("上传失败！文件大小超过限制，最大上传："+getFileMB(permitted)+",实际大小："+getFileMB(actual));
					} else if (e instanceof InvalidExtensionException){
						message.setMessage("不能上传此文件类型,请重新选择文件上传！");
					}
				}	
			}
		}else{				
			//this.feedbackService.doCompleteTask(feedback, taskId, null, request);
		}
		if(id == null) {
			message.setMessage("获取反馈对象失败");
		} else {			
		    fbr.setSolutions(feedback.getSituation());
		    fbr.setProblems(feedback.getProblems());
		    fbr.setSituation(feedback.getSituation());
		    fbr.setFdaList(fbaList);
		    fbr.setFeedbackDate(new Date());
		    fbr.setFeedbackUser(UserUtil.getUserFromSession());
		    this.baseService.update(fbr);
			
			//以下是为了查看是否延期反馈
			Date currentDate=new Date();
			Project prj=fbr.getProject();
			Date feedbackStartDate=fbr.getFeedbackEndDate();
			if(prj!=null){
				if(currentDate.after(feedbackStartDate)){
					ProjectScore projectScore1=new ProjectScore(prj,fbr.getId(),"超期未反馈",-50);
					ProjectScore projectScore2=new ProjectScore(prj,fbr.getId(),"超期未反馈，又反馈",+20);
					//将延迟次数+1
					Integer relayCount=fbr.getDelayCount();
					fbr.setRefuseCount(relayCount+1);
					this.baseService.update(fbr);
					
					this.projectScoreService.doAdd(projectScore1);
					this.projectScoreService.doAdd(projectScore2);
					message.setData(id);
					message.setMessage("上传了"+count+"个附件，反馈成功！ 但属逾期反馈，减30分");
				}else{
					message.setData(id);
					message.setMessage("上传了"+count+"个附件，反馈成功！");
				}		
			}					
		}		
		return message;		
	}
	private String getFileMB(long byteFile){  
		   if(byteFile==0)  
		       return "0MB";  
		   long mb=1024*1024;  
		   return ""+byteFile/mb+"MB";  
		} 

//	@Override
//	public void doUpdate(FeedbackRecord feedback) throws Exception {
//		String situation=feedback.getSituation();
//		String problem=feedback.getProblems();
//		String hql="Update FeedbackRecord fbr set "
//				+ "fbr.situation=:situation,"
//				+ "fbr.problem=:problem"
//				+ "fbr.solutions=:solutions"
//				+ "fbr.fdaList=:fdaList"
//				+ "where fbr.id=:id";
//		this.baseService.executeHql(hql, params);
//	}

	@Override
	public List<FeedbackRecord> findByDate(Date beginDate, Date endDate)
			throws Exception {
		String hql = "from FeedbackRecord where createDate between :begin and :end";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("begin", beginDate);
		params.put("end", endDate);
		return this.baseService.find(hql);
	}

	@Override
	public void doCompleteTask(FeedbackRecord feedback, String taskId,
			MultipartFile file, HttpServletRequest request) throws Exception {
		Integer id = feedback.getId();
		if(id == null) {
			if(!BeanUtilsExt.isBlank(file)) {
				/*String filePath = FileUploadUtils.upload(request, file, Constants.FILE_PATH);
				feedback.setFilePath(filePath);
				feedback.setFileName(file.getOriginalFilename());
				feedback.setUploadDate(new Date());*/
			}
			feedback.setStatus(FeedbackStatus.FEEDBACKING.toString());
			feedback.setIsDelay(0);		//是否迟报，得根据时间判断
			this.baseService.add(feedback);
			
		} else {
			this.baseService.update(feedback);
		}
		
		//如果有提示的代办任务，则完成任务。
		if(StringUtils.isNotBlank(taskId)) {
			this.processService.complete(taskId, null, null);
		}
		
	}
	
	@Override
	public void doDelete(Integer id) throws Exception {
		String hql = "update FeedbackRecord set isDelete = 1 where id = " + id.toString();
		this.baseService.executeHql(hql);
	}

	@Override
	public void doStartProcess(FeedbackRecord fb) throws Exception {
		FeedbackRecord feedback = this.findById(fb.getId());
		feedback.setStatus(FeedbackStatus.FEEDBACKING.toString());
		this.baseService.update(feedback);
		TaskInfo taskInfo=feedback.getProject().getTaskInfo();
		//给用户提示任务
		User user=UserUtil.getUserFromSession();
		ProcessTask processTask=new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setTitle("反馈已提交，等待审核！");
		processTask.setUrl("/feedback/toApproval?feedbackId="+feedback.getId().toString());
		TaskSource taskResource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskResource.getTaskInfoType().getName());		//任务类型
		processTask.setTaskInfoId(taskInfo.getId());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		
		//初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("createTaskUser", taskInfo.getCreateUser().getId().toString());
		vars.put("projectId", feedback.getProject().getId().toString());
		vars.put("processTaskId", processTaskId.toString());
		//启动审批流程
		this.processService.startApproval("ApprovalFeedback", taskInfo.getId().toString(), vars);	
				
	}
	
	@Override
	public void doApproval(Integer feedbackId, boolean isPass, String taskId, String comment) throws Exception {
		User user = UserUtil.getUserFromSession();
		Map<String, Object> variables = new HashMap<String, Object>();
		FeedbackRecord fbr = this.findById(feedbackId);
		if(isPass) {
			fbr.setStatus(FeedbackStatus.ACCEPT.toString()); //审批成功
		} else {
			fbr.setStatus(FeedbackStatus.RETURNED.toString()); //审批失败
			//以下是为了查看是否存在未反馈
			Project prj=fbr.getProject();//获取该反馈的project
			ProjectScore projectScore=new ProjectScore(prj,fbr.getId(),"反馈未予采用，返回承办单位一次",-10);
			this.projectScoreService.doAdd(projectScore);
			
			TaskInfo taskInfo=fbr.getProject().getTaskInfo();
			ProcessTask processTask = new ProcessTask();
			processTask.setTaskTitle(taskInfo.getTitle());
			processTask.setApplyUserId(user.getId());
			processTask.setApplyUserName(user.getName());
			processTask.setTaskInfoId(taskInfo.getId());
			processTask.setTaskInfoType(taskInfo.getTaskSource().getTaskInfoType().getName());
			processTask.setTitle("反馈已被退回，请修改后重新提交！");
			processTask.setUrl("/feedback/toMain?id="+fbr.getId().toString()+"&action=modify");
			Serializable processTaskId = this.processTaskService.doAdd(processTask);
			variables.put("processTaskId", processTaskId.toString());
		}
		this.doUpdate(fbr);
		
		// 评论,可记录每次审核意见
		Comments comments = new Comments();
		comments.setUserId(user.getId().toString());
		comments.setUserName(user.getName()); 
		comments.setContent(comment);
		comments.setBusinessKey(feedbackId);
		comments.setBusinessForm(BusinessForm.FEEDBACK_FORM.toString());
	    variables.put("isPass", isPass);
		this.processService.complete(taskId, comments, variables);
	}

	@Override
	public void doCompleteTask(Integer feedbackId, String taskId) throws Exception {
		//给秘书长提示代办任务
		FeedbackRecord feedback = this.findById(feedbackId);
		feedback.setStatus(FeedbackStatus.FEEDBACKING.toString());
		this.baseService.update(feedback);
		TaskInfo taskInfo=feedback.getProject().getTaskInfo();
		Map<String, Object> variables = new HashMap<String, Object>();		
		User user = UserUtil.getUserFromSession();
		ProcessTask processTask = new ProcessTask();
		processTask.setTaskTitle(taskInfo.getTitle());
		processTask.setApplyUserId(user.getId());
		processTask.setApplyUserName(user.getName());
		processTask.setTaskInfoId(taskInfo.getId());
		TaskSource taskSource = this.taskResourceService.findById(taskInfo.getTaskSource().getId());
		processTask.setTaskInfoType(taskSource.getTaskInfoType().getName());
		processTask.setTitle("反馈修改完成！需要重新审批!");
		processTask.setUrl("/feedback/toApproval?feedbackId="+feedback.getId().toString());
		Serializable processTaskId = this.processTaskService.doAdd(processTask);
		//初始化任务参数
		variables.put("processTaskId", processTaskId.toString());
		this.processService.complete(taskId, null, variables);
		
	}

	@Override
	public List<FeedbackRecord> findNoAccept(String projectId) throws Exception {
		String hql = "from FeedbackRecord where status <> 'ACCEPT' and isDelete = 0 and project.id = " + projectId.toString();
		return this.baseService.find(hql);
	}
	
	@Override
	public void doRemoveAttrByID(Integer id) throws Exception {
		String hql="delete from FeedbackAtt where id="+id.toString();
		this.baseService.executeHql(hql);
	}

}
