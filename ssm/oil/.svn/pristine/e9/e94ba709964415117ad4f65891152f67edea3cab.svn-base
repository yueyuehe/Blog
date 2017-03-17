package cn.hm.oil.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BasePipeLineDao;
import cn.hm.oil.dao.ConstructionDao;
import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.Construction;
import cn.hm.oil.service.ConstructionService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.PageSupport;

@Service(value="constructionService")
public class ConstructionServiceImpl extends AbstractModuleSuport implements ConstructionService {

	@Autowired
	private BasePipeLineDao basePipeLineDao;
	
	@Autowired
	private ConstructionDao constructionDao;
	
	@Override
	public void saveConstruction(Construction construction) {
		// TODO Auto-generated method stub
		if(construction.getId() != null) {
			constructionDao.updateConstruction(construction);
		} else {
			constructionDao.insertConstruction(construction);
		}
	}

	@Override
	public void deleteConstructionById(Integer id) {
		// TODO Auto-generated method stub
		constructionDao.deleteConstructionById(id);
	}

	@Override
	public List<Construction> queryConstruction(Map<String, Object> param,
			PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps != null) {
			return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.ConstructionDao.queryConstruction", param, ps);
			//return this.getListPageSupportByManualOperation("cn.hm.oil.dao.ConstructionDao.queryConstruction", "cn.hm.oil.dao.ConstructionDao.queryConstruction_count", param, ps);
		} else {
			return constructionDao.queryConstruction(param);
		}
	}

	@Override
	public void saveConstructionVerify(Integer id, Integer verifier,
			Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		constructionDao.updateConstructionVerify(id, verifier, status, verify_desc);
	}

	@Override
	public Construction queryConstructionById(Integer id) {
		// TODO Auto-generated method stub
		return constructionDao.queryConstructionById(id);
	}
	
	@Override
	public void updateConstructionTime(Integer id, String create_t,
			String verify_t) {
		constructionDao.updateConstructionTime(id,create_t,verify_t);
	}

	@Override
	public Construction queryConstructionByParam(Map<String, Object> param) {
		return constructionDao.queryConstructionByParam(param);
	}

	@Override
	public int queryConstructionByParamTotal(Map<String, Object> param) {
		return constructionDao.queryConstructionByParamTotal(param);
	}
	
	@Override
	public List<BasePipelineSpec> queryConstructionAuditSpecList(Map<String, Object> param) {
		return constructionDao.queryConstructionAuditSpecList(param);
	}

	@Override
	public List<Construction> queryConstruction_new(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.ConstructionDao.queryConstruction_new", param, ps);
	}

	@Override
	public List<Construction> queryConstruction_new(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return constructionDao.queryConstruction_new(param);
	}

	@Override
	public void updateChangeStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		constructionDao.updateChangeStatus(id, status);
	}

	@Override
	public List<BasePipeline> queryPipeLineByAdminConstruction(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return constructionDao.queryBasePipelineByAdminConstruction(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<BasePipelineSection> querySectionByAdminConstruction(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return constructionDao.queryBasePipelineSectionByAdminConstruction(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNewByAdminConstruction(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return constructionDao.querySpecListNewByAdminConstruction(param);
		return basePipeLineDao.querySpecListNew(param);
	}	
}
