package cn.hm.oil.domain;

import java.util.Date;
import java.util.List;

public class Locality {
	private Integer id;
	private String project_name;
	private String survey;
	private String progress;
	private String leader;
	private Integer creater;
	private Date create_time;
	private Integer status;
	private Date verify_time;
	private Integer verifier;
	private String verify_desc;
	private Integer operate_num;
	private Integer finished_num;
	
	private List<LocalityDetail> detailList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getSurvey() {
		return survey;
	}

	public void setSurvey(String survey) {
		this.survey = survey;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}

	public Integer getVerifier() {
		return verifier;
	}

	public void setVerifier(Integer verifier) {
		this.verifier = verifier;
	}

	public String getVerify_desc() {
		return verify_desc;
	}

	public void setVerify_desc(String verify_desc) {
		this.verify_desc = verify_desc;
	}

	public Integer getOperate_num() {
		return operate_num;
	}

	public void setOperate_num(Integer operate_num) {
		this.operate_num = operate_num;
	}

	public Integer getFinished_num() {
		return finished_num;
	}

	public void setFinished_num(Integer finished_num) {
		this.finished_num = finished_num;
	}

	public List<LocalityDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<LocalityDetail> detailList) {
		this.detailList = detailList;
	}
}
