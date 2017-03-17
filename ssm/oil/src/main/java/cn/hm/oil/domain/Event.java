package cn.hm.oil.domain;

import java.util.Date;
import java.util.List;

/**
 * 一事一案结构
 * 
 * @author Administrator
 * 
 */
public class Event {
	private Integer id;
	private Integer pl_id;
	private Integer pl_section_id;
	private Integer pl_spec_id;
	private Integer type_id;
	private Date discover_date;
	private String position_start;
	private String position_end;
	private String longitude;
	private String latitude;
	private String content;
	private String remark;
	private Boolean send_notice;
	private String notice_file;
	private Boolean is_warn;
	private String warn_file;
	private String moon_file;
	private String scene_file;
	private String report_object;
	private Date create_time;
	private Integer created_by;
	private Integer status;
	private String message;
	private Integer close;
	private String keyword;

	private String reporter;
	private String verify;
	private String code_no;
	private Integer verify_level;
	private String level_name;
	private String typeName;
	
	private String pl_name;
	private String pl_section_name;
	private String pl_spec_name;
	
	private String ef_length;
	private String pl_no;
	private String risk;
	private String link_name;
	private String link_duty;
	private String link_method;
	private String unit_name;
	private String unit_address;
	private String unit_post;
	private String ma_remark;
	private String creater;
	private Boolean is_ma_remark;
	
	private Boolean is_delay;

	private List<String> fileNames;

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getClose() {
		return close;
	}

	public void setClose(Integer close) {
		this.close = close;
	}

	/**
	 * @return the report_object
	 */
	public String getReport_object() {
		return report_object;
	}

	/**
	 * @param report_object
	 *            the report_object to set
	 */
	public void setReport_object(String report_object) {
		this.report_object = report_object;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the type_id
	 */
	public Integer getType_id() {
		return type_id;
	}

	/**
	 * @param type_id
	 *            the type_id to set
	 */
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the reporter
	 */
	public String getReporter() {
		return reporter;
	}

	/**
	 * @param reporter
	 *            the reporter to set
	 */
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	/**
	 * @return the created_by
	 */
	public Integer getCreated_by() {
		return created_by;
	}

	/**
	 * @param created_by
	 *            the created_by to set
	 */
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
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
	 * @return the discover_date
	 */
	public Date getDiscover_date() {
		return discover_date;
	}

	/**
	 * @param discover_date
	 *            the discover_date to set
	 */
	public void setDiscover_date(Date discover_date) {
		this.discover_date = discover_date;
	}

	/**
	 * @return the position_start
	 */
	public String getPosition_start() {
		return position_start;
	}

	/**
	 * @param position_start
	 *            the position_start to set
	 */
	public void setPosition_start(String position_start) {
		this.position_start = position_start;
	}

	/**
	 * @return the position_end
	 */
	public String getPosition_end() {
		return position_end;
	}

	/**
	 * @param position_end
	 *            the position_end to set
	 */
	public void setPosition_end(String position_end) {
		this.position_end = position_end;
	}

	/**
	 * @return the gps_start
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param gps_start
	 *            the gps_start to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the gps_end
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param gps_end
	 *            the gps_end to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
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
	 * @return the send_notice
	 */
	public Boolean getSend_notice() {
		return send_notice;
	}

	/**
	 * @param send_notice
	 *            the send_notice to set
	 */
	public void setSend_notice(Boolean send_notice) {
		this.send_notice = send_notice;
	}

	public String getNotice_file() {
		return notice_file;
	}

	public void setNotice_file(String notice_file) {
		this.notice_file = notice_file;
	}

	/**
	 * @return the is_warn
	 */
	public Boolean getIs_warn() {
		return is_warn;
	}

	/**
	 * @param is_warn
	 *            the is_warn to set
	 */
	public void setIs_warn(Boolean is_warn) {
		this.is_warn = is_warn;
	}

	/**
	 * @return the attachment
	 */
	public String getWarn_file() {
		return warn_file;
	}

	/**
	 * @param attachment
	 *            the attachment to set
	 */
	public void setWarn_file(String warn_file) {
		this.warn_file = warn_file;
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

	public String getPl_name() {
		return pl_name;
	}

	public void setPl_name(String pl_name) {
		this.pl_name = pl_name;
	}

	public String getPl_section_name() {
		return pl_section_name;
	}

	public void setPl_section_name(String pl_section_name) {
		this.pl_section_name = pl_section_name;
	}

	public String getPl_spec_name() {
		return pl_spec_name;
	}

	public void setPl_spec_name(String pl_spec_name) {
		this.pl_spec_name = pl_spec_name;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getCode_no() {
		return code_no;
	}

	public void setCode_no(String code_no) {
		this.code_no = code_no;
	}

	public Integer getVerify_level() {
		return verify_level;
	}

	public void setVerify_level(Integer verify_level) {
		this.verify_level = verify_level;
	}

	public String getScene_file() {
		return scene_file;
	}

	public void setScene_file(String scene_file) {
		this.scene_file = scene_file;
	}

	public String getMoon_file() {
		return moon_file;
	}

	public void setMoon_file(String moon_file) {
		this.moon_file = moon_file;
	}

	public String getEf_length() {
		return ef_length;
	}

	public void setEf_length(String ef_length) {
		this.ef_length = ef_length;
	}

	public String getPl_no() {
		return pl_no;
	}

	public void setPl_no(String pl_no) {
		this.pl_no = pl_no;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getLink_name() {
		return link_name;
	}

	public void setLink_name(String link_name) {
		this.link_name = link_name;
	}

	public String getLink_duty() {
		return link_duty;
	}

	public void setLink_duty(String link_duty) {
		this.link_duty = link_duty;
	}

	public String getLink_method() {
		return link_method;
	}

	public void setLink_method(String link_method) {
		this.link_method = link_method;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getUnit_address() {
		return unit_address;
	}

	public void setUnit_address(String unit_address) {
		this.unit_address = unit_address;
	}

	public String getUnit_post() {
		return unit_post;
	}

	public void setUnit_post(String unit_post) {
		this.unit_post = unit_post;
	}

	public String getMa_remark() {
		return ma_remark;
	}

	public void setMa_remark(String ma_remark) {
		this.ma_remark = ma_remark;
	}

	public Boolean getIs_ma_remark() {
		return is_ma_remark;
	}

	public void setIs_ma_remark(Boolean is_ma_remark) {
		this.is_ma_remark = is_ma_remark;
	}

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public Boolean getIs_delay() {
		return is_delay;
	}

	public void setIs_delay(Boolean is_delay) {
		this.is_delay = is_delay;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

}
