package com.hdc.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.FeedbackAtt;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.TaskInfo;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.ITaskInfoService;
import com.hdc.util.Constants;
import com.hdc.util.upload.FileUploadUtils;
import com.hdc.util.upload.exception.InvalidExtensionException;
/**
 * 反馈控制器
 * @author zhao
 *
 */

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

	@Autowired
	private IFeedbackRecordService feedbackService;
	
	@Autowired
	private ITaskInfoService taskInfoService;
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList() {
		return "feedback/list_feedback";
	}
	
	/**
	 * 获取分页数据
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<FeedbackRecord> getList(Parameter param) throws Exception {
		Page<FeedbackRecord> page = new Page<FeedbackRecord>(param.getPage(), param.getRows());		
		this.feedbackService.getListPage(param, page);
		return new Datagrid<FeedbackRecord>(page.getTotal(),page.getResult());
	}
	
	/**
	 * 跳转添加或修改页面
	 * @param id
	 * @param taskInfoId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(
		     	@RequestParam(value = "action", required = false) String action,
				@RequestParam(value = "id", required = false) Integer id) throws Exception {
		ModelAndView mv = null;
		if("edit".equals(action)){
			mv = new ModelAndView("feedback/main_feedback");			
		}else if("check".equals(action)){
			mv = new ModelAndView("feedback/check_feedback");			
		}else if("detail".equals(action)){
			mv = new ModelAndView("feedback/details_feedback");			
		}else if("add".equals(action)){
			mv = new ModelAndView("feedback/main_feedback");
		}	
		if(id!=null){
			mv.addObject("feedback", this.feedbackService.findById(id));
		}		
		return mv;
	}
	
	/**
	 * 添加或修改
	 * @param feedback
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Message saveOrUpdate(
				FeedbackRecord feedback, 
				@RequestParam("file") MultipartFile[] file,
				HttpServletRequest request) throws Exception {
		Message message = new Message();
		Integer id = feedback.getId();
		try {
			if(file.length!=0){				
				Set<FeedbackAtt> fbaList=new HashSet<FeedbackAtt>();
				for(int i=0;i<file.length;i++){
					try {
						String filePath = FileUploadUtils.upload(request, file[i], Constants.FILE_PATH);
						FeedbackAtt fba=new FeedbackAtt();
						fba.setUrl(filePath);
						fba.setName(file[i].getOriginalFilename());;
						fba.setUploadDate(new Date());
						//子类把主类加一下，子类中才会有主类的ID外键；
						fba.setFdRecord(feedback);
						fbaList.add(fba);
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
				feedback.setFdaList(fbaList);
			}else{				
				//this.feedbackService.doCompleteTask(feedback, taskId, null, request);
			}
			if(id == null) {
				this.feedbackService.doAdd(feedback);
				message.setMessage("上传了"+file.length+"个材料，添加成功！");
			} else {
				this.feedbackService.doUpdate(feedback);
				message.setData(id);
				message.setMessage("修改成功！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("操作失败!");
			throw e;
		}
		
		return message;
	}
	  private String getFileMB(long byteFile){  
	        if(byteFile==0)  
	           return "0MB";  
	        long mb=1024*1024;  
	        return ""+byteFile/mb+"MB";  
	    } 
	    
	/**
	 * 查看taskInfoId下的所有反馈信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/details/{taskInfoId}")
	public ModelAndView detailsTab(@PathVariable("taskInfoId") Integer id) throws Exception {
		ModelAndView mv = new ModelAndView("feedback/list_feedback");
		List<FeedbackRecord> list = this.feedbackService.findByTaskId(id);
		mv.addObject("list", list);		
		return mv;
	} 
	
}
