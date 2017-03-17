/**
 * 
 */
package cn.hm.oil.domain;

/**
 * @author Administrator
 * 
 */
public class BasePipelineSpec {
	private Integer id;
	private Integer pl_id;
	private Integer pl_section_id;
	private String name;
	private Boolean red=false;

	public Boolean getRed()
	{
		return red;
	}
	
	public void setRed(Boolean r)
	{
		this.red = r;
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
