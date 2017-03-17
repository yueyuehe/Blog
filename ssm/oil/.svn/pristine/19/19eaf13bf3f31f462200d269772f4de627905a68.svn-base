package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.CheckLog;
import cn.hm.oil.domain.Tips;
import cn.hm.oil.util.PageSupport;

public interface TipsService {
	public void saveTips(Tips tips);

	public void deleteTipsByid(Integer id);
	
	public void deleteCheckLogByid(Integer id);
	
	public List<Tips> queryTips(Map<String, Object> param, PageSupport ps);
	
	public void saveCheckLog(CheckLog cl);
	
	public List<CheckLog> queryCheckList(Map<String, Object> param, PageSupport ps);
	
	public CheckLog queryCheckById(Integer id);
}
