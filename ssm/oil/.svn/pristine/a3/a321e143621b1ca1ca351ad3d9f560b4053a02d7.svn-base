package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.RoutineAttention;
import cn.hm.oil.domain.RoutineAttentionDetail;
import cn.hm.oil.util.PageSupport;

public interface BaseRoutineAttentionService {
	
	public void updateRoutineAttention(RoutineAttention routineAttention);
	
	public void deleteRoutineAttentionDetailByRaid(Integer ra_id);
	
	public void deleteRoutineAttentionById(Integer id);
	
	public RoutineAttention queryRoutineAttentionById(Integer id);
	
	public void insertRoutineAttention(RoutineAttention routineAttention);
	
	public void insertRoutineAttentionDetail(RoutineAttentionDetail routineAttentionDetail);
	
	public List<RoutineAttention> queryRoutineAttention(Map<String, Object> param);
	
	public List<RoutineAttention> queryRoutineAttention(Map<String, Object> param, PageSupport ps);
	
	public void updateRoutineAttentionVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);

	public void saveRoutionAttention(RoutineAttention routineAttention);	
	
	public void updateRoutineAttentionTime(Integer id,String create_t,String verify_t);
	
	public List<RoutineAttention> queryRoutineAttentionVerifyMerge(Map<String, Object> param);
	
	public RoutineAttention queryRoutineAttentionMergeInfor(Map<String, Object> param);
	
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailVerifyMerge(Map<String, Object> param, PageSupport ps);
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailVerifyMerge(Map<String, Object> param);
	
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailByraid(Integer ra_id);
}
