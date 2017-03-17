package cn.hm.oil.domain;

import org.jsoup.helper.StringUtil;

public class PerformanceMeasureDetail {
	private Integer id;
	private Integer pm_id;
	private Integer type;
	private String position;
	private String month_1;
	private String month_2;
	private String month_3;
	private String month_4;
	private String month_5;
	private String month_6;
	private String month_7;
	private String month_8;
	private String month_9;
	private String month_10;
	private String month_11;
	private String month_12;
	private Integer status;
	private Boolean canEdit=false;
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	private String typeName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPm_id() {
		return pm_id;
	}

	public void setPm_id(Integer pm_id) {
		this.pm_id = pm_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMonth_1() {
		return month_1;
	}

	public void setMonth_1(String month_1) {
		this.month_1 = month_1;
	}

	public String getMonth_2() {
		return month_2;
	}

	public void setMonth_2(String month_2) {
		this.month_2 = month_2;
	}

	public String getMonth_3() {
		return month_3;
	}

	public void setMonth_3(String month_3) {
		this.month_3 = month_3;
	}

	public String getMonth_4() {
		return month_4;
	}

	public void setMonth_4(String month_4) {
		this.month_4 = month_4;
	}

	public String getMonth_5() {
		return month_5;
	}

	public void setMonth_5(String month_5) {
		this.month_5 = month_5;
	}

	public String getMonth_6() {
		return month_6;
	}

	public void setMonth_6(String month_6) {
		this.month_6 = month_6;
	}

	public String getMonth_7() {
		return month_7;
	}

	public void setMonth_7(String month_7) {
		this.month_7 = month_7;
	}

	public String getMonth_8() {
		return month_8;
	}

	public void setMonth_8(String month_8) {
		this.month_8 = month_8;
	}

	public String getMonth_9() {
		return month_9;
	}

	public void setMonth_9(String month_9) {
		this.month_9 = month_9;
	}

	public String getMonth_10() {
		return month_10;
	}

	public void setMonth_10(String month_10) {
		this.month_10 = month_10;
	}

	public String getMonth_11() {
		return month_11;
	}

	public void setMonth_11(String month_11) {
		this.month_11 = month_11;
	}

	public String getMonth_12() {
		return month_12;
	}

	public void setMonth_12(String month_12) {
		this.month_12 = month_12;
	}
	
	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean v) {
		this.canEdit = v;
	}
	
	public void resetCanEidt(Integer role)
	{
		this.canEdit = role == 3 && StringUtil.isBlank(this.position) == false && (this.status==-2 || this.status==-1);
	}
}
