package cn.hm.oil.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.PotentialCurve_2016;
import cn.hm.oil.domain.SysUsers;
import cn.hm.oil.util.PageSupport;

public interface BasePlCurveService_2016 {
	public void savePlCurve(PotentialCurve_2016 pc, List<PotentialCurveDetail> pcdList);
	public List<PotentialCurve_2016> queryPotentialCurve(Map<String, Object> param, PageSupport ps);
	public List<BasePipeline> queryPipeLine(Map<String, Object> param);
	public List<SysUsers>		queryUsers(Map<String, Object> param);
	public List<PotentialCurveDetail> queryPotentialCurveDetailByPcid(int pc_id);
	public void updatePotentialCurveVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);
	public PotentialCurve_2016 queryPotentialCurveById(Integer id);
	public List<BasePipeline> queryPipeLineByUser(Integer admin);
	public List<BaseReceiver> queryLeaderList();
	public PotentialCurve_2016 queryPotentialCurveByMeasureId(Integer id);
	public void updateAutoSignRed(Integer id, Integer red);
}
