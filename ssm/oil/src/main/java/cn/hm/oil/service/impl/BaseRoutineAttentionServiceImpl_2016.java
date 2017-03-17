package cn.hm.oil.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BasePipeLineDao;
import cn.hm.oil.dao.BasePotentialMeasureDao_2016;
import cn.hm.oil.dao.BaseRoutineAttentionDao;
import cn.hm.oil.dao.BaseRoutineAttentionDao_2016;
import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.RoutineAttention;
import cn.hm.oil.domain.RoutineAttentionDetail;
import cn.hm.oil.domain.RoutineAttention_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BaseRoutineAttentionService;
import cn.hm.oil.service.BaseRoutineAttentionService_2016;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.PageSupport;

@Service(value="baseRoutineAttentionServiceImpl_2016")
public class BaseRoutineAttentionServiceImpl_2016 extends AbstractModuleSuport implements BaseRoutineAttentionService_2016 {

	@Autowired
	private BaseRoutineAttentionDao_2016 basePotentialMeasurelDao;
	
	@Autowired
	private BasePipeLineDao basePipeLineDao;
	
	@Override
	public List<BasePipeline> queryPipeLineByAdminRoutineAttention(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePotentialMeasurelDao.queryPipeLineByAdminRoutineAttention(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<SysUsers> queryUsers(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePotentialMeasurelDao.queryUsers(param);
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
	public RoutineAttention_2016 queryLastRoutineAttentionByUserId(Integer id, Integer pl_id) {
		// TODO Auto-generated method stub
		return basePotentialMeasurelDao.queryLastRoutineAttentionByUserId(id,pl_id);
	}

	@Override
	public void saveRoutionAttention(RoutineAttention_2016 ra, List<RoutineAttentionDetail> raList) {
		// TODO Auto-generated method stub
		if(ra.getId() != null && ra.getId().intValue() > 0) {
			basePotentialMeasurelDao.updateRoutineAttention(ra);
			basePotentialMeasurelDao.deleteRoutineAttentionDetailByRaid(ra.getId());
		} else {
			basePotentialMeasurelDao.insertRoutineAttention(ra);
		}
		for (RoutineAttentionDetail ppd : raList){
			ppd.setRa_id(ra.getId());
			basePotentialMeasurelDao.insertRoutineAttentionDetail(ppd);
		}
	}

	@Override
	public List<RoutineAttention_2016> queryRoutineAttentions(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if (ps == null) {
			return basePotentialMeasurelDao.queryRoutineAttentions(param);
		} else {
			return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BaseRoutineAttentionDao_2016.queryRoutineAttentions",param, ps);
		}
	}

	@Override
	public void updateRoutineAttentionVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		basePotentialMeasurelDao.updateRoutineAttentionVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public RoutineAttention_2016 queryRoutineAttentionById(Integer id) {
		// TODO Auto-generated method stub
		return basePotentialMeasurelDao.queryRoutineAttentionById(id);
	}
}
