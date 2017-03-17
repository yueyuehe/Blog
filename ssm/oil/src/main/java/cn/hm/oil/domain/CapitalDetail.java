package cn.hm.oil.domain;

import java.util.Date;

public class CapitalDetail {
	private Integer id;
	private Integer ca_id;
	private Integer no;
	private String content;
	private Date plan_time;
	private Date workable_time;
	private String workspace;
	private String contacts;
	private String telnum;
	private String annex;
	private String remark;
	private Integer operator;
	private Date operate_time;
	private Integer operate_num;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCa_id() {
		return ca_id;
	}

	public void setCa_id(Integer ca_id) {
		this.ca_id = ca_id;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPlan_time() {
		return plan_time;
	}

	public void setPlan_time(Date plan_time) {
		this.plan_time = plan_time;
	}

	public Date getWorkable_time() {
		return workable_time;
	}

	public void setWorkable_time(Date workable_time) {
		this.workable_time = workable_time;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public String getAnnex() {
		return annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public Date getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(Date operate_time) {
		this.operate_time = operate_time;
	}

	public Integer getOperate_num() {
		return operate_num;
	}

	public void setOperate_num(Integer operate_num) {
		this.operate_num = operate_num;
	}

}
