package cn.hm.oil.domain;

public class DynamicConsequenceDetailAttachement {
	
	private Integer id;
	private Integer dc_id;
	private Integer dcd_id;
	private Integer month;
	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDcd_id() {
		return dcd_id;
	}
	public void setDcd_id(Integer dcd_id) {
		this.dcd_id = dcd_id;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getDc_id() {
		return dc_id;
	}
	public void setDc_id(Integer dc_id) {
		this.dc_id = dc_id;
	}
	
}
