/**
 * 
 */
package cn.hm.oil.domain;

/**
 * @author Administrator
 * 
 */
public class Upgrade {
	private Integer id;
	private String os;
	private String version;
	private String update_path;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdate_path() {
		return update_path;
	}

	public void setUpdate_path(String update_path) {
		this.update_path = update_path;
	}

}
