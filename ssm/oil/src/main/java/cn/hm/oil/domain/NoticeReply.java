/**
 * 
 */
package cn.hm.oil.domain;

import java.util.Date;

/**
 * @author Administrator
 * 
 */
public class NoticeReply {
	private Integer id;
	private Integer notice_id;
	private Integer user_id;
	private String content;
	private Date create_time;
	private String path;

	private String replier;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the replier
	 */
	public String getReplier() {
		return replier;
	}

	/**
	 * @param replier
	 *            the replier to set
	 */
	public void setReplier(String replier) {
		this.replier = replier;
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
	 * @return the notice_id
	 */
	public Integer getNotice_id() {
		return notice_id;
	}

	/**
	 * @param notice_id
	 *            the notice_id to set
	 */
	public void setNotice_id(Integer notice_id) {
		this.notice_id = notice_id;
	}

	/**
	 * @return the user_id
	 */
	public Integer getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id
	 *            the user_id to set
	 */
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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

}
