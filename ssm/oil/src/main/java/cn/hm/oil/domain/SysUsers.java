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
public class SysUsers {
	private Integer id;
	private String name;
	private String username;
	private String password;
	private Date last_login_time;
	private String last_login_ip;
	private String remark;
	private Integer level;
	private Integer event_checker;
	private String mail_address;

	private List<SysMenus> menus;

	public Integer getEvent_checker() {
		return event_checker;
	}

	public void setEvent_checker(Integer event_checker) {
		this.event_checker = event_checker;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	/**
	 * @return the menus
	 */
	public List<SysMenus> getMenus() {
		return menus;
	}

	/**
	 * @param menus
	 *            the menus to set
	 */
	public void setMenus(List<SysMenus> menus) {
		this.menus = menus;
	}

	/**
	 * @return the last_login_time
	 */
	public Date getLast_login_time() {
		return last_login_time;
	}

	/**
	 * @param last_login_time
	 *            the last_login_time to set
	 */
	public void setLast_login_time(Date last_login_time) {
		this.last_login_time = last_login_time;
	}

	/**
	 * @return the last_login_ip
	 */
	public String getLast_login_ip() {
		return last_login_ip;
	}

	/**
	 * @param last_login_ip
	 *            the last_login_ip to set
	 */
	public void setLast_login_ip(String last_login_ip) {
		this.last_login_ip = last_login_ip;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail_address() {
		return mail_address;
	}

	public void setMail_address(String mail_address) {
		this.mail_address = mail_address;
	}

}
