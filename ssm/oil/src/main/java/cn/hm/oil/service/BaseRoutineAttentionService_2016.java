package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PipelinePatrol_2016;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.RoutineAttention;
import cn.hm.oil.domain.RoutineAttentionDetail;
import cn.hm.oil.domain.RoutineAttention_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.util.PageSupport;

public interface BaseRoutineAttentionService_2016 {
	
	public List<BasePipeline> queryPipeLineByAdminRoutineAttention(Map<String, Object> param);
	public List<SysUsers>		queryUsers(Map<String, Object> param);
	public List<BasePipeline> queryPipeLineByUser(Integer admin);
	public List<BaseReceiver> queryLeaderList();
	public RoutineAttention_2016 queryLastRoutineAttentionByUserId(Integer id, Integer pl_id);	
	public void saveRoutionAttention(RoutineAttention_2016 routineAttention, List<RoutineAttentionDetail> raList);
	public List<RoutineAttention_2016> queryRoutineAttentions(Map<String, Object> param, PageSupport ps);
	public void updateRoutineAttentionVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);
	public RoutineAttention_2016 queryRoutineAttentionById(Integer id);
	/*
	public void updateRoutineAttention(RoutineAttention routineAttention);
	
	public void deleteRoutineAttentionDetailByRaid(Integer ra_id);
	
	public void deleteRoutineAttentionById(Integer id);
	
	public RoutineAttention queryRoutineAttentionById(Integer id);
	
	public void insertRoutineAttention(RoutineAttention routineAttention);
	
	public void insertRoutineAttentionDetail(RoutineAttentionDetail routineAttentionDetail);
	
	public List<RoutineAttention> queryRoutineAttention(Map<String, Object> param);
	
	public List<RoutineAttention> queryRoutineAttention(Map<String, Object> param, PageSupport ps);
	
	

	public void saveRoutionAttention(RoutineAttention routineAttention);	
	
	public void updateRoutineAttentionTime(Integer id,String create_t,String verify_t);
	
	public List<RoutineAttention> queryRoutineAttentionVerifyMerge(Map<String, Object> param);
	
	public RoutineAttention queryRoutineAttentionMergeInfor(Map<String, Object> param);
	
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailVerifyMerge(Map<String, Object> param, PageSupport ps);
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailVerifyMerge(Map<String, Object> param);
	
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailByraid(Integer ra_id);*/
}
