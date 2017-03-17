package cn.hm.oil.domain;

import java.util.Date;
import java.util.List;

public class PipelinePatrol_2016 {
	private Integer id;
	private Integer pl_id;
	private String up_id;
	private String jinzhan;
	private String create_t;
	private String verify_t;
	private Integer p_month;
	private String save_jinzhan;
	private Date create_time;
	private Integer creater;
	private Integer status;
	private Integer vreater;
	private Integer verifier;
	private Date verify_time;
	private String verify_desc;

	private String patroler;
	private String patrol_no;

	private String pl_name;
	private String name;

	private List<PipelinePatrolDetail> detailList;

	private Boolean canEdit=false;
	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean v) {
		this.canEdit = v;
	}
	
	public void resetCanEidt(Integer role)
	{
		this.canEdit = role == 3 && (this.status==-2 || this.status==-1);
	}
	
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	/**
	 * @return the detailList
	 */
	public List<PipelinePatrolDetail> getDetailList() {
		return detailList;
	}

	/**
	 * @param detailList
	 *            the detailList to set
	 */
	public void setDetailList(List<PipelinePatrolDetail> detailList) {
		this.detailList = detailList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPl_id() {
		return pl_id;
	}

	public void setPl_id(Integer pl_id) {
		this.pl_id = pl_id;
	}
	
	public String getJinzhan() {
		return jinzhan;
	}

	public void setJinzhan(String jinzhan) {
		this.jinzhan = jinzhan;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Integer getP_month() {
		return p_month;
	}

	public void setP_month(Integer p_month) {
		this.p_month = p_month;
	}

	public String getSave_jinzhan() {
		return save_jinzhan;
	}

	public void setSave_jinzhan(String save_jinzhan) {
		this.save_jinzhan = save_jinzhan;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getVreater() {
		return vreater;
	}

	public void setVreater(Integer vreater) {
		this.vreater = vreater;
	}

	public Integer getVerifier() {
		return verifier;
	}

	public void setVerifier(Integer verifier) {
		this.verifier = verifier;
	}

	public Date getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}

	public String getVerify_desc() {
		return verify_desc;
	}

	public void setVerify_desc(String verify_desc) {
		this.verify_desc = verify_desc;
	}

	public String getPl_name() {
		return pl_name;
	}

	public void setPl_name(String pl_name) {
		this.pl_name = pl_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatroler() {
		return patroler;
	}

	public void setPatroler(String patroler) {
		this.patroler = patroler;
	}

	public String getPatrol_no() {
		return patrol_no;
	}

	public void setPatrol_no(String patrol_no) {
		this.patrol_no = patrol_no;
	}

	public String getUp_id() {
		return up_id;
	}

	public void setUp_id(String up_id) {
		this.up_id = up_id;
	}

	public String getVerify_t() {
		return verify_t;
	}

	public void setVerify_t(String verify_t) {
		this.verify_t = verify_t;
	}

	public String getCreate_t() {
		return create_t;
	}

	public void setCreate_t(String create_t) {
		this.create_t = create_t;
	}
}
