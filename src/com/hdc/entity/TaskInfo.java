package com.hdc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 事项
 * @author ZML
 *
 */
@Entity
@Table(name = "TASK_INFO")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TaskInfo extends BaseCommonEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1621968964712893873L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", length = 10, nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "TITLE", length = 200)
	private String title;			//标题
	
	@Column(name = "TASK_NO", length = 200)
	private String taskNo;			//文号
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATE_TASK_DATE")
	private Date createTaskDate;	//立项日期
	
	@Column(name = "FEEDBACK_CYCLE", length = 1)
	private Integer feedback_cycle;	//反馈周期
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "END_TASK_DATE")
	private Date endTaskDate;		//办结时限
	
	@Column(name = "CONTACTS", length = 30)
	private String contacts;		//联系人
	
	@Column(name = "CONTACTS_PHONE", length = 30)
	private String contactsPhone;	//联系电话
	
	@Column(name = "HOST_UNIT", length = 100)
	private String hostUnit;		//主办单位
	
	@Column(name = "ASSISTANT_UNIT", length = 100)
	private String assistantUnit;	//协办单位
	
	@Column(name = "TASK_CONTENT", length = 2000)
	private String taskContent;		//事项内容
	
	@Column(name = "LEAD_COMMENTS", length = 2000)
	private String leadComments;	//领导批示
	
	@Column(name = "SUGGESTION", length = 2000)
	private String suggestion;		//拟办意见
	
	@Column(name = "FILE_NAME", length = 500)
	private String fileName;		//文件名称
	
	@Column(name = "FILE_PATH", length = 1000)
	private String filePath;		//文件路径
	
	@Column(name = "SUPERVISOR", length = 5)
	private Integer supervisor;		//督办专员 id
	
	@Column(name = "STATUS", length = 30)
	private String status;			//状态(0.待签收  1.办理中  2.已办结)

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public Date getCreateTaskDate() {
		return createTaskDate;
	}

	public void setCreateTaskDate(Date createTaskDate) {
		this.createTaskDate = createTaskDate;
	}

	public Integer getFeedback_cycle() {
		return feedback_cycle;
	}

	public void setFeedback_cycle(Integer feedback_cycle) {
		this.feedback_cycle = feedback_cycle;
	}

	public Date getEndTaskDate() {
		return endTaskDate;
	}

	public void setEndTaskDate(Date endTaskDate) {
		this.endTaskDate = endTaskDate;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public String getHostUnit() {
		return hostUnit;
	}

	public void setHostUnit(String hostUnit) {
		this.hostUnit = hostUnit;
	}

	public String getAssistantUnit() {
		return assistantUnit;
	}

	public void setAssistantUnit(String assistantUnit) {
		this.assistantUnit = assistantUnit;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

	public String getLeadComments() {
		return leadComments;
	}

	public void setLeadComments(String leadComments) {
		this.leadComments = leadComments;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Integer supervisor) {
		this.supervisor = supervisor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
