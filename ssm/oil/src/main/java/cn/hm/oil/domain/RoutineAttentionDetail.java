package cn.hm.oil.domain;

public class RoutineAttentionDetail {
	private Integer id;
	private Integer ra_id;
	private String no;
	private String type;
	private String lczh;
	private String address;
	private String descrip;
	private String atten_date;
	private String content;
	private String person;
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

	public Integer getRa_id() {
		return ra_id;
	}

	public void setRa_id(Integer ra_id) {
		this.ra_id = ra_id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLczh() {
		return lczh;
	}

	public void setLczh(String lczh) {
		this.lczh = lczh;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public String getAtten_date() {
		return atten_date;
	}

	public void setAtten_date(String atten_date) {
		this.atten_date = atten_date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}
	
	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean v) {
		this.canEdit = v;
	}
	
	public void resetCanEidt(Integer role)
	{
		this.canEdit = role == 3 && this.no != null  && (this.status==-2 || this.status==-1);
	}
}
