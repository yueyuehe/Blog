package cn.hm.oil.domain;

import org.jsoup.helper.StringUtil;

public class RunRecordcomprehensiveDetail {
	private Integer id;
	private Integer c_id;
	private String estimated;
	private String actual;
	private String wdtd;
	private String jztd;
	private String ljtd;
	private String jcgxtd;
	private String fljctd;
	private String qttd;
	private String o_max_a;
	private String o_min_a;
	private String o_avg_a;
	private String o_max_v;
	private String o_min_v;
	private String o_avg_v;
	private String tdd_v_max;
	private String tdd_v_min;
	private String sdl;
	private String bhl;
	private String sbwhl;
	private String comment;
	private Integer status;
	private Boolean canEdit=false;
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getC_id() {
		return c_id;
	}
	public void setC_id(Integer c_id) {
		this.c_id = c_id;
	}
	public String getEstimated() {
		return estimated;
	}
	public void setEstimated(String estimated) {
		this.estimated = estimated;
	}
	public String getActual() {
		return actual;
	}
	public void setActual(String actual) {
		this.actual = actual;
	}
	public String getWdtd() {
		return wdtd;
	}
	public void setWdtd(String wdtd) {
		this.wdtd = wdtd;
	}
	public String getJztd() {
		return jztd;
	}
	public void setJztd(String jztd) {
		this.jztd = jztd;
	}
	public String getLjtd() {
		return ljtd;
	}
	public void setLjtd(String ljtd) {
		this.ljtd = ljtd;
	}
	public String getJcgxtd() {
		return jcgxtd;
	}
	public void setJcgxtd(String jcgxtd) {
		this.jcgxtd = jcgxtd;
	}
	public String getFljctd() {
		return fljctd;
	}
	public void setFljctd(String fljctd) {
		this.fljctd = fljctd;
	}
	public String getQttd() {
		return qttd;
	}
	public void setQttd(String qttd) {
		this.qttd = qttd;
	}
	public String getO_max_a() {
		return o_max_a;
	}
	public void setO_max_a(String o_max_a) {
		this.o_max_a = o_max_a;
	}
	public String getO_min_a() {
		return o_min_a;
	}
	public void setO_min_a(String o_min_a) {
		this.o_min_a = o_min_a;
	}
	public String getO_avg_a() {
		return o_avg_a;
	}
	public void setO_avg_a(String o_avg_a) {
		this.o_avg_a = o_avg_a;
	}
	public String getO_max_v() {
		return o_max_v;
	}
	public void setO_max_v(String o_max_v) {
		this.o_max_v = o_max_v;
	}
	public String getO_min_v() {
		return o_min_v;
	}
	public void setO_min_v(String o_min_v) {
		this.o_min_v = o_min_v;
	}
	public String getO_avg_v() {
		return o_avg_v;
	}
	public void setO_avg_v(String o_avg_v) {
		this.o_avg_v = o_avg_v;
	}
	public String getTdd_v_max() {
		return tdd_v_max;
	}
	public void setTdd_v_max(String tdd_v_max) {
		this.tdd_v_max = tdd_v_max;
	}
	public String getTdd_v_min() {
		return tdd_v_min;
	}
	public void setTdd_v_min(String tdd_v_min) {
		this.tdd_v_min = tdd_v_min;
	}
	public String getSdl() {
		return sdl;
	}
	public void setSdl(String sdl) {
		this.sdl = sdl;
	}
	public String getBhl() {
		return bhl;
	}
	public void setBhl(String bhl) {
		this.bhl = bhl;
	}
	public String getSbwhl() {
		return sbwhl;
	}
	public void setSbwhl(String sbwhl) {
		this.sbwhl = sbwhl;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void setCanEdit(Boolean v) {
		this.canEdit = v;
	}
	
	public Boolean getCanEdit() {
		return this.canEdit;
	}
	
	public void resetCanEidt(Integer role)
	{
		this.canEdit = role == 3 && c_id != null && (this.status==-2 || this.status==-1);
	}
}
