package cn.hm.oil.domain;

import java.util.Date;
import java.util.List;

public class RoutineAttention {
	private Integer id;
	private Integer pl_id;
	private String up_id;
	private Integer pl_section_id;
	private Integer pl_spec_id;
	private String jinzhan;
	private String weihu;
	private String create_t;
	private String verify_t;
	private Integer year;
	private Date create_time;
	private Integer creater;
	private Integer status;
	private Integer verifier;
	private Date verify_time;
	private String verify_desc;
	private String pl_name;
	private String pl_section_name;
	private String pl_spec_name;
	
	private String comment;
	
	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private List<RoutineAttentionDetail> detaillist;

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

	public Integer getPl_section_id() {
		return pl_section_id;
	}

	public void setPl_section_id(Integer pl_section_id) {
		this.pl_section_id = pl_section_id;
	}

	public Integer getPl_spec_id() {
		return pl_spec_id;
	}

	public void setPl_spec_id(Integer pl_spec_id) {
		this.pl_spec_id = pl_spec_id;
	}

	public String getJinzhan() {
		return jinzhan;
	}

	public void setJinzhan(String jinzhan) {
		this.jinzhan = jinzhan;
	}

	public String getWeihu() {
		return weihu;
	}

	public void setWeihu(String weihu) {
		this.weihu = weihu;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public List<RoutineAttentionDetail> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<RoutineAttentionDetail> detaillist) {
		this.detaillist = detaillist;
	}

	public String getPl_section_name() {
		return pl_section_name;
	}

	public void setPl_section_name(String pl_section_name) {
		this.pl_section_name = pl_section_name;
	}

	public String getPl_spec_name() {
		return pl_spec_name;
	}

	public void setPl_spec_name(String pl_spec_name) {
		this.pl_spec_name = pl_spec_name;
	}

	public String getUp_id() {
		return up_id;
	}

	public void setUp_id(String up_id) {
		this.up_id = up_id;
	}

	public String getCreate_t() {
		return create_t;
	}

	public void setCreate_t(String create_t) {
		this.create_t = create_t;
	}

	public String getVerify_t() {
		return verify_t;
	}

	public void setVerify_t(String verify_t) {
		this.verify_t = verify_t;
	}

}
