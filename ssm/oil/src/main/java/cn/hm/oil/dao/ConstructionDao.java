package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.Construction;

public interface ConstructionDao {
	public void insertConstruction(Construction construction);
	
	public void updateConstruction(Construction construction);
	
	public Construction queryConstructionById(@Param(value="id") Integer id);
	
	public void deleteConstructionById(@Param(value="id") Integer id);
	
	public void updateConstructionVerify(@Param(value="id") Integer id,@Param(value="verifier") Integer verifier,
			@Param(value="status")Integer status,@Param(value="verify_desc") String verify_desc);
	
	public List<Construction> queryConstruction(Map<String, Object> param);
	
	public void updateConstructionTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);

	public Construction queryConstructionByParam(@Param("param") Map<String, Object> param);

	public int queryConstructionByParamTotal(@Param("param") Map<String, Object> param);

	public List<BasePipelineSpec> queryConstructionAuditSpecList(@Param("param") Map<String, Object> param);
	
	public List<Construction> queryConstruction_new(Map<String, Object> param);
	public void updateChangeStatus(@Param("id") Integer id,@Param("status")  Integer status);
	
	public void updateAutoSubmitEveryWeek();
	
	public List<BasePipeline> queryBasePipelineByAdminConstruction(Map<String, Object> param);
	public List<BasePipelineSection> queryBasePipelineSectionByAdminConstruction(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminConstruction(Map<String, Object> param);
}
