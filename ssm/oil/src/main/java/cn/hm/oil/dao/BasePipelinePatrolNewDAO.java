package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;

/**
 * @author Administrator
 * 
 * 管道巡检工作记录_管理
 * 
 */
public interface BasePipelinePatrolNewDAO {
	
	public PipelinePatrol queryPipelinePatrolById(@Param(value="id") Integer id);
	
	public void deletePipelinePatrolById(@Param(value="id") Integer id);
	
	public void insertPipelinePatrol(PipelinePatrol pipelinePatrol);
	
	public void insertPipelinePatrolDetail(PipelinePatrolDetail pipelinePatrolDetail);
	
	public void updatePipelinePatrolDetail(PipelinePatrolDetail pipelinePatrolDetail);

	public List<PipelinePatrol> queryPipelinePatrol(Map<String, Object> param);
	
	public void updatePipelinePatrolVerifyStatus(@Param(value="id") Integer id,@Param(value="verifier") Integer verifier,@Param(value="status") Integer status,@Param(value="verify_desc") String verify_desc);

	public void updatePipelinePatro(PipelinePatrol pipelinePatrol);
	
	public void deletePipelinePatrolDetailById(@Param(value="pp_id") Integer pp_id);
	
	public void updatePipelinePatrolTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);

	public PipelinePatrol queryPipelinePatrolByList(@Param("param") Map<String, Object> param);

	public int queryPipelinePatrolByListTotal(@Param("param") Map<String, Object> param);

	public List<BasePipelineSpec> queryPipelinePatrolAuditSpecList(@Param("param") Map<String, Object> param);
	
	public PipelinePatrol queryPipelinePatrolMergeInfo(Map<String, Object> param);
	
	public List<PipelinePatrol> queryPipelinePatrolMerge(Map<String, Object> param);
	
	public List<PipelinePatrolDetail> queryPipelinePatrolDetail(Map<String, Object> param);
	
	public void updateResetDetailStatus(@Param(value="id") Integer id);
	public int queryDetailStatus(@Param(value="id") Integer id);
	public void updateChangeDetailStatus(@Param(value="id") Integer id, @Param(value="status") Integer status);
	public List<PipelinePatrolDetail> queryPipelinePatrolDetailMerge(Map<String, Object> param);
	
	public void updateAutoSubmitEveryWeek();
	public List<BasePipeline> queryBasePipelineByAdminPatrol(Map<String, Object> param);
	public List<BasePipelineSection> queryBasePipelineSectionByAdminPatrol(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPatrol(Map<String, Object> param);
	public int queryShouldRedPatrol(BasePipelineSpec spec);
}
