package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PerformanceMeasure;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.domain.PerformanceMeasure_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.util.PageSupport;

public interface BasePMeasureService_2016 {
	public List<BasePipeline> queryPipeLineByUser(Integer admin);
	public List<BaseReceiver> queryLeaderList();
	public PerformanceMeasure_2016 queryLastPMeasureByUserId(Integer id, Integer pl_id);
	public void savePMeasure(PerformanceMeasure_2016 pp, List<PerformanceMeasureDetail> ppdList);
	public PerformanceMeasure_2016 queryPMeasureById(Integer id);
	public List<BasePipeline> queryPipeLineByAdminPMeasure(Map<String, Object> param);
	public List<SysUsers>		queryUsers(Map<String, Object> param);
	public List<PerformanceMeasure_2016> queryPMeasures(Map<String, Object> param, PageSupport ps);
	public void updatePerformanceMeasureVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);
}
