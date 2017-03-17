package cn.hm.oil.domain;

public class RunRecordDetail {
	private Integer id;
	private Integer r_id;
	private Integer day;
	private Integer time;
	private Float jldy;
	private Float zlsc_a;
	private Float zlsc_v;
	private Float tdddw;
	private String recorder;
	private String comment;
	private String others;
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
	public Integer getR_id() {
		return r_id;
	}
	public void setR_id(Integer r_id) {
		this.r_id = r_id;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Float getJldy() {
		return jldy;
	}
	public void setJldy(Float jldy) {
		this.jldy = jldy;
	}
	public Float getZlsc_a() {
		return zlsc_a;
	}
	public void setZlsc_a(Float zlsc_a) {
		this.zlsc_a = zlsc_a;
	}
	public Float getZlsc_v() {
		return zlsc_v;
	}
	public void setZlsc_v(Float zlsc_v) {
		this.zlsc_v = zlsc_v;
	}
	public Float getTdddw() {
		return tdddw;
	}
	public void setTdddw(Float tdddw) {
		this.tdddw = tdddw;
	}
	public String getRecorder() {
		return recorder;
	}
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	
	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean v) {
		this.canEdit = v;
	}
	
	public void resetCanEidt(Integer role)
	{
		this.canEdit = role == 3 && this.day != null && this.time != null && (this.status==-2 || this.status==-1);
	}
}
