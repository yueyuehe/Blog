package cn.hm.oil.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hm.oil.dao.BasePipeLineDao;
import cn.hm.oil.dao.BasePotentialCurveDao_2016;
import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.PotentialCurve_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.service.BasePlCurveService_2016;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.PageSupport;

@Service(value="basePlCurveService_2016")
public class BasePlCurveServiceImpl_2016 extends AbstractModuleSuport implements BasePlCurveService_2016{
	
	@Autowired
	private BasePotentialCurveDao_2016 basePotentialCurveDao;
	
	@Autowired
	private BasePipeLineDao basePipeLineDao;
	
	@Override
	public void savePlCurve(PotentialCurve_2016 pc, List<PotentialCurveDetail> pcdList) {
		// TODO Auto-generated method stub
		if (pc.getId() != null && pc.getId().intValue() > 0) {
			PotentialCurve_2016 pco = basePotentialCurveDao.queryPotentialCurveById(pc.getId());
			DataUtil.deleteByUploadImg(pco.getChartPath());
			basePotentialCurveDao.updatePotentialCurve(pc);
			basePotentialCurveDao.deletePotentialCurveDetailByPCid(pc.getId());
			this.updateAutoSignRed(pc.getId(), 0);
		} else {
			basePotentialCurveDao.insertPotentialCurve(pc);
		}
		for (PotentialCurveDetail pcd : pcdList) {
			pcd.setPc_id(pc.getId());
			basePotentialCurveDao.insertPotentialCurveDetail(pcd);
		}
	}

	@Override
	public List<PotentialCurve_2016> queryPotentialCurve(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if (ps == null) {
			return basePotentialCurveDao.queryPotentialCurve(param);
		} else {
			return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BasePotentialCurveDao_2016.queryPotentialCurve",param, ps);
		}
	}

	@Override
	public List<BasePipeline> queryPipeLine(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePotentialCurveDao.queryPipeLine(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<SysUsers> queryUsers(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePotentialCurveDao.queryUsers(param);
	}

	@Override
	public List<PotentialCurveDetail> queryPotentialCurveDetailByPcid(int pc_id) {
		// TODO Auto-generated method stub
		return basePotentialCurveDao.queryPotentialCurveDetailByPcid(pc_id);
	}

	@Override
	public void updatePotentialCurveVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		this.updateAutoSignRed(id, 0);
		basePotentialCurveDao.updatePotentialCurveVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public PotentialCurve_2016 queryPotentialCurveById(Integer id) {
		// TODO Auto-generated method stub
		return basePotentialCurveDao.queryPotentialCurveById(id);
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
	public PotentialCurve_2016 queryPotentialCurveByMeasureId(Integer id) {
		// TODO Auto-generated method stub
		return basePotentialCurveDao.queryPotentialCurveByMeasureId(id);
	}

	@Override
	public void updateAutoSignRed(Integer id, Integer red) {
		// TODO Auto-generated method stub
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("red", red);
		if(red.intValue() == 0)//标记为正常状态
		{
			if(id == null)
				return;
			params.put("id", id);
		}
		else if(red.intValue() == 1)
		{
			params.put("status", 0);
		}
		else if(red.intValue() == 2)
		{
			params.put("old_red", 1);
		}
		basePotentialCurveDao.updateAutoSignRed(params);
	}
}
