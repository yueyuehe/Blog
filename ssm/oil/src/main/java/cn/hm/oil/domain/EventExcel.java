package cn.hm.oil.domain;

import java.util.Date;

public class EventExcel {

	private Integer id;
	private Integer e_id;
	private Integer aType;
	private String path;
	private Date create_time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getE_id() {
		return e_id;
	}
	public void setE_id(Integer e_id) {
		this.e_id = e_id;
	}
	public Integer getaType() {
		return aType;
	}
	public void setaType(Integer aType) {
		this.aType = aType;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
