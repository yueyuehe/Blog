package cn.hm.oil.dao;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.CheckLog;
import cn.hm.oil.domain.Tips;

public interface TipsDao {
	
	public void saveTips(Tips tips);
	
	public void deleteTipsByid(@Param(value="id") Integer id);
	
	public void insertCheckLog(CheckLog cl);
	
	public void updateCheckLog(CheckLog cl);
	
	public CheckLog queryCheckById(Integer id);
	
	public void deleteCheckLogByid(Integer id);
	
}
