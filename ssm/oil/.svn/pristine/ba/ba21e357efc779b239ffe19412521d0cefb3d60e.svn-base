package cn.hm.oil.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BasePipeLineDao;
import cn.hm.oil.dao.BasePotentialMeasureDao_2016;
import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.PotentialMeasure_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BasePlMeasureService_2016;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.PageSupport;

@Service(value="basePlMeasureServiceImpl_2016")
public class BasePlMeasureServiceImpl_2016 extends AbstractModuleSuport implements BasePlMeasureService_2016{
	
	@Autowired
	private BasePotentialMeasureDao_2016 basePotentialMeasurelDao;
	
	@Autowired
	private BasePipeLineDao basePipeLineDao;
	
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
	public void savePlMeasure(PotentialMeasure_2016 pp, List<PotentialMeasureDetail> ppdList) {
		// TODO Auto-generated method stub
		if(pp.getId() != null && pp.getId().intValue() > 0) {
			basePotentialMeasurelDao.updatePotentialMeasure(pp);
			basePotentialMeasurelDao.deletePotentialMeasureDetailByPMid(pp.getId());
		} else {
			basePotentialMeasurelDao.insertPotentialMeasure(pp);
		}
		for (PotentialMeasureDetail ppd : ppdList){
			ppd.setPm_id(pp.getId());
			basePotentialMeasurelDao.insertPotentialMeasureDetail(ppd);
		}
	}

	@Override
	public List<BasePipeline> queryPipeLineByAdminPlMeasure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePotentialMeasurelDao.queryBasePipelineByAdminPlMeasure(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<SysUsers> queryUsers(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePotentialMeasurelDao.queryUsers(param);
	}

	@Override
	public List<PotentialMeasure_2016> queryPlMeasures(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if (ps == null) {
			return basePotentialMeasurelDao.queryPlMeasures(param);
		} else {
			return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BasePotentialMeasureDao_2016.queryPlMeasures",param, ps);
		}
	}


	@Override
	public void updatePlMeasureVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		basePotentialMeasurelDao.updatePotentialMeasureVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public PotentialMeasure_2016 queryPotentialMeasureByCreate(Date createTime, Integer id) {
		// TODO Auto-generated method stub
		return basePotentialMeasurelDao.queryPotentialMeasureByCreate(createTime,id);
	}
	
}
