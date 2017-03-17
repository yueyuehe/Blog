package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PipelinePatrol_2016;
import cn.hm.oil.domain.RunRecordcomprehensive;
import cn.hm.oil.domain.RunRecordcomprehensiveDetail;
import cn.hm.oil.domain.RunRecordcomprehensive_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.util.PageSupport;

public interface BaseRcCompService_2016 {
	public List<BasePipeline> queryPipeLineByUser(Integer admin);
	public List<BaseReceiver> queryLeaderList();
	public void updateRunRecordcomprehensiveDetail(RunRecordcomprehensiveDetail recd);
	public void saveRcComp(RunRecordcomprehensive_2016 rec, RunRecordcomprehensiveDetail recd);
	public List<BasePipeline> queryPipeLineByAdminRcComp(Map<String, Object> param);
	public List<SysUsers>		queryUsers(Map<String, Object> param);
	public List<RunRecordcomprehensive_2016> queryRcComps(Map<String, Object> param, PageSupport ps);
	public RunRecordcomprehensiveDetail queryRunRecordcomprehensiveDetailById(Integer id);
	public void updateRunRecordcomprehensiveVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);
	
	/*public List<BasePipeline> queryPipeLineByAdminPatrol(Map<String, Object> param);
	
	
	
	public void savePlPatrol(PipelinePatrol_2016 pp, List<PipelinePatrolDetail> ppdList);
	public PipelinePatrol_2016 queryLastPatrolByUserId(Integer id);
	public PipelinePatrol_2016 queryPatrolById(Integer id);
	public List<PipelinePatrol_2016> queryPatrols(Map<String, Object> param, PageSupport ps);
	public void updatePipelinePatrolVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);*/
}
