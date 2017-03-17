/**
 * 
 */
package cn.hm.oil.domain;

import java.util.Date;

import org.jsoup.helper.StringUtil;

/**
 * @author Administrator
 * 
 */
public class PotentialMeasureDetail {
	private Integer id;
	private Integer pm_id;
	private String no;
	private Date m_date;
	private Float potential;
	private Float a;
	private Float v;
	private Float tongdian;
	private String measurer;
	private String remark;
	private Integer status;
	private Boolean canEdit=false;
	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the pm_id
	 */
	public Integer getPm_id() {
		return pm_id;
	}

	/**
	 * @param pm_id
	 *            the pm_id to set
	 */
	public void setPm_id(Integer pm_id) {
		this.pm_id = pm_id;
	}

	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}

	/**
	 * @param no
	 *            the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}

	/**
	 * @return the m_date
	 */
	public Date getM_date() {
		return m_date;
	}

	/**
	 * @param m_date
	 *            the m_date to set
	 */
	public void setM_date(Date m_date) {
		this.m_date = m_date;
	}

	/**
	 * @return the potential
	 */
	public Float getPotential() {
		return potential;
	}

	/**
	 * @param potential
	 *            the potential to set
	 */
	public void setPotential(Float potential) {
		this.potential = potential;
	}

	/**
	 * @return the a
	 */
	public Float getA() {
		return a;
	}

	/**
	 * @param a
	 *            the a to set
	 */
	public void setA(Float a) {
		this.a = a;
	}

	/**
	 * @return the v
	 */
	public Float getV() {
		return v;
	}

	/**
	 * @param v
	 *            the v to set
	 */
	public void setV(Float v) {
		this.v = v;
	}

	/**
	 * @return the tongdian
	 */
	public Float getTongdian() {
		return tongdian;
	}

	/**
	 * @param tongdian
	 *            the tongdian to set
	 */
	public void setTongdian(Float tongdian) {
		this.tongdian = tongdian;
	}

	/**
	 * @return the measurer
	 */
	public String getMeasurer() {
		return measurer;
	}

	/**
	 * @param measurer
	 *            the measurer to set
	 */
	public void setMeasurer(String measurer) {
		this.measurer = measurer;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean v) {
		this.canEdit = v;
	}
	
	public void resetCanEidt(Integer role)
	{
		this.canEdit = role == 3 && StringUtil.isBlank(this.no) == false && (this.status==-2 || this.status==-1);
	}
}
