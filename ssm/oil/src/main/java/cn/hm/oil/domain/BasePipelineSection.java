/**
 * 
 */
package cn.hm.oil.domain;

/**
 * @author Administrator
 * 
 */
public class BasePipelineSection {
	private Integer id;
	private Integer pl_id;
	private String name;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
