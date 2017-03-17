package cn.hm.oil.domain;

import java.util.Date;

public class NoticeDelay {

	private Integer id;
	private Integer n_id;
	private String delay_reason;
	private Date delay_time;
	private Integer verify_delay;
	private String verify_reason;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getN_id() {
		return n_id;
	}
	public void setN_id(Integer n_id) {
		this.n_id = n_id;
	}
	public String getDelay_reason() {
		return delay_reason;
	}
	public void setDelay_reason(String delay_reason) {
		this.delay_reason = delay_reason;
	}
	public Date getDelay_time() {
		return delay_time;
	}
	public void setDelay_time(Date delay_time) {
		this.delay_time = delay_time;
	}
	public Integer getVerify_delay() {
		return verify_delay;
	}
	public void setVerify_delay(Integer verify_delay) {
		this.verify_delay = verify_delay;
	}
	public String getVerify_reason() {
		return verify_reason;
	}
	public void setVerify_reason(String verify_reason) {
		this.verify_reason = verify_reason;
	}
}
