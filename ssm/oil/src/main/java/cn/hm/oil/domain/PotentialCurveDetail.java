package cn.hm.oil.domain;

public class PotentialCurveDetail {
	private Integer id;
	private Integer pc_id;
	private String no;
	private Float p_early;
	private Float p_end;
	private String measurer;
	private String remark;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPc_id() {
		return pc_id;
	}
	public void setPc_id(Integer pc_id) {
		this.pc_id = pc_id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Float getP_early() {
		return p_early;
	}
	public void setP_early(Float p_early) {
		this.p_early = p_early;
	}
	public Float getP_end() {
		return p_end;
	}
	public void setP_end(Float p_end) {
		this.p_end = p_end;
	}
	public String getMeasurer() {
		return measurer;
	}
	public void setMeasurer(String measurer) {
		this.measurer = measurer;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
