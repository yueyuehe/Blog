package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;

/**
 * @author Administrator
 * 
 * 管道巡检工作记录_管理
 * 
 */
public interface BasePipelinePatrolDAO {
	
	public PipelinePatrol queryPipelinePatrolById(@Param(value="id") Integer id);
	
	public void deletePipelinePatrolById(@Param(value="id") Integer id);
	
	public void insertPipelinePatrol(PipelinePatrol pipelinePatrol);
	
	public void insertPipelinePatrolDetail(PipelinePatrolDetail pipelinePatrolDetail);

	public List<PipelinePatrol> queryPipelinePatrol(Map<String, Object> param);
	
	public void updatePipelinePatrolVerifyStatus(@Param(value="id") Integer id,@Param(value="verifier") Integer verifier,@Param(value="status") Integer status,@Param(value="verify_desc") String verify_desc);

	public void updatePipelinePatro(PipelinePatrol pipelinePatrol);
	
	public void deletePipelinePatrolDetailById(@Param(value="pp_id") Integer pp_id);
}
