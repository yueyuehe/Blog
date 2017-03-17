/**
 * 
 */
package cn.hm.oil.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.PotentialMeasure_2016;
import cn.hm.oil.domain.RunRecordDetail;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.util.PageSupport;

/**
 * @author Administrator
 * 
 * 管道保护电位测量_管理
 * 
 */
public interface BasePotentialMeasureDao_2016 {
				
	public void updatePotentialMeasure(PotentialMeasure_2016 potentialMeasure);
	public void deletePotentialMeasureDetailByPMid(@Param(value="pm_id") Integer pm_id);
	public void insertPotentialMeasure(PotentialMeasure_2016 potentialMeasure);
	public void insertPotentialMeasureDetail(PotentialMeasureDetail potentialMeasureDetail);
	public List<BasePipeline> queryBasePipelineByAdminPlMeasure(Map<String, Object> param);
	public List<SysUsers> queryUsers(Map<String, Object> param);
	public List<PotentialMeasure_2016> queryPlMeasures(Map<String, Object> param);
	public void updatePotentialMeasureVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
	public PotentialMeasure_2016 queryPotentialMeasureByCreate(Date createTime, Integer createId);
	
	
	/*
	
	
	public PotentialMeasure queryPotentialMeasureById(@Param(value="id") Integer id);
	
	public void deletePotentialMeasureById(@Param(value="id") Integer id);
	
	
	
	
	
	public void updatePotentialMeasureDetail(PotentialMeasureDetail potentialMeasureDetail);
	
	public List<PotentialMeasure> queryPotentialMeasure(Map<String, Object> param);
	
	public void updatePotentialMeasureVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
	
	public void updatePotentialMeasureTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);
	
	public List<PotentialMeasure> queryPotentialMeasureMerge(Map<String, Object> param);
	
	public PotentialMeasure queryPotentialMeasureMergeInfo(Map<String, Object> param);

	public PotentialMeasure queryPotentialMeasureByParam(@Param("param") Map<String, Object> param);

	public int queryPotentialMeasureByParamTotal(@Param("param") Map<String, Object> param);
	
	public List<PotentialMeasureDetail> queryPotentialMeasureDetail(Map<String, Object> param, PageSupport ps);
	
	public void updateResetDetailStatus(@Param(value="id") Integer id);
	public int queryDetailStatus(@Param(value="id") Integer id);
	public void updateChangeDetailStatus(@Param(value="id") Integer id, @Param(value="status") Integer status);
	public List<PotentialMeasureDetail> queryPotentialMeasureDetail(Map<String, Object> param);	
	public List<PotentialMeasureDetail> queryPotentialMeasureDetailMerge(Map<String, Object> param);
	
	public void updateAutoSubmitEveryWeek();
	
	
	public List<BasePipelineSection> queryBasePipelineSectionByAdminPlMeasure(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPlMeasure(Map<String, Object> param);*/
}
