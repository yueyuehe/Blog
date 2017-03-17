/**
 * 
 */
package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.util.PageSupport;

/**
 * @author Administrator
 * 
 */
public interface BasePipeLineDao {

	public List<BasePipeline> queryPipeLine(Map<String, Object> param);
	
	public List<BaseReceiver> queryLeaderList();
	
	public List<BasePipeline> queryPipeLineByUser(@Param(value="admin") Integer admin);
	
	public List<BasePipelineSection> querySection(@Param(value="pl_id") Integer pl_id);
	
	public List<BasePipelineSection> querySectionByUser(@Param(value="pl_id") Integer pl_id, @Param(value="admin") Integer admin);
	
	public List<BasePipelineSpec> querySpecByUser(@Param(value="pl_section_id") Integer pl_section_id, @Param(value="admin") Integer admin);
	
	public List<BasePipelineSpec> querySpec(@Param(value="pl_section_id") Integer pl_section_id);
	
	public BasePipeline queryPipeLineByName(@Param(value = "name") String name);
	
	public BasePipelineSection querySectionByName(@Param(value = "name") String name, @Param(value="pl_id") Integer pl_id);
	
	public BasePipelineSpec querySpecByName(@Param(value = "name") String name, @Param(value="pl_section_id") Integer pl_section_id);
	
	public void inserPipeLine(BasePipeline basePipeline);

	public void inserSection(BasePipelineSection basePipelineSection);
	
	public void inserSpec(BasePipelineSpec basePipelineSpec);

	public List<BasePipelineSpec> querySpecList(@Param("param") Map<String, Object> param);
	
	public List<BasePipelineSpec> querySpecListNew(Map<String, Object> param);
	
}
