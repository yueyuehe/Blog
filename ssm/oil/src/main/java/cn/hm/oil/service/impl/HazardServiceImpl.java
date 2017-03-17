package cn.hm.oil.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BaseHazardDao;
import cn.hm.oil.domain.Hazard;
import cn.hm.oil.service.ExcelService;
import cn.hm.oil.service.HazardService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.PageSupport;

@Service(value = "HazardService")
public class HazardServiceImpl extends AbstractModuleSuport implements HazardService {

	@Autowired
	private BaseHazardDao baseHazardDao;
	
	@Override
	public void saveHazard(Hazard hazard) {
		baseHazardDao.saveHazard(hazard);
	}
	
	@Override
	public List<Hazard> queryHazard(Map<String, Object> param,PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseHazardDao.queryHazard",
				"cn.hm.oil.dao.BaseHazardDao.queryHazard_count", param, ps);
	}
	
	@Override
	public Hazard queryHazardByid(Integer id){
		return baseHazardDao.queryHazardByid(id);
		
	}
	
	@Override
	public void deleteHazardByid(Integer id) {
		// TODO Auto-generated method stub
		baseHazardDao.deleteHazardByid(id);
		
	}
}
