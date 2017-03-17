package cn.hm.oil.domain;

import java.util.Date;

public class Sexcel {
	private Integer id;
	private String title;
	private Integer filesoure;
	private Date createtime;
	private String filetype;
	private String filesize;
	private String filepath;
	private Integer creater;
	private Date deletetime;
	private Integer daleter;
	private String typename;
	private Integer isdir;
	private Integer parent;
	
	private String deletername;
	private String creatername;
	
	public String getCreatername() {
		return creatername;
	}

	public void setCreatername(String creatername) {
		this.creatername = creatername;
	}

	public String getDeletername() {
		return deletername;
	}

	public void setDeletername(String deletername) {
		this.deletername = deletername;
	}

	public Integer getFilesoure() {
		return filesoure;
	}

	public void setFilesoure(Integer filesoure) {
		this.filesoure = filesoure;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Date getDeletetime() {
		return deletetime;
	}

	public void setDeletetime(Date deletetime) {
		this.deletetime = deletetime;
	}

	public Integer getDaleter() {
		return daleter;
	}

	public void setDaleter(Integer daleter) {
		this.daleter = daleter;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public Integer getIsdir() {
		return isdir;
	}

	public void setIsdir(Integer isdir) {
		this.isdir = isdir;
	}

}
