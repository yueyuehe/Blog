package cn.hm.oil.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.PotentialMeasure_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.util.PageSupport;

public interface BasePlMeasureService_2016 {
	public List<BasePipeline> queryPipeLineByUser(Integer admin);
	public List<BaseReceiver> queryLeaderList();
	public void savePlMeasure(PotentialMeasure_2016 rec, List<PotentialMeasureDetail> recd);
	public void updatePlMeasureVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);
	public List<SysUsers>		queryUsers(Map<String, Object> param);
	public List<PotentialMeasure_2016> queryPlMeasures(Map<String, Object> param, PageSupport ps);
	//public void updatePlMeasureDetail(PotentialMeasureDetail recd);
	
	public List<BasePipeline> queryPipeLineByAdminPlMeasure(Map<String, Object> param);
	public PotentialMeasure_2016 queryPotentialMeasureByCreate(Date createTime, Integer id);
	
	//public PotentialMeasureDetail queryPlMeasureDetailById(Integer id);
	
}
