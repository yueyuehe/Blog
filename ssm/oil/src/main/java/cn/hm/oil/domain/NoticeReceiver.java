/**
 * 
 */
package cn.hm.oil.domain;

/**
 * @author Administrator
 * 
 */
public class NoticeReceiver {
	private Integer id;
	private Integer notice_id;
	private Integer user_id;
	private Integer publish_id;

	private String receiver;
	private String mail;
	
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	
	public boolean equals(Object other){       //重写equals方法，后面最好重写hashCode方法
		 
		  if(this == other)                                      //先检查是否其自反性，后比较other是否为空。这样效率高
		   return true;
		  if(other == null)        
		   return false;
		  if( !(other instanceof NoticeReceiver))
		   return false;
		 
		  final NoticeReceiver nt = (NoticeReceiver)other;
		  //System.out.println("id"+nt.getUser_id()+"_____"+this.getUser_id());
		  if(nt.getUser_id()==this.getUser_id())
		   return true;
		  return false;
		 }

	public Integer getPublish_id() {
		return publish_id;
	}

	public void setPublish_id(Integer publish_id) {
		this.publish_id = publish_id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
