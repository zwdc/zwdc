package com.hdc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hdc.entity.Datagrid;
import com.hdc.entity.Message;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.entity.Project;
import com.hdc.service.IProjectService;

/**
 * 任务交办管理器
 * @author ZML
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private IProjectService projectService;
	
	
	/**
	 * 根据标识跳转到不同页面
	 * @param type
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList(@RequestParam("type") Integer type) {
		if(type == 1) {
			//待签收
			return "project/list_c_project";
		} else if(type == 2) {
			//办理中
			return "project/list_h_project";
		} else if(type == 3) {
			//申请办结
			return "project/list_ae_project";
		} else if(type == 4) {
			//已办结
			return "project/list_e_project";
		}
		return null;
	}
	
	/**
	 * 跳转签收页面
	 * @param taskInfoId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toClaim")
	public ModelAndView toClaim(@RequestParam("projectId") Integer projectId) throws Exception {
		ModelAndView mv = new ModelAndView("project/claim_project");
		Project project = this.projectService.findById(projectId);
		if(project != null) {
			//TaskInfo taskInfo = this.taskInfoService.findById(project.getTaskInfo().getId());
			mv.addObject("taskInfo", project.getTaskInfo());
			mv.addObject("projectId", projectId);
			mv.addObject("suggestion", project.getSuggestion());
		}
		return mv;
	}
	
	/**
	 * 更新操作
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Message update(@RequestParam("projectId") String projectId, @RequestParam("suggestion") String suggestion) throws Exception {
		Message message = new Message();
		try {
			this.projectService.doUpdateById(projectId, suggestion);
			message.setMessage("更新成功！");
			message.setData(projectId);
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("更新失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 根据type获取不同数据
	 * @param param
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<Object> getList(Parameter param, @RequestParam("type") Integer type) throws Exception {
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(param.getPage(), param.getRows());
		List<Object> jsonList=new ArrayList<Object>(); 
		/*User user = UserUtil.getUserFromSession();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("group.id", user.getGroup().getId());
		map.put("user.id", "is null");
		List<Project> list = this.projectService.getListPage(param, page, map);
		for(Project project : list) {
			Map<String, Object> m=new HashMap<String, Object>();
			TaskInfo taskInfo = project.getTaskInfo();
			m.put("id", project.getId());
			m.put("taskInfoId", taskInfo.getId());
			m.put("taskTitle", taskInfo.getTitle());
			m.put("urgency", taskInfo.getUrgency());
			m.put("sourceName", taskInfo.getTaskSource().getName());
			m.put("hostGroup", project.getGroup().getName());
			if(project.getUser() != null) {
				m.put("hostUser", project.getUser().getName());
			}
			m.put("endTaskDate", taskInfo.getEndTaskDate());
			m.put("fbFrequencyName", taskInfo.getFbFrequency().getName());
			jsonList.add(m);
		}*/
		
		List<Map<String, Object>> list = this.projectService.getProjectList(param, type, page);
		for(Map<String, Object> map : list) {
			Map<String, Object> m=new HashMap<String, Object>();
			m.put("id", map.get("ID"));
			m.put("taskInfoId", map.get("TASK_ID"));
			m.put("taskTitle", map.get("TITLE"));
			m.put("urgency", map.get("URGENCY"));
			m.put("sourceName", map.get("SOURCE_NAME"));
			m.put("hostGroup", map.get("GROUP_NAME"));
			m.put("hostUser", map.get("USER_NAME"));
			m.put("endTaskDate", map.get("END_TASK_DATE"));
			m.put("fbFrequencyName", map.get("FREQUENCY_NAME"));
			jsonList.add(m);
		}
		return new Datagrid<Object>(page.getTotal(), jsonList);
	}
	
	/**
	 * 签收任务交办表
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/claimProject/{projectId}")
	@ResponseBody
	public Message claimProject(@PathVariable("projectId") String projectId) throws Exception {
		Message message = new Message();
		try {
			if(StringUtils.isNotBlank(projectId)) {
				Boolean flag = this.projectService.doClaimProject(projectId);
				if(flag) {
					message.setMessage("签收成功！");
				} else {
					message.setMessage("该任务已被其他人签收！");
				}
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("签收失败！");
			throw e;
		}
		return message;
	}
	
}
