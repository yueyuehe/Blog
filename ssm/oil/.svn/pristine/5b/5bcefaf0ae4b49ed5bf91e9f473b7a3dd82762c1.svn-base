package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.Construction;
import cn.hm.oil.util.PageSupport;

public interface ConstructionService {
	public void saveConstruction(Construction construction);
	
	public void deleteConstructionById(Integer id);
	
	public List<Construction> queryConstruction(Map<String, Object> param, PageSupport ps);
	
	public void saveConstructionVerify(Integer id, Integer verifier, Integer status, String verify_desc);
	
	public Construction queryConstructionById(Integer id);
	
	public void updateConstructionTime(Integer id,String create_t,String verify_t);

	public Construction queryConstructionByParam(Map<String, Object> param);

	public int queryConstructionByParamTotal(Map<String, Object> param);

	public List<BasePipelineSpec> queryConstructionAuditSpecList(Map<String, Object> param);
	
	public List<Construction> queryConstruction_new(Map<String, Object> param, PageSupport ps);
	public List<Construction> queryConstruction_new(Map<String, Object> param);
	public void updateChangeStatus(Integer id, Integer status);
	
	public List<BasePipeline> queryPipeLineByAdminConstruction(Map<String, Object> param);
	public List<BasePipelineSection> querySectionByAdminConstruction(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminConstruction( Map<String, Object> param);
}
