package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.domain.RoutineAttention;
import cn.hm.oil.domain.RoutineAttentionDetail;
import cn.hm.oil.util.PageSupport;

/**
 * 
 * 阴极保护站运行记录_管理
 * 
 */
public interface BaseRoutineAttentionDao {
	
	public void updateRoutineAttention(RoutineAttention routineAttention);
	
	public void deleteRoutineAttentionDetailByRaid(@Param(value="ra_id") Integer ra_id);
	
	public void deleteRoutineAttentionById(@Param(value="id") Integer id);
	
	public RoutineAttention queryRoutineAttentionById(@Param(value="id") Integer id);
	
	public void insertRoutineAttention(RoutineAttention routineAttention);
	
	public void insertRoutineAttentionDetail(RoutineAttentionDetail routineAttentionDetail);
	public void updateRoutineAttentionDetail(RoutineAttentionDetail pmd);
	
	public List<RoutineAttention> queryRoutineAttention(Map<String, Object> param);
	
	public void updateRoutineAttentionVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
	
	public void updateRoutineAttentionTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);
	
	public List<RoutineAttention> queryRoutineAttentionVerifyMerge(Map<String, Object> param);
	
	public RoutineAttention queryRoutineAttentionMergeInfor(Map<String, Object> param);
	
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailBraId(@Param(value="ra_id") Integer ra_id);
	
	public List<RoutineAttentionDetail> queryRoutineAttentionDetail(Map<String, Object> param, PageSupport ps);
	public List<RoutineAttentionDetail> queryRoutineAttentionDetail(Map<String, Object> param);
	
	public void updateResetDetailStatus(@Param(value="id") Integer id);
	public int queryDetailStatus(@Param(value="id") Integer id);
	public void updateChangeDetailStatus(@Param(value="id") Integer id, @Param(value="status") Integer status);
	
	public void updateAutoSubmitEveryWeek();
	
	public List<BasePipeline> queryBasePipelineByAdminRoutine(Map<String, Object> param);
	public List<BasePipelineSection> queryBasePipelineSectionByAdminRoutine(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminRoutine(Map<String, Object> param);
}
