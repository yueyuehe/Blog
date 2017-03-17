package cn.hm.oil.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BasePipeLineDao;
import cn.hm.oil.dao.BasePipelinePatrolDAO_2016;
import cn.hm.oil.dao.BasePotentialCurveDao_2016;
import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PipelinePatrol_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BasePipelinePatrolService_2016;
import cn.hm.oil.service.BasePlCurveService_2016;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.PageSupport;

@Service(value="basePipelinePatrolServiceImpl_2016")
public class BasePipelinePatrolServiceImpl_2016 extends AbstractModuleSuport implements BasePipelinePatrolService_2016{
	
	@Autowired
	private BasePipelinePatrolDAO_2016 basePipelinePatrolDao;
	
	@Autowired
	private BasePipeLineDao basePipeLineDao;

	@Override
	public List<BasePipeline> queryPipeLineByAdminPatrol(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePipelinePatrolDao.queryBasePipelineByAdminPatrol(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<SysUsers> queryUsers(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePipelinePatrolDao.queryUsers(param);
	}

	@Override
	public List<BasePipeline> queryPipeLineByUser(Integer admin) {
		// TODO Auto-generated method stub
		return basePipeLineDao.queryPipeLineByUser(admin);
	}

	@Override
	public List<BaseReceiver> queryLeaderList() {
		// TODO Auto-generated method stub
		return basePipeLineDao.queryLeaderList();
	}

	@Override
	public void savePlPatrol(PipelinePatrol_2016 pp, List<PipelinePatrolDetail> ppdList) {
		// TODO Auto-generated method stub
		if(pp.getId() != null && pp.getId().intValue() > 0) {
			basePipelinePatrolDao.updatePipelinePatro(pp);
			basePipelinePatrolDao.deletePipelinePatrolDetailById(pp.getId());
		} else {
			basePipelinePatrolDao.insertPipelinePatrol(pp);
		}
		for (PipelinePatrolDetail ppd : ppdList){
			ppd.setPp_id(pp.getId());
			basePipelinePatrolDao.insertPipelinePatrolDetail(ppd);
		}
	}

	@Override
	public PipelinePatrol_2016 queryLastPatrolByUserId(Integer id, Integer pl_id) {
		// TODO Auto-generated method stub
		return basePipelinePatrolDao.queryLastPatrolByUserId(id,pl_id);
	}

	@Override
	public List<PipelinePatrol_2016> queryPatrols(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if (ps == null) {
			return basePipelinePatrolDao.queryPatrols(param);
		} else {
			return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BasePipelinePatrolDAO_2016.queryPatrols",param, ps);
		}
	}

	@Override
	public PipelinePatrol_2016 queryPatrolById(Integer id) {
		// TODO Auto-generated method stub
		return basePipelinePatrolDao.queryPatrolById(id);
	}

	@Override
	public void updatePipelinePatrolVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		basePipelinePatrolDao.updatePipelinePatrolVerifyStatus(id, verifier, status, verify_desc);
	}
}
