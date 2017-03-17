package cn.hm.oil.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BaseRoutineAttentionDao;
import cn.hm.oil.domain.RoutineAttention;
import cn.hm.oil.domain.RoutineAttentionDetail;
import cn.hm.oil.service.BaseRoutineAttentionService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.PageSupport;

@Service(value="baseRoutineAttentionService")
public class BaseRoutineAttentionServiceImpl extends AbstractModuleSuport implements BaseRoutineAttentionService {

	@Autowired
	private BaseRoutineAttentionDao routineAttentionDao;
	
	@Override
	public void updateRoutineAttention(RoutineAttention routineAttention) {
		// TODO Auto-generated method stub
		deleteRoutineAttentionDetailByRaid(routineAttention.getId());
		routineAttentionDao.updateRoutineAttention(routineAttention);
		for(RoutineAttentionDetail rd: routineAttention.getDetaillist()) {
			routineAttentionDao.insertRoutineAttentionDetail(rd);
		}
	}

	@Override
	public void deleteRoutineAttentionDetailByRaid(Integer ra_id) {
		// TODO Auto-generated method stub
		routineAttentionDao.deleteRoutineAttentionDetailByRaid(ra_id);
	}

	@Override
	public void deleteRoutineAttentionById(Integer id) {
		// TODO Auto-generated method stub
		routineAttentionDao.deleteRoutineAttentionById(id);
	}

	@Override
	public RoutineAttention queryRoutineAttentionById(Integer id) {
		// TODO Auto-generated method stub
		return routineAttentionDao.queryRoutineAttentionById(id);
	}

	@Override
	public void insertRoutineAttention(RoutineAttention routineAttention) {
		// TODO Auto-generated method stub
		routineAttentionDao.insertRoutineAttention(routineAttention);
		for(RoutineAttentionDetail rd: routineAttention.getDetaillist()) {
			routineAttentionDao.insertRoutineAttentionDetail(rd);
		}
	}

	@Override
	public void insertRoutineAttentionDetail(RoutineAttentionDetail routineAttentionDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<RoutineAttention> queryRoutineAttention(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return routineAttentionDao.queryRoutineAttention(param);
	}

	@Override
	public List<RoutineAttention> queryRoutineAttention(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps==null){
			return routineAttentionDao.queryRoutineAttention(param);
		} else {
		}
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseRoutineAttentionDao.queryRoutineAttention", "cn.hm.oil.dao.BaseRoutineAttentionDao.queryRoutineAttention_count", param, ps);
	}

	@Override
	public void updateRoutineAttentionVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		routineAttentionDao.updateRoutineAttentionVerifyStatus(id, verifier, status, verify_desc);
	}
	
	public void saveRoutionAttention(RoutineAttention routineAttention) {
		if(routineAttention.getId() != null && routineAttention.getId().intValue() > 0) {
			routineAttentionDao.deleteRoutineAttentionDetailByRaid(routineAttention.getId());
			routineAttentionDao.updateRoutineAttention(routineAttention);
		} else {
			routineAttentionDao.insertRoutineAttention(routineAttention);
		}
		for(RoutineAttentionDetail rd: routineAttention.getDetaillist()) {
			rd.setRa_id(routineAttention.getId());
			routineAttentionDao.insertRoutineAttentionDetail(rd);
		}
	}	
	
	@Override
	public void updateRoutineAttentionTime(Integer id, String create_t,
			String verify_t) {
		routineAttentionDao.updateRoutineAttentionTime(id,create_t,verify_t);
	}

	@Override
	public List<RoutineAttention> queryRoutineAttentionVerifyMerge(Map<String, Object> param) {
		return routineAttentionDao.queryRoutineAttentionVerifyMerge(param);
	}

	@Override
	public RoutineAttention queryRoutineAttentionMergeInfor(Map<String, Object> param) {
		return routineAttentionDao.queryRoutineAttentionMergeInfor(param);
	}

	@Override
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailVerifyMerge(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BaseRoutineAttentionDao.queryRoutineAttentionDetailVerifyMerge", param, ps);
	}

	@Override
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailByraid(Integer ra_id) {
		return routineAttentionDao.queryRoutineAttentionDetailBraId(ra_id);
	}

	@Override
	public List<RoutineAttentionDetail> queryRoutineAttentionDetailVerifyMerge(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return routineAttentionDao.queryRoutineAttentionDetail(param);
	}
}
