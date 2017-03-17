package cn.hm.oil.domain;

import java.util.Date;
import java.util.List;

public class PotentialCurve_2016 {
	private Integer id;//主键
	private Integer pl_id;//所属管线ID
	private String up_id;//	
	private String unit;//单位名称
	private Integer c_month;//测量年月
	private Date create_time;//创建时间
	private Integer status;//数据记录状态：1、通过；0、未审核；-1、未通过 ,-2草稿；
	private Integer creater;//数据创建人
	private String verify_desc;//数据审核说明
	private Integer verifier;//数据审核人
	private Date verify_time;//数据审核日期
	private String create_t;
	private String verify_t;
	private String chartPath;
	private String analysis;
	private String name;
	private String pl_name;
	private Integer pl_measure_id;
	private Integer red;
	private List<PotentialCurveDetail> detailList;
	
	private Boolean canEdit=false;
	
	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean v) {
		this.canEdit = v;
	}
	
	public void resetCanEidt(Integer role)
	{
		this.canEdit = role == 3 && this.id != null && (this.status==-2 || this.status==-1);
	}
	
	public Integer getPl_measure_id() {
		return pl_measure_id;
	}

	public void setPl_measure_id(Integer id) {
		this.pl_measure_id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getRed() {
		return red;
	}

	public void setRed(Integer red) {
		this.red = red;
	}

	public Integer getPl_id() {
		return pl_id;
	}

	public void setPl_id(Integer pl_id) {
		this.pl_id = pl_id;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getC_month() {
		return c_month;
	}

	public void setC_month(Integer c_month) {
		this.c_month = c_month;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPl_name() {
		return pl_name;
	}

	public void setPl_name(String name) {
		this.pl_name = name;
	}

	public String getChartPath() {
		return chartPath;
	}

	public void setChartPath(String chartPath) {
		this.chartPath = chartPath;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public List<PotentialCurveDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PotentialCurveDetail> detailList) {
		this.detailList = detailList;
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

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}
}
