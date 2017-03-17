package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.PerformanceMeasure;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.util.PageSupport;
/**
 * @author Administrator
 * 
 * 绝缘接头（法兰）性能测量记录_管理
 * 
 */

public interface BasePerformanceMeasureNewDao {
	
	public void updatePerformanceMeasure(PerformanceMeasure performanceMeasure);
	
	public void deletePerformanceMeasureDetailByPMid(@Param(value="pm_id") Integer pm_id);
	
	public void deletePerformanceMeasureById(@Param(value="id") Integer id);
	
	public PerformanceMeasure queryPerformanceMeasureById(@Param(value="id") Integer id);
	
	public void insertPerformanceMeasure(PerformanceMeasure pm);
	
	public void insertPerformanceMeasureDetail(PerformanceMeasureDetail pmd);
	
	public void updatePerformanceMeasureDetail(PerformanceMeasureDetail pmd);
	
	public List<PerformanceMeasure> queryPerformanceMeasure(Map<String, Object> param);
	
	public void updatePerformanceMeasureVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
	
	public void updatePerformanceMeasureTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);

	public int queryValvePatrolByListTotal(@Param("param") Map<String, Object> param);

	public PerformanceMeasure queryValvePatrolByList(@Param("param") Map<String, Object> param);

	public List<PerformanceMeasure> queryPerformanceMeasureAuditSpecList(@Param("param") Map<String, Object> param);
	
	public List<PerformanceMeasureDetail> queryPerformanceMeasureDetail(Map<String, Object> param, PageSupport ps);
	public List<PerformanceMeasureDetail> queryPerformanceMeasureDetail(Map<String, Object> param);
	
	public void updateResetDetailStatus(@Param(value="id") Integer id);
	public int queryDetailStatus(@Param(value="id") Integer id);
	public void updateChangeDetailStatus(@Param(value="id") Integer id, @Param(value="status") Integer status);
	
	public void updateAutoSubmitEveryWeek();
	public List<BasePipeline> queryBasePipelineByAdminPMeasure(Map<String, Object> param);
	public List<BasePipelineSection> queryBasePipelineSectionByAdminPMeasure(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPMeasure(Map<String, Object> param);
}
