/**
 * 
 */
package cn.hm.oil.domain;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class PotentialMeasure {
	private Integer id;
	private Integer pl_id;
	private String up_id;
	private Integer pl_spec_id;
	private Integer pl_section_id;
	private String jinzhan;
	private String create_t;
	private String verify_t;
	private Integer c_month;// 年月
	private String unit;
	private String save_date;
	private Date create_time;
	private Integer creater;
	private Integer status;
	private Integer verifier;
	private Date verify_time;
	private String verify_desc;

	private String verify_name;

	private String pl_name;
	private String pl_section_name;
	private String pl_spec_name;

	private List<PotentialMeasureDetail> detailList;

	/**
	 * @return the creater
	 */
	public Integer getCreater() {
		return creater;
	}

	/**
	 * @param creater
	 *            the creater to set
	 */
	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the verifier
	 */
	public Integer getVerifier() {
		return verifier;
	}

	/**
	 * @param verifier
	 *            the verifier to set
	 */
	public void setVerifier(Integer verifier) {
		this.verifier = verifier;
	}

	/**
	 * @return the verify_time
	 */
	public Date getVerify_time() {
		return verify_time;
	}

	/**
	 * @param verify_time
	 *            the verify_time to set
	 */
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}

	/**
	 * @return the verify_desc
	 */
	public String getVerify_desc() {
		return verify_desc;
	}

	/**
	 * @param verify_desc
	 *            the verify_desc to set
	 */
	public void setVerify_desc(String verify_desc) {
		this.verify_desc = verify_desc;
	}

	/**
	 * @return the detailList
	 */
	public List<PotentialMeasureDetail> getDetailList() {
		return detailList;
	}

	/**
	 * @param detailList
	 *            the detailList to set
	 */
	public void setDetailList(List<PotentialMeasureDetail> detailList) {
		this.detailList = detailList;
	}

	/**
	 * @return the pl_name
	 */
	public String getPl_name() {
		return pl_name;
	}

	/**
	 * @param pl_name
	 *            the pl_name to set
	 */
	public void setPl_name(String pl_name) {
		this.pl_name = pl_name;
	}

	/**
	 * @return the pl_section_name
	 */
	public String getPl_section_name() {
		return pl_section_name;
	}

	/**
	 * @param pl_section_name
	 *            the pl_section_name to set
	 */
	public void setPl_section_name(String pl_section_name) {
		this.pl_section_name = pl_section_name;
	}

	/**
	 * @return the pl_spec_name
	 */
	public String getPl_spec_name() {
		return pl_spec_name;
	}

	/**
	 * @param pl_spec_name
	 *            the pl_spec_name to set
	 */
	public void setPl_spec_name(String pl_spec_name) {
		this.pl_spec_name = pl_spec_name;
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
	 * @return the pl_id
	 */
	public Integer getPl_id() {
		return pl_id;
	}

	/**
	 * @param pl_id
	 *            the pl_id to set
	 */
	public void setPl_id(Integer pl_id) {
		this.pl_id = pl_id;
	}

	/**
	 * @return the pl_spec_id
	 */
	public Integer getPl_spec_id() {
		return pl_spec_id;
	}

	/**
	 * @param pl_spec_id
	 *            the pl_spec_id to set
	 */
	public void setPl_spec_id(Integer pl_spec_id) {
		this.pl_spec_id = pl_spec_id;
	}

	/**
	 * @return the pl_section_id
	 */
	public Integer getPl_section_id() {
		return pl_section_id;
	}

	/**
	 * @param pl_section_id
	 *            the pl_section_id to set
	 */
	public void setPl_section_id(Integer pl_section_id) {
		this.pl_section_id = pl_section_id;
	}

	/**
	 * @return the jinzhan
	 */
	public String getJinzhan() {
		return jinzhan;
	}

	/**
	 * @param jinzhan
	 *            the jinzhan to set
	 */
	public void setJinzhan(String jinzhan) {
		this.jinzhan = jinzhan;
	}

	/**
	 * @return the c_month
	 */
	public Integer getC_month() {
		return c_month;
	}

	/**
	 * @param c_month
	 *            the c_month to set
	 */
	public void setC_month(Integer c_month) {
		this.c_month = c_month;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the save_date
	 */
	public String getSave_date() {
		return save_date;
	}

	/**
	 * @param save_date
	 *            the save_date to set
	 */
	public void setSave_date(String save_date) {
		this.save_date = save_date;
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getVerify_name() {
		return verify_name;
	}

	public void setVerify_name(String verify_name) {
		this.verify_name = verify_name;
	}

	public String getUp_id() {
		return up_id;
	}

	public void setUp_id(String up_id) {
		this.up_id = up_id;
	}

	public String getVerify_t() {
		return verify_t;
	}

	public void setVerify_t(String verify_t) {
		this.verify_t = verify_t;
	}

	public String getCreate_t() {
		return create_t;
	}

	public void setCreate_t(String create_t) {
		this.create_t = create_t;
	}

}
