/**
 * 
 */
package cn.hm.oil.domain;

/**
 * @author Administrator
 * 
 */
public class BaseResource {
	private Integer id;
	private Integer pl_id;
	private Integer pl_spec_id;
	private Integer pl_section_id;
	private Float mpa;
	private Float throughout;
	private Float length;
	private String chamber_well;
	private String fangfu_station;
	private Integer admin;
	private Integer disable;

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
	 * @return the mpa
	 */
	public Float getMpa() {
		return mpa;
	}

	/**
	 * @param mpa
	 *            the mpa to set
	 */
	public void setMpa(Float mpa) {
		this.mpa = mpa;
	}

	/**
	 * @return the throughout
	 */
	public Float getThroughout() {
		return throughout;
	}

	/**
	 * @param throughout
	 *            the throughout to set
	 */
	public void setThroughout(Float throughout) {
		this.throughout = throughout;
	}

	/**
	 * @return the length
	 */
	public Float getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(Float length) {
		this.length = length;
	}

	/**
	 * @return the chamber_well
	 */
	public String getChamber_well() {
		return chamber_well;
	}

	/**
	 * @param chamber_well
	 *            the chamber_well to set
	 */
	public void setChamber_well(String chamber_well) {
		this.chamber_well = chamber_well;
	}

	/**
	 * @return the fangfu_station
	 */
	public String getFangfu_station() {
		return fangfu_station;
	}

	/**
	 * @param fangfu_station
	 *            the fangfu_station to set
	 */
	public void setFangfu_station(String fangfu_station) {
		this.fangfu_station = fangfu_station;
	}

	/**
	 * @return the admin
	 */
	public Integer getAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(Integer admin) {
		this.admin = admin;
	}

	/**
	 * @return the disable
	 */
	public Integer getDisable() {
		return disable;
	}

	/**
	 * @param disable
	 *            the disable to set
	 */
	public void setDisable(Integer disable) {
		this.disable = disable;
	}

}
