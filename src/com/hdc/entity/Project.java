package com.hdc.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OrderBy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hdc.util.Constants.ProjectStatus;

/**
 * 项目表(具体到哪个单位)
 * @author zhao
 *
 */

@Entity
@Table(name = "PROJECT")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Project extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6435964346901408218L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10, nullable = false, unique = true)
	private Integer id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	@JsonIgnore
	private User user;				//承办人(签收用)  签收人
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="role_id")
	@JsonIgnore
	private Role role;				//承办人角色(签收用)


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="group_id")
	@JsonIgnore
	private Group group;				//承办单位(签收用)
	
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="refuse_user_id")
	@JsonIgnore
	private User refuseUser;		//拒签收用户
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="task_info_id")
	@JsonIgnore
	private TaskInfo taskInfo;		//任务
	
	@OneToMany(mappedBy = "project",fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	@JsonIgnore
	@OrderBy(clause="id ASC")
	private Set<FeedbackRecord> fbrList=new HashSet<FeedbackRecord>(); //反馈表
	
	public Set<FeedbackRecord> getFbrList() {
		return fbrList;
	}

	public void setFbrList(Set<FeedbackRecord> fbrList) {
		this.fbrList = fbrList;
	}

	/**
	 * @see ProjectStatus
	 */
	@Column(name = "status", length = 30)
	private String status;			//项目状态
	
	@Column(name = "suggestion", length = 4000)
	private String suggestion;		//拟办意见
	
	@Column(name="score", precision = 5, scale = 2)
	private Double score;			//项目总分
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "start_date")
	private Date startDate;			//项目开始时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "end_date")
	private Date endDate;			//项目办结时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "claim_date")
	private Date claimDate;			//项目签收时间
	
	@Column(name = "refuse_reason", length = 2000)
	private String refuseReason;	//拒签原因

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public TaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(Date claimDate) {
		this.claimDate = claimDate;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getRefuseUser() {
		return refuseUser;
	}

	public void setRefuseUser(User refuseUser) {
		this.refuseUser = refuseUser;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
}
