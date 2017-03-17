package cn.hm.oil.domain;

public class ValveMaintDetail {
	private Integer id;
	private Integer vm_id;
	private String up_id;
	private String particips;
	private String content;
	private String question;
	private Integer status;
	private Boolean canEdit=false;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVm_id() {
		return vm_id;
	}

	public void setVm_id(Integer vm_id) {
		this.vm_id = vm_id;
	}

	public String getParticips() {
		return particips;
	}

	public void setParticips(String particips) {
		this.particips = particips;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getUp_id() {
		return up_id;
	}

	public void setUp_id(String up_id) {
		this.up_id = up_id;
	}
}
