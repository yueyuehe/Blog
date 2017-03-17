/**
 * 
 */
package cn.hm.oil.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hm.oil.dao.BaseFacilitiesMaintenanceDao;
import cn.hm.oil.dao.BaseHighConsequenceDao;
import cn.hm.oil.dao.BasePerformanceMeasureNewDao;
import cn.hm.oil.dao.BasePipeLineDao;
import cn.hm.oil.dao.BasePipelinePatrolNewDAO;
import cn.hm.oil.dao.BasePotentialCurveDao;
import cn.hm.oil.dao.BasePotentialMeasureNewDao;
import cn.hm.oil.dao.BaseRoutineAttentionDao;
import cn.hm.oil.dao.BaseRunRecordNewDao;
import cn.hm.oil.dao.BaseRunRecordcomprehensiveNewDao;
import cn.hm.oil.dao.CapitalDao;
import cn.hm.oil.dao.ConstructionDao;
import cn.hm.oil.dao.DynamicConsequenceDao;
import cn.hm.oil.dao.InvestDao;
import cn.hm.oil.dao.LocalityDao;
import cn.hm.oil.dao.PromptDao;
import cn.hm.oil.dao.SysPushDao;
import cn.hm.oil.dao.SysUsersDao;
import cn.hm.oil.dao.TipsDao;
import cn.hm.oil.dao.ValveMaintDao;
import cn.hm.oil.dao.ValvePatrolDao;
import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.BaseReceiver;
import cn.hm.oil.domain.Capital;
import cn.hm.oil.domain.CapitalDetail;
import cn.hm.oil.domain.DynamicConsequence;
import cn.hm.oil.domain.DynamicConsequenceDetail;
import cn.hm.oil.domain.DynamicConsequenceDetailAttachement;
import cn.hm.oil.domain.FacilitiesMaintenance;
import cn.hm.oil.domain.FacilitiesMaintenanceDetail;
import cn.hm.oil.domain.HighConsequence;
import cn.hm.oil.domain.HighConsequenceDetail;
import cn.hm.oil.domain.Invest;
import cn.hm.oil.domain.InvestDetail;
import cn.hm.oil.domain.Locality;
import cn.hm.oil.domain.LocalityDetail;
import cn.hm.oil.domain.PerformanceMeasure;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.domain.PipelinePatrol;
import cn.hm.oil.domain.PipelinePatrolDetail;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.PotentialMeasureDetail;
import cn.hm.oil.domain.Prompt;
import cn.hm.oil.domain.PromptType;
import cn.hm.oil.domain.RoutineAttentionDetail;
import cn.hm.oil.domain.RunRecord;
import cn.hm.oil.domain.RunRecordDetail;
import cn.hm.oil.domain.RunRecordcomprehensive;
import cn.hm.oil.domain.RunRecordcomprehensiveDetail;
import cn.hm.oil.domain.SysPush;
import cn.hm.oil.domain.Tips;
import cn.hm.oil.domain.ValveMaint;
import cn.hm.oil.domain.ValveMaintDetail;
import cn.hm.oil.domain.ValvePatrol;
import cn.hm.oil.domain.ValvePatrolDetail;
import cn.hm.oil.service.NewBaseService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.AndroidPushMessageSample;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SimpleMailSender;

/**
 * @author Administrator
 *
 */
@Service(value="newBaseService")
public class NewBaseServiceImpl extends AbstractModuleSuport implements NewBaseService {

	@Autowired
	private BasePipeLineDao basePipeLineDao;
	
	@Autowired
	private BasePotentialMeasureNewDao basePotentialMeasureDao;
	
	@Autowired
	private BaseFacilitiesMaintenanceDao baseFacilitiesMaintenanceDao;
	
	@Autowired
	private BasePipelinePatrolNewDAO basePipelinePatrolNewDAO;
	
	@Autowired
	private BaseRunRecordNewDao baseRunRecordNewDao;
	
	@Autowired
	private BaseRunRecordcomprehensiveNewDao baseRunRecordcomprehensiveDao;
	
	@Autowired
	private BasePerformanceMeasureNewDao basePerformanceMeasureNewDao;
	
	@Autowired
	private BasePotentialCurveDao basePotentialCurveDao;
	
	@Autowired
	private BaseHighConsequenceDao baseHighConsequenceDao;
	
	@Autowired
	private BaseRoutineAttentionDao baseRoutineAttentionDao;
	
	@Autowired
	private ConstructionDao constructionDao;
	
	@Autowired
	private InvestDao investDao;
	
	@Autowired
	private CapitalDao capitalDao;
	
	@Autowired
	private LocalityDao localityDao;
	
	@Autowired
	private PromptDao promptDao;
	
	@Autowired
	private ValveMaintDao valveMaintDao;
	
	@Autowired
	private ValvePatrolDao valvePatrolDao;
	
	@Autowired
	private DynamicConsequenceDao dynamicConsequenceDao;

	@Autowired
	private TipsDao tipsDao;
	@Autowired
	private SysPushDao sysPushDao;
	@Autowired
	private SysUsersDao sysUsersDao;
	
	@Override
	public List<BasePipeline> queryPipeLine() {
		return basePipeLineDao.queryPipeLine(new HashMap<String,Object>());
	}

	@Override
	public List<BasePipelineSection> querySection(Integer pl_id) {
		return basePipeLineDao.querySection(pl_id);
	}
	
	@Override
	public List<BasePipelineSpec> querySpec(Integer pl_section_id) {
		return basePipeLineDao.querySpec(pl_section_id);
	}

	@Override
	public void savePlMeasure(PotentialMeasure pm, List<PotentialMeasureDetail> pmdList) {
		if (pm.getId() != null && pm.getId().intValue() > 0) {
			basePotentialMeasureDao.updatePotentialMeasure(pm);
			basePotentialMeasureDao.deletePotentialMeasureDetailByPMid(pm.getId());
		} else {
			basePotentialMeasureDao.insertPotentialMeasure(pm);
		}
		for (PotentialMeasureDetail pmd : pmdList) {
			pmd.setPm_id(pm.getId());
			basePotentialMeasureDao.insertPotentialMeasureDetail(pmd);
		}
	}
	
	@Override
	public void modifyPlMeasureDetail(PotentialMeasureDetail pmd) {
		// TODO Auto-generated method stub
		basePotentialMeasureDao.updatePotentialMeasureDetail(pmd);
	}
	
	@Override
	public void savePlPatrol(PipelinePatrol pp, List<PipelinePatrolDetail> ppdList) {
		if(pp.getId() != null && pp.getId().intValue() > 0) {
			basePipelinePatrolNewDAO.updatePipelinePatro(pp);
			basePipelinePatrolNewDAO.deletePipelinePatrolDetailById(pp.getId());
		} else {
			basePipelinePatrolNewDAO.insertPipelinePatrol(pp);
		}
		for (PipelinePatrolDetail ppd : ppdList){
			ppd.setPp_id(pp.getId());
			basePipelinePatrolNewDAO.insertPipelinePatrolDetail(ppd);
		}
	}
	
	@Override
	public void modifyPlPatrolDetail(PipelinePatrolDetail ppd) {
		// TODO Auto-generated method stub
		basePipelinePatrolNewDAO.updatePipelinePatrolDetail(ppd);
	}
	
	@Override
	public List<PipelinePatrol> queryPipelinePatrol(Map<String, Object> param, PageSupport ps) {
		if(ps==null){
			return basePipelinePatrolNewDAO.queryPipelinePatrol(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BasePipelinePatrolNewDAO.queryPipelinePatrol", "cn.hm.oil.dao.BasePipelinePatrolNewDAO.queryPipelinePatrol_count", param, ps);
		}
	}

	@Override
	public List<PotentialMeasure> queryPotentialMeasure(Map<String, Object> param, PageSupport ps) {
		if(ps==null){
			return basePotentialMeasureDao.queryPotentialMeasure(param);
		} else {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BasePotentialMeasureNewDao.queryPotentialMeasure", "cn.hm.oil.dao.BasePotentialMeasureNewDao.queryPotentialMeasure_count", param, ps);
		}
	}

	@Override
	public void deletePotentialMeasureById(Integer id) {
		basePotentialMeasureDao.deletePotentialMeasureDetailByPMid(id);
		basePotentialMeasureDao.deletePotentialMeasureById(id);
	}

	@Override
	public void deletePipelinePatrolById(Integer id) {
		PipelinePatrol pp = basePipelinePatrolNewDAO.queryPipelinePatrolById(id);
		if(!CollectionUtils.isEmpty(pp.getDetailList())){
			for(PipelinePatrolDetail pd : pp.getDetailList()){
				if(!StringUtils.isBlank(pd.getAnnex())){
					DataUtil.deleteByUploadImg(pd.getAnnex());
				}
			}
		}
		basePipelinePatrolNewDAO.deletePipelinePatrolDetailById(id);
		basePipelinePatrolNewDAO.deletePipelinePatrolById(id);
	}
	
	@Override
	public PotentialMeasure queryPotentialMeasureById(Integer id) {
		return basePotentialMeasureDao.queryPotentialMeasureById(id);
	}
	
	@Override
	public PipelinePatrol queryPipelinePatrolById(Integer id) {
		return basePipelinePatrolNewDAO.queryPipelinePatrolById(id);
	}
	
	@Override
	public void saveFMaint(FacilitiesMaintenance fm, List<FacilitiesMaintenanceDetail> fmdList) {
		if(fm.getId() != null && fm.getId().intValue() > 0) {
			baseFacilitiesMaintenanceDao.updateFacilitiesMaintenance(fm);
			baseFacilitiesMaintenanceDao.deleteFacilitiesMaintenanceDetailById(fm.getId());
		} else {
			baseFacilitiesMaintenanceDao.insertFacilitiesMaintenance(fm);
		}
		for (FacilitiesMaintenanceDetail fmd : fmdList){
			fmd.setFm_id(fm.getId());
			baseFacilitiesMaintenanceDao.insertFacilitiesMaintenanceDetail(fmd);
		}
	}
	
	@Override
	public List<FacilitiesMaintenance> queryFacilitiesMaintenance(Map<String, Object> param, PageSupport ps) {
		if(ps==null){
			return baseFacilitiesMaintenanceDao.queryFacilitiesMaintenance(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseFacilitiesMaintenanceDao.queryFacilitiesMaintenance", "cn.hm.oil.dao.BaseFacilitiesMaintenanceDao.queryFacilitiesMaintenance_count", param, ps);
		}
	}

	@Override
	public FacilitiesMaintenance queryFacilitiesMaintenanceById(Integer id) {
		return baseFacilitiesMaintenanceDao.queryFacilitiesMaintenanceById(id);
	}
	
	@Override
	public void saveRcRecord(RunRecord rc, List<RunRecordDetail> rcdList){
		if (rc.getId() != null && rc.getId().intValue() > 0) {
			baseRunRecordNewDao.updateRunRecord(rc);
			baseRunRecordNewDao.deleteRunRecordDetailByRid(rc.getId());
		} else {
			baseRunRecordNewDao.insertRunRecord(rc);
		}
		for(RunRecordDetail rcd : rcdList) {
			rcd.setR_id(rc.getId());
			baseRunRecordNewDao.insertRunRecordDetail(rcd);
		}
	}
	
	@Override
	public void modifyRunRecordDetail(RunRecordDetail rcd) {
		// TODO Auto-generated method stub
		baseRunRecordNewDao.updateRunRecordDetail(rcd);
	}
	
	@Override
	public List<RunRecord> queryRunRecord(Map<String, Object> param, PageSupport ps) {
		if(ps==null){
			return baseRunRecordNewDao.queryRunRecord(param);
		}else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseRunRecordNewDao.queryRunRecord", "cn.hm.oil.dao.BaseRunRecordNewDao.queryRunRecord_count", param, ps);
		}
	}
	
	@Override
	public RunRecord queryRunRecordById(Integer id) {
		return baseRunRecordNewDao.queryRunRecordById(id);
	}
	
	@Override
	public void saveRcComp(RunRecordcomprehensive rec, RunRecordcomprehensiveDetail recd) {
		if (rec.getId() != null && rec.getId().intValue() > 0) {
			baseRunRecordcomprehensiveDao.updateRunRecordcomprehensive(rec);
			baseRunRecordcomprehensiveDao.deleteRunRecordcomprehensiveDetailByCid(rec.getId());
		} else {
			baseRunRecordcomprehensiveDao.insertRunRecordcomprehensive(rec);
		}
		recd.setC_id(rec.getId());
		baseRunRecordcomprehensiveDao.insertRunRecordcomprehensiveDetail(recd);
	}
	 
	@Override
	public List<RunRecordcomprehensive> queryRunRecordcomprehensive(Map<String, Object> param, PageSupport ps) {
		if(ps==null){
			return baseRunRecordcomprehensiveDao.queryRunRecordcomprehensive(param);
		}else{
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseRunRecordcomprehensiveNewDao.queryRunRecordcomprehensive",
				"cn.hm.oil.dao.BaseRunRecordcomprehensiveNewDao.queryRunRecordcomprehensive_count", param, ps);
		}
	}
	
	@Override
	public RunRecordcomprehensiveDetail queryRunRecordcomprehensiveDetailById(Integer id) {
		return baseRunRecordcomprehensiveDao.queryRunRecordcomprehensiveDetailById(id);
	}
	
	@Override
	public RunRecordcomprehensive queryRunRecordcomprehensiveById(Integer id) {
		return baseRunRecordcomprehensiveDao.queryRunRecordcomprehensiveById(id);
	}
	
	@Override
	public void savePMeasure(PerformanceMeasure pm, List<PerformanceMeasureDetail> pmdList) {
		if (pm.getId() != null && pm.getId().intValue() > 0) {
			basePerformanceMeasureNewDao.updatePerformanceMeasure(pm);
			basePerformanceMeasureNewDao.deletePerformanceMeasureDetailByPMid(pm.getId());
		} else {
			basePerformanceMeasureNewDao.insertPerformanceMeasure(pm);
		}
		for(PerformanceMeasureDetail pmd : pmdList) {
			pmd.setPm_id(pm.getId());
			basePerformanceMeasureNewDao.insertPerformanceMeasureDetail(pmd);
		}
	}
	
	@Override
	public List<PerformanceMeasure> queryPerformanceMeasure(Map<String, Object> param, PageSupport ps) {
		if(ps==null){
			return basePerformanceMeasureNewDao.queryPerformanceMeasure(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BasePerformanceMeasureNewDao.queryPerformanceMeasure",
				"cn.hm.oil.dao.BasePerformanceMeasureNewDao.queryPerformanceMeasure_count", param, ps);
		}
	}
	
	@Override
	public PerformanceMeasure queryPerformanceMeasureById(Integer id) {
		return basePerformanceMeasureNewDao.queryPerformanceMeasureById(id);
	}
	
	@Override
	public void savePlCurve(PotentialCurve pc, List<PotentialCurveDetail> pcdList) {
		if (pc.getId() != null && pc.getId().intValue() > 0) {
			PotentialCurve pco = basePotentialCurveDao.queryPotentialCurveById(pc.getId());
			DataUtil.deleteByUploadImg(pco.getChartPath());
			basePotentialCurveDao.updatePotentialCurve(pc);
			basePotentialCurveDao.deletePotentialCurveDetailByPCid(pc.getId());
		} else {
			basePotentialCurveDao.insertPotentialCurve(pc);
		}
		for(PotentialCurveDetail pcd : pcdList) {
			pcd.setPc_id(pc.getId());
			basePotentialCurveDao.insertPotentialCurveDetail(pcd);
		}
	}
	
	@Override
	public List<PotentialCurve> queryPotentialCurve(Map<String, Object> param, PageSupport ps) {
		if(ps==null){
			return basePotentialCurveDao.queryPotentialCurve(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BasePotentialCurveDao.queryPotentialCurve",
					"cn.hm.oil.dao.BasePotentialCurveDao.queryPotentialCurve_count", param, ps);
		}
	}
	
	@Override
	public PotentialCurve queryPotentialCurveById(Integer id) {
		return basePotentialCurveDao.queryPotentialCurveById(id);
	}

	@Override
	public void updatePotentialMeasureVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		basePotentialMeasureDao.updatePotentialMeasureVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public List<BasePipeline> queryPipeLineByUser(Integer admin) {
		// TODO Auto-generated method stub
		return basePipeLineDao.queryPipeLineByUser(admin);
	}

	@Override
	public List<BasePipelineSection> querySectionByUser(Integer pl_id,
			Integer admin) {
		// TODO Auto-generated method stub
		return basePipeLineDao.querySectionByUser(pl_id, admin);
	}

	@Override
	public List<BasePipelineSpec> querySpecByUser(
			Integer pl_section_id, Integer admin) {
		// TODO Auto-generated method stub
		return basePipeLineDao.querySpecByUser(pl_section_id, admin);
	}

	@Override
	public void updatePerformanceMeasureVerifyStatus(Integer id,
			Integer verifier, Integer status, String verify_desc) {
		
		basePerformanceMeasureNewDao.updatePerformanceMeasureVerifyStatus(id, verifier, status, verify_desc);
		
	}

	@Override
	public void updateRunRecordcomprehensiveVerifyStatus(Integer id,
			Integer verifier, Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		
		baseRunRecordcomprehensiveDao.updateRunRecordcomprehensiveVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public void updateRunRecordVerifyStatus(Integer id, Integer verifier,
			Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		
		baseRunRecordNewDao.updateRunRecordVerifyStatus(id, verifier, status, verify_desc);
	}
	
	@Override
	public void updatePotentialCurveVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		if(status!=null){
			PotentialCurve p = basePotentialCurveDao.queryPotentialCurveById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的管道保护电位曲线图已审核通过！";
			} else {
				content = "您提交的管道保护电位曲线图未审核通过！";
			}
				saveTips(content, p.getCreater(), "/admin/base/pl_curve/show?id=" + id);
				//eventDao.updateEventStatus(status, message, event_id, type_id);
			}
		basePotentialCurveDao.updatePotentialCurveVerifyStatus(id, verifier, status, verify_desc);
	}
	
	@Override
	public void updatePipelinePatrolVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		
		basePipelinePatrolNewDAO.updatePipelinePatrolVerifyStatus(id, verifier, status, verify_desc);
	}
	
	@Override
	public void updateFacilitiesMaintenanceVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		if(status!=null){
		FacilitiesMaintenance f = baseFacilitiesMaintenanceDao.queryFacilitiesMaintenanceById(id);
		String content;
		if (status.intValue() == 1) {
			content = "您提交的集输气管线附属设施台帐已审核通过！";
		} else {
			content = "您提交的集输气管线附属设施台帐未审核通过！";
		}
			saveTips(content, f.getCreater(), "/admin/base/f_maint/show?id=" + id);
			//eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		baseFacilitiesMaintenanceDao.updateFacilitiesMaintenanceVerifyStatus(id, verifier, status,verify_desc);
	}
	
	private void saveTips(String message, Integer user_id, String url) {
		Tips tips = new Tips();
		tips.setContent(message);
		tips.setUser_id(user_id);
		tips.setUrl(url);
		//此处需要查询用户的通道id
		tipsDao.saveTips(tips);
		List<String> address = new ArrayList<String>();
		String mail = sysUsersDao.queryMailAddress(user_id);
		if(!StringUtils.isBlank(mail)){
			address.add(mail);
			SimpleMailSender.sendMail(address,"您有新的消息，请及时处理",message);
		}
		SysPush push = sysPushDao.queryPush(tips.getUser_id());
		if(push != null) {
			AndroidPushMessageSample.pushNotice(push.getUserId(), push.getChannelId(), "新消息", tips);
		}
		//AndroidPushMessageSample.pushNotice("995379719876257548", 3870620768511225045L, "新消息", tips);

	}
	
	@Override
	public void saveHighConse(HighConsequence hc, List<HighConsequenceDetail> hcdList) {
		if (hc.getId() != null && hc.getId().intValue() > 0) {
			baseHighConsequenceDao.updateHighConsequence(hc);
			baseHighConsequenceDao.deleteHighConsequenceDetailByHcId(hc.getId());
		} else {
			baseHighConsequenceDao.insertHighConsequence(hc);
		}
		for(HighConsequenceDetail hcd:hcdList) {
			hcd.setHc_id(hc.getId());
			baseHighConsequenceDao.insertHighConsequenceDetail(hcd);
		}
	}
	
	@Override
	public List<HighConsequence> queryHighConsequence(Map<String, Object> param, PageSupport ps) {
		if(ps == null) {
			return baseHighConsequenceDao.queryHighConsequence(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseHighConsequenceDao.queryHighConsequence",
					"cn.hm.oil.dao.BaseHighConsequenceDao.queryHighConsequence_count", param, ps);
		}
	}
	
	@Override
	public List<Invest> queryInvest(PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.InvestDao.queryInvest", "cn.hm.oil.dao.InvestDao.queryInvest_count", null, ps);
	}
	
	@Override
	public List<Capital> queryCapital(PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.CapitalDao.queryCapital", "cn.hm.oil.dao.CapitalDao.queryCapital_count", null, ps);
	}
	
	@Override
	public List<Locality> queryLocality(PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.LocalityDao.queryLocality", "cn.hm.oil.dao.LocalityDao.queryLocality_count", null, ps);
	}
	
	@Override
	public HighConsequence queryHighConsequenceById(Integer id) {
		return baseHighConsequenceDao.queryHighConsequenceById(id);
	}
	
	@Override
	public void updateHighConsequenceVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc, Integer creater) {
		baseHighConsequenceDao.updateHighConsequenceVerifyStatus(id, verifier, status, verify_desc);
		String content;
		if (status == 1) {
			content = "您提交的静态高后果区资料已审核通过！";
		} else {
			content = "您提交的静态高后果区资料未审核通过！";
		}
		saveTips(content, creater, "/admin/base/h_sequel/show?id=" + id);
	}
	
	@Override
	public void saveInvest(Invest invest, List<InvestDetail> indList) {
		if(invest.getId() != null) {
			investDao.deleteInvestDetail(invest.getId());
			investDao.updateInvest(invest.getId());
			for(InvestDetail ind : indList) {
				ind.setOperate_num(investDao.queryOperate_num(invest.getId()));
				ind.setIv_id(invest.getId());
				investDao.saveInvestDetail(ind);
			}
		} else {
		investDao.saveInvest(invest);
			for(InvestDetail ind : indList) {
				//ind.setOperate_num(invest.getOperate_num() + 1);
				ind.setIv_id(invest.getId());
				investDao.saveInvestDetail(ind);
			}
		}
	}
	
	@Override
	public Invest queryInvestById(Integer id) {
		return investDao.queryInvestById(id);
	}
	
	@Override
	public void saveCapital(Capital capital, List<CapitalDetail> cadList) {
		if(capital.getId() != null) {
			capitalDao.deleteCapitalDetail(capital.getId());
			capitalDao.updateCapital(capital.getId());
			for(CapitalDetail cad : cadList) {
				cad.setOperate_num(capitalDao.queryOperate_num(capital.getId()));
				cad.setCa_id(capital.getId());
				capitalDao.saveCapitalDetail(cad);
			}
		} else {
			capitalDao.saveCapital(capital);
			for(CapitalDetail cad : cadList) {
				cad.setCa_id(capital.getId());
				capitalDao.saveCapitalDetail(cad);
			}
		}
	}
	
	@Override
	public Capital queryCapitalById(Integer id) {
		return capitalDao.queryCapitalById(id);
	}
	
	@Override
	public void saveLocality(Locality locality, List<LocalityDetail> lodList) {
		if(locality.getId() != null) {
			localityDao.deleteLocalityDetail(locality.getId());
			localityDao.updateLocality(locality.getId());
			for(LocalityDetail lod : lodList) {
				lod.setOperate_num(localityDao.queryOperate_num(locality.getId()));
				lod.setLo_id(locality.getId());
				localityDao.saveLocalityDetail(lod);
			}
		} else {
			localityDao.saveLocality(locality);
			for(LocalityDetail lod : lodList) {
				lod.setLo_id(locality.getId());
				localityDao.saveLocalityDetail(lod);
			}
		}
	}
	
	@Override
	public Locality queryLocalityById(Integer id) {
		return localityDao.queryLocalityById(id);
	}
	
	@Override
	public List<PromptType> queryPromptType() {
		return promptDao.queryPromptType();
	}
	
	@Override
	public Prompt quertPromptByType(Integer type_id) {
		return promptDao.quertPromptByType(type_id);
	}
	
	@Override
	public void updatePromptByType(Prompt prompt) {
		if(promptDao.quertPromptByType(prompt.getType_id()) == null) {
			promptDao.insertPrompt(prompt);
		} else {
			promptDao.updatePrompt(prompt);
		}
	}
	
	@Override
	public void saveValveMaint(ValveMaint vm, ValveMaintDetail vmd) {
		if(vm.getId() != null) {
			valveMaintDao.updateValueMaint(vm);
			valveMaintDao.deleteValueMaintByVMId(vm.getId());
		} else {
			valveMaintDao.saveValveMaint(vm);
		}
		vmd.setVm_id(vm.getId());
		valveMaintDao.saveValveMaintDetail(vmd);
	}
	
	@Override
	public List<ValveMaint> queryValveMaint(Map<String, Object> param, PageSupport ps) {
		if(ps == null) {
			return valveMaintDao.queryValveMaint(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.ValveMaintDao.queryValveMaint", "cn.hm.oil.dao.ValveMaintDao.queryValveMaint_count", param, ps);
		}
	}
	
	@Override
	public ValveMaint queryValveMaintById(Integer id) {
		return valveMaintDao.queryValveMaintById(id);
	}
	
	@Override
	public void saveVMaintVerify(ValveMaint vm) {
		if(vm.getStatus()!=null){
			ValveMaint p = valveMaintDao.queryValveMaintById(vm.getId());
			String content;
			if (vm.getStatus().intValue() == 1) {
				content = "您提交的阀室、阀井维护保养工作记录已审核通过！";
			} else {
				content = "您提交的阀室、阀井维护保养工作记录未审核通过！";
			}
				saveTips(content, p.getCreater(), "/admin/base/v_maint/show?id=" + vm.getId());
				//eventDao.updateEventStatus(status, message, event_id, type_id);
			}
		valveMaintDao.saveVerify(vm);
	}
	
	@Override
	public void saveVPatrol(ValvePatrol vp,ValvePatrolDetail vpd) {
		if(vp.getId() != null) {
			valvePatrolDao.updateValvePatrol(vp);
			valvePatrolDao.deleteValvePatrolDetailByVPId(vp.getId());
		} else {
			valvePatrolDao.saveValvePatrol(vp);
		}
		vpd.setVp_id(vp.getId());
		valvePatrolDao.saveValvePatrolDetail(vpd);
	}
	
	@Override
	public List<ValvePatrol> queryValvePatrol(Map<String, Object> param, PageSupport ps) {
		if(ps == null) {
			return valvePatrolDao.queryValvePatrol(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.ValvePatrolDao.queryValvePatrol", "cn.hm.oil.dao.ValvePatrolDao.queryValvePatrol_count", param, ps);
		}
	}
	
	@Override
	public ValvePatrol queryValvePatrolById(Integer id) {
		return valvePatrolDao.queryValvePatrolById(id);
	}
	
	@Override
	public void saveValveVerify(ValvePatrol vp) {
		if(vp.getStatus()!=null){
			ValvePatrol p = valvePatrolDao.queryValvePatrolById(vp.getId());
			String content;
			if (vp.getStatus().intValue() == 1) {
				content = "您提交的阀室、阀井巡检工作记录已审核通过！";
			} else {
				content = "您提交的阀室、阀井巡检工作记录未审核通过！";
			}
				saveTips(content, p.getCreater(), "/admin/base/v_patrol/show?id=" + vp.getId());
				//eventDao.updateEventStatus(status, message, event_id, type_id);
			}
		valvePatrolDao.saveValveVerify(vp);
	}
	
	@Override
	public void saveDynamicConseq(DynamicConsequence dc, List<DynamicConsequenceDetail> dcdList) {
		DynamicConsequence d_s = new DynamicConsequence();
		if(dc.getId() != null) {
			d_s = dynamicConsequenceDao.queryDynamicById(dc.getId());
			dynamicConsequenceDao.deleteDynamicConseDetailByDcId(dc.getId());
			dynamicConsequenceDao.updateDynamic(dc);
		} else {
			dynamicConsequenceDao.saveDynamicConse(dc);
		}
		Integer fi = 0;
		for(DynamicConsequenceDetail dcd : dcdList) {
			dcd.setDc_id(dc.getId());
			dynamicConsequenceDao.saveDynamicConseDetail(dcd);
			//List<DynamicConsequenceDetailAttachement> dcdas = new ArrayList<DynamicConsequenceDetailAttachement>();
			if(!StringUtils.isBlank(dcd.getAnnex_file())){ 
				Integer i=1;
				for(String af : dcd.getAnnex_file().split(";")){
					DynamicConsequenceDetailAttachement dcda = new DynamicConsequenceDetailAttachement();
					dcda.setDc_id(dc.getId());
					dcda.setDcd_id(dcd.getId());
					dcda.setMonth(i);
					if(!StringUtils.isBlank(af)){
						String path="";
						try {
							path = DataUtil.moveToFileDir(af);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(!StringUtils.isBlank(path)){
							dcda.setPath(path);
						}
						if(!CollectionUtils.isEmpty(d_s.getDetailList())){
							if(d_s.getDetailList().size()>fi){
								DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
								if(!CollectionUtils.isEmpty(dc_s.getAttachementList())){
									if(dc_s.getAttachementList().size()>(i-1)){
										DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList().get(i-1); 
										if(!StringUtils.isBlank(dcd_s.getPath())){
											try {
												DataUtil.moveFileToDeleteFileDir(dcd_s.getPath());
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
								}
							}
						}
					} else {
						if(!CollectionUtils.isEmpty(d_s.getDetailList())){
							if(d_s.getDetailList().size()>fi){
								DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
								if(!CollectionUtils.isEmpty(dc_s.getAttachementList())){
									if(dc_s.getAttachementList().size()>(i-1)){
										DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList().get(i-1); 
										if(!StringUtils.isBlank(dcd_s.getPath())){
											dcda.setPath(dcd_s.getPath());
										}
									}
								}
							}
						}
					}
					dynamicConsequenceDao.insertDynamicConsequenceDetailAttachement(dcda);
					i++;
				}
				if(i<13){
					for(;i<13;i++){
						DynamicConsequenceDetailAttachement dcda = new DynamicConsequenceDetailAttachement();
						dcda.setDc_id(dc.getId());
						dcda.setDcd_id(dcd.getId());
						dcda.setMonth(i);
						if(!CollectionUtils.isEmpty(d_s.getDetailList())){
							if(d_s.getDetailList().size()>fi){
								DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
								if(!CollectionUtils.isEmpty(dc_s.getAttachementList())){
									if(dc_s.getAttachementList().size()>(i-1)){
										DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList().get(i-1); 
										if(!StringUtils.isBlank(dcd_s.getPath())){
											dcda.setPath(dcd_s.getPath());
										}
									}
								}
							}
						}
						dynamicConsequenceDao.insertDynamicConsequenceDetailAttachement(dcda);
					}
				}
			} else {
				for(Integer f=1;f<13;f++){
					DynamicConsequenceDetailAttachement dcda = new DynamicConsequenceDetailAttachement();
					dcda.setDc_id(dc.getId());
					dcda.setDcd_id(dcd.getId());
					dcda.setMonth(f);
					if(!CollectionUtils.isEmpty(d_s.getDetailList())){
						if(d_s.getDetailList().size()>fi){
							DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
							if(!CollectionUtils.isEmpty(dc_s.getAttachementList())){
								if(dc_s.getAttachementList().size()>(f-1)){
									DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList().get(f-1); 
									if(!StringUtils.isBlank(dcd_s.getPath())){
										dcda.setPath(dcd_s.getPath());
									}
								}
							}
						}
					}
					dynamicConsequenceDao.insertDynamicConsequenceDetailAttachement(dcda);
				}
			}
			if(!CollectionUtils.isEmpty(d_s.getDetailList())){
				if(d_s.getDetailList().size()>fi){
					dynamicConsequenceDao.deleteDynamicConseDetailAttachementByDetailId(d_s.getDetailList().get(fi).getId());
				}
			}
			fi++;
		}
		if(!CollectionUtils.isEmpty(d_s.getDetailList())){
			if(d_s.getDetailList().size()>fi){
				for(;fi<d_s.getDetailList().size();fi++){
					DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
					if(!CollectionUtils.isEmpty(dc_s.getAttachementList())){
						for(Integer w=0;w<dc_s.getAttachementList().size();w++){
								DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList().get(w); 
								if(!StringUtils.isBlank(dcd_s.getPath())){
									try {
										DataUtil.moveFileToDeleteFileDir(dcd_s.getPath());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
						}
					}
					dynamicConsequenceDao.deleteDynamicConseDetailAttachementByDetailId(d_s.getDetailList().get(fi).getId());
				}
			}
		}
	}

	@Override
	public List<DynamicConsequence> queryDynamicConsequence(Map<String, Object> param, PageSupport ps) {
		if(ps == null) {
			return dynamicConsequenceDao.queryDynamicConsequence(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.DynamicConsequenceDao.queryDynamicConsequence",
				"cn.hm.oil.dao.DynamicConsequenceDao.queryDinamicConsequence_count", param, ps);
		}
	}
	
	@Override
	public DynamicConsequence queryDynamicById(Integer id) {
		return dynamicConsequenceDao.queryDynamicById(id);
	}
	
	@Override
	public void saveDynamicVerify(DynamicConsequence dc) {
		dynamicConsequenceDao.saveDynamicVerify(dc);
		String content;
		if (dc.getStatus().intValue() == 1) {
			content = "您提交的动态高后果区资料已审核通过！";
		} else {
			content = "您提交的动态高后果区资料未审核通过！";
		}
		saveTips(content, dc.getCreater(), "/admin/base/d_sequel/show?id=" + dc.getId());
		//eventDao.updateEventStatus(status, message, event_id, type_id);
		
	}

	@Override
	public void deleteTips(Integer id) {
		tipsDao.deleteTipsByid(id);
		
	}

	@Override
	public List<DynamicConsequenceDetailAttachement> queryDynamicAttachementByDetailId(
			Integer id) {
		return dynamicConsequenceDao.queryDynamicAttachementByDetailId(id);
	}

	@Override
	public Integer queryVerifyMonthByDcId(Integer id) {
		return dynamicConsequenceDao.queryVerifyMonthByDcId(id);
	}

	@Override
	public void updateHighConsequenceAttachement(DynamicConsequence dc) {
		for(DynamicConsequenceDetail dcd : dc.getDetailList()){
			String af = dcd.getAnnex_file();
			Integer i = 1;
			for(String a:af.split(";")){
				if(i>dc.getVerify_month()){
					if(!StringUtils.isBlank(a)){
						DynamicConsequenceDetailAttachement dcda = new DynamicConsequenceDetailAttachement();
						try {
							dcda.setPath(DataUtil.moveToFileDir(a));
							dcda.setMonth(i);
							dcda.setDcd_id(dcd.getId());
							dynamicConsequenceDao.updateDynamicAttachement(dcda);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				i++;
			}
		}
		dynamicConsequenceDao.updateDynamicVerifyStatus(dc.getId(),0);
	}

	@Override
	public void deletePotentialCurveById(Integer id) {
		basePotentialCurveDao.deletePotentialCurveDetailByPCid(id);
		PotentialCurve pc = basePotentialCurveDao.queryPotentialCurveById(id);
		if(!StringUtils.isBlank(pc.getChartPath())){
			DataUtil.deleteByUploadImg(pc.getChartPath());
		}
		basePotentialCurveDao.deletePotentialCurveById(id);
	}

	@Override
	public void deletePerformanceMeasureById(Integer id) {
		basePerformanceMeasureNewDao.deletePerformanceMeasureDetailByPMid(id);
		basePerformanceMeasureNewDao.deletePerformanceMeasureById(id);
	}

	@Override
	public void deleteRunRecordcomprehensiveById(Integer id) {
		baseRunRecordcomprehensiveDao.deleteRunRecordcomprehensiveDetailByCid(id);
		baseRunRecordcomprehensiveDao.deleteRunRecordcomprehensiveById(id);
	}

	@Override
	public void deleteRunRecordById(Integer id) {
		baseRunRecordNewDao.deleteRunRecordDetailByRid(id);
		baseRunRecordNewDao.deleteRunRecordById(id);
	}

	@Override
	public void deleteFacilitiesMaintenanceById(Integer id) {
		baseFacilitiesMaintenanceDao.deleteFacilitiesMaintenanceDetailById(id);
		baseFacilitiesMaintenanceDao.deleteFacilitiesMaintenanceById(id);
	}

	@Override
	public void deleteValvePatrolById(Integer id) {
		valvePatrolDao.deleteValvePatrolDetailByVPId(id);
		valvePatrolDao.deleteValvePatrolById(id);
	}

	@Override
	public void deleteValveMaintById(Integer id) {
		valveMaintDao.deleteValueMaintByVMId(id);
		valveMaintDao.deleteValveMaintById(id);
	}
	
	@Override
	public void updatePipelinePatrolTime(Integer id, String create_t,
			String verify_t) {
		basePipelinePatrolNewDAO.updatePipelinePatrolTime(id,create_t,verify_t);
	}
	
	@Override
	public void updatePerformanceMeasureTime(Integer id, String create_t,
			String verify_t) {
		basePerformanceMeasureNewDao.updatePerformanceMeasureTime(id,create_t,verify_t);
	}
	
	@Override
	public void updateRunRecordTime(Integer id, String create_t,
			String verify_t) {
		baseRunRecordNewDao.updateRunRecordTime(id,create_t,verify_t);
	}
	
	@Override
	public void updatePotentialMeasureTime(Integer id, String create_t,
			String verify_t) {
		basePotentialMeasureDao.updatePotentialMeasureTime(id,create_t,verify_t);
	}

	@Override
	public PipelinePatrol queryPipelinePatrolByList(Map<String, Object> param) {
		return basePipelinePatrolNewDAO.queryPipelinePatrolByList(param);
	}

	@Override
	public int queryPipelinePatrolByListTotal(Map<String, Object> param) {
		return basePipelinePatrolNewDAO.queryPipelinePatrolByListTotal(param);
	}
	
	@Override
	public List<BasePipelineSpec> queryPipelinePatrolAuditSpecList(Map<String, Object> param) {
		return basePipelinePatrolNewDAO.queryPipelinePatrolAuditSpecList(param);
	}

	@Override
	public int queryValvePatrolByListTotal(Map<String, Object> param) {
		return basePerformanceMeasureNewDao.queryValvePatrolByListTotal(param);
	}

	@Override
	public PerformanceMeasure queryValvePatrolByList(Map<String, Object> param) {
		return basePerformanceMeasureNewDao.queryValvePatrolByList(param);
	}
	
	@Override
	public List<PerformanceMeasure> queryPerformanceMeasureAuditSpecList(Map<String, Object> param) {
		return basePerformanceMeasureNewDao.queryPerformanceMeasureAuditSpecList(param);
	}

	@Override
	public List<PotentialMeasure> queryPotentialMeasureMerge(Map<String, Object> param) {
		return basePotentialMeasureDao.queryPotentialMeasureMerge(param);
	}

	@Override
	public PotentialMeasure queryPotentialMeasureMergeInfo(Map<String, Object> param) {
		return basePotentialMeasureDao.queryPotentialMeasureMergeInfo(param);
	}

	@Override
	public List<PotentialMeasureDetail> queryPotentialMeasureDetailMerge(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BasePotentialMeasureNewDao.queryPotentialMeasureDetailMerge", param, ps);
	}
	
	@Override
	public List<PotentialMeasureDetail> queryPotentialMeasureDetailMerge(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePotentialMeasureDao.queryPotentialMeasureDetailMerge(param);
	}

	@Override
	public List<RunRecord> queryRunRecordMerge(Map<String, Object> param) {
		return baseRunRecordNewDao.queryRunRecordMerge(param);
	}

	@Override
	public RunRecord queryRunRecordMergeInfo(Map<String, Object> param) {
		return baseRunRecordNewDao.queryRunRecordMergeInfo(param);
	}

	@Override
	public List<RunRecordDetail> queryRunRecordDetailMerge(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BaseRunRecordNewDao.queryRunRecordDetailMerge", param, ps);
	}
	
	@Override
	public RunRecord RunRecordByList(Map<String, Object> param) {
		return baseRunRecordNewDao.RunRecordByList(param);
	}

	@Override
	public int RunRecordByListTotal(Map<String, Object> param) {
		return baseRunRecordNewDao.RunRecordByListTotal(param);
	}
	
	@Override
	public List<BasePipelineSpec> queryRunRecordAuditSpecList(Map<String, Object> param) {
		return baseRunRecordNewDao.queryRunRecordAuditSpecList(param);
	}

	@Override
	public PotentialMeasure queryPotentialMeasureByParam(Map<String, Object> param) {
		return basePotentialMeasureDao.queryPotentialMeasureByParam(param);
	}

	@Override
	public int queryPotentialMeasureByParamTotal(Map<String, Object> param) {
		return basePotentialMeasureDao.queryPotentialMeasureByParamTotal(param);
	}

	@Override
	public PipelinePatrol queryPipelinePatrolMergeInfo(Map<String, Object> param) {
		return basePipelinePatrolNewDAO.queryPipelinePatrolMergeInfo(param);
	}

	@Override
	public List<PipelinePatrol> queryPipelinePatrolMerge(Map<String, Object> param) {
		return basePipelinePatrolNewDAO.queryPipelinePatrolMerge(param);
	}

	@Override
	public List<PipelinePatrolDetail> queryPipelinePatrolDetailMerge(Map<String, Object> param, PageSupport ps) {
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BasePipelinePatrolNewDAO.queryPipelinePatrolDetailMerge", param, ps);
	}
	
	@Override
	public List<PipelinePatrolDetail> queryPipelinePatrolDetailMerge(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePipelinePatrolNewDAO.queryPipelinePatrolDetailMerge(param);
	}
	
	@Override
	public List<PipelinePatrolDetail> queryPipelinePatrolDetail(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps==null)
			return basePipelinePatrolNewDAO.queryPipelinePatrolDetail(param);
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BasePipelinePatrolNewDAO.queryPipelinePatrolDetail", param, ps);
	}
	
	@Override
	public List<PipelinePatrolDetail> queryPipelinePatrolDetail(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePipelinePatrolNewDAO.queryPipelinePatrolDetail(param);
	}

	@Override
	public List<PerformanceMeasureDetail> queryPerformanceMeasureDetail(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps==null)
			return basePerformanceMeasureNewDao.queryPerformanceMeasureDetail(param);
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BasePerformanceMeasureNewDao.queryPerformanceMeasureDetail", param, ps);
	}
	
	@Override
	public List<PerformanceMeasureDetail> queryPerformanceMeasureDetail(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePerformanceMeasureNewDao.queryPerformanceMeasureDetail(param);
	}

	@Override
	public List<RunRecordDetail> queryRunRecordDetail(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps==null)
			return baseRunRecordNewDao.queryRunRecordDetail(param);
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BaseRunRecordNewDao.queryRunRecordDetail", param, ps);
	}
	
	@Override
	public List<RunRecordDetail> queryRunRecordDetail(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseRunRecordNewDao.queryRunRecordDetail(param);
	}

	@Override
	public void updateCheckRunRecordDetailStatus(RunRecordDetail detail) {
		// TODO Auto-generated method stub
		if(detail.getStatus() == 255)
		{
			baseRunRecordNewDao.updateResetDetailStatus(detail.getId());
			detail.setStatus(new Integer(baseRunRecordNewDao.queryDetailStatus(detail.getId())));
		}
	}

	@Override
	public void updateChangeRunRecordDetailStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		baseRunRecordNewDao.updateChangeDetailStatus(id, status);
	}

	@Override
	public List<PotentialMeasureDetail> queryPotentialMeasureDetail(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps==null)
			return basePotentialMeasureDao.queryPotentialMeasureDetail(param);
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BasePotentialMeasureNewDao.queryPotentialMeasureDetail", param, ps);
	}
	
	@Override
	public List<PotentialMeasureDetail> queryPotentialMeasureDetail(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePotentialMeasureDao.queryPotentialMeasureDetail(param);
	}

	@Override
	public void updateCheckPotentialMeasureDetailStatus(PotentialMeasureDetail detail) {
		// TODO Auto-generated method stub
		if(detail.getStatus() == 255)
		{
			basePotentialMeasureDao.updateResetDetailStatus(detail.getId());
			detail.setStatus(new Integer(basePotentialMeasureDao.queryDetailStatus(detail.getId())));
		}
	}

	@Override
	public void updateChangePotentialMeasureDetailStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		basePotentialMeasureDao.updateChangeDetailStatus(id, status);
	}

	@Override
	public void updateCheckPipelinePatrolDetailStatus(PipelinePatrolDetail detail) {
		// TODO Auto-generated method stub
		if(detail.getStatus() == 255)
		{
			detail.setStatus(new Integer(basePipelinePatrolNewDAO.queryDetailStatus(detail.getId())));
			basePipelinePatrolNewDAO.updateResetDetailStatus(detail.getId());
		}
	}

	@Override
	public void updateChangePipelinePatrolDetailStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		basePipelinePatrolNewDAO.updateChangeDetailStatus(id, status);
	}

	@Override
	public void updateCheckPerformanceMeasureDetailStatus(PerformanceMeasureDetail detail) {
		// TODO Auto-generated method stub
		if(detail.getStatus() == 255)
		{
			basePerformanceMeasureNewDao.updateResetDetailStatus(detail.getId());
			detail.setStatus(new Integer(basePerformanceMeasureNewDao.queryDetailStatus(detail.getId())));
		}
	}

	@Override
	public void updateChangePerformanceMeasureDetailStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		basePerformanceMeasureNewDao.updateChangeDetailStatus(id, status);
	}

	@Override
	public void modifyPMeasureDetail(PerformanceMeasureDetail pmd) {
		// TODO Auto-generated method stub
		//basePerformanceMeasureNewDao.
		basePerformanceMeasureNewDao.updatePerformanceMeasureDetail(pmd);
	}
	
	
	
	@Override
	public List<RoutineAttentionDetail> queryRoutineAttentionDetail(Map<String, Object> param, PageSupport ps) {
		// TODO Auto-generated method stub
		if(ps==null)
			return baseRoutineAttentionDao.queryRoutineAttentionDetail(param);
		return this.getListPageSupportByManualOperationAutoCount("cn.hm.oil.dao.BaseRoutineAttentionDao.queryRoutineAttentionDetail", param, ps);
	}
	
	@Override
	public List<RoutineAttentionDetail> queryRoutineAttentionDetail(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseRoutineAttentionDao.queryRoutineAttentionDetail(param);
	}

	@Override
	public void updateCheckRoutineAttentionDetailStatus(RoutineAttentionDetail detail) {
		// TODO Auto-generated method stub
		if(detail.getStatus() == 255)
		{
			baseRoutineAttentionDao.updateResetDetailStatus(detail.getId());
			detail.setStatus(new Integer(baseRoutineAttentionDao.queryDetailStatus(detail.getId())));
		}
	}

	@Override
	public void updateChangeRoutineAttentionDetailStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		baseRoutineAttentionDao.updateChangeDetailStatus(id, status);
	}
	
	@Override
	public void modifyRoutineAttentionDetail(RoutineAttentionDetail rad) {
		// TODO Auto-generated method stub
		baseRoutineAttentionDao.updateRoutineAttentionDetail(rad);
	}

	@Override
	public List<BaseReceiver> queryLeaderList() {
		// TODO Auto-generated method stub
		return basePipeLineDao.queryLeaderList();
	}

	@Override
	public void updateChangeRunRecordcomprehensiveStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		baseRunRecordcomprehensiveDao.updateChangeDetailStatus(id, status);
	}

	@Override
	public void updateRunRecordcomprehensiveTime(Integer id, String create_t, String verify_t) {
		// TODO Auto-generated method stub
		baseRunRecordcomprehensiveDao.updateRunRecordcomprehensiveTime(id, create_t, verify_t);
	}

	@Override
	public List<BasePipelineSpec> queryRunRecordcomprehensiveAuditSpecList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseRunRecordcomprehensiveDao.queryRunRecordcomprehensiveAuditSpecList(param);
	}

	@Override
	public void updateCheckRunRecordcomprehensiveDetailStatus(RunRecordcomprehensiveDetail detail) {
		// TODO Auto-generated method stub
		if(detail.getStatus() == 255)
		{
			baseRunRecordcomprehensiveDao.updateResetDetailStatus(detail.getId());
			detail.setStatus(new Integer(baseRunRecordcomprehensiveDao.queryDetailStatus(detail.getId())));
		}
	}

	@Override
	public void updateRunRecordcomprehensiveDetail(RunRecordcomprehensiveDetail recd) {
		// TODO Auto-generated method stub
		baseRunRecordcomprehensiveDao.updateRunRecordcomprehensiveDetail(recd);
	}

	@Override
	public void updateAutoSubmitEveryWeek() {
		// TODO Auto-generated method stub
		basePerformanceMeasureNewDao.updateAutoSubmitEveryWeek();
		basePipelinePatrolNewDAO.updateAutoSubmitEveryWeek();
		basePotentialMeasureDao.updateAutoSubmitEveryWeek();
		baseRoutineAttentionDao.updateAutoSubmitEveryWeek();
		baseRunRecordcomprehensiveDao.updateAutoSubmitEveryWeek();
		baseRunRecordNewDao.updateAutoSubmitEveryWeek();
		constructionDao.updateAutoSubmitEveryWeek();
	}

	@Override
	public List<BasePipeline> queryPipeLineByAdminPatrol(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePipelinePatrolNewDAO.queryBasePipelineByAdminPatrol(param);
		return basePipeLineDao.queryPipeLine(param);
	}
	
	@Override
	public List<BasePipelineSection> querySectionByAdminPatrol(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePipelinePatrolNewDAO.queryBasePipelineSectionByAdminPatrol(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNewByAdminPatrol(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePipelinePatrolNewDAO.querySpecListNewByAdminPatrol(param);
		return basePipeLineDao.querySpecListNew(param);
	}
	
	@Override
	public List<BasePipeline> queryPipeLineByAdminPMeasure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePerformanceMeasureNewDao.queryBasePipelineByAdminPMeasure(param);
		return basePipeLineDao.queryPipeLine(param);
	}
	
	@Override
	public List<BasePipelineSection> querySectionByAdminPMeasure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePerformanceMeasureNewDao.queryBasePipelineSectionByAdminPMeasure(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNewByAdminPMeasure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePerformanceMeasureNewDao.querySpecListNewByAdminPMeasure(param);
		return basePipeLineDao.querySpecListNew(param);
	}

	@Override
	public List<BasePipeline> queryPipeLineByAdminRcComp(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return baseRunRecordcomprehensiveDao.queryBasePipelineByAdminRcComp(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<BasePipelineSection> querySectionByAdminRcComp(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseRunRecordcomprehensiveDao.queryBasePipelineSectionByAdminRcComp(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNewByAdminRcComp(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return baseRunRecordcomprehensiveDao.querySpecListNewByAdminRcComp(param);
		return basePipeLineDao.querySpecListNew(param);
	}

	@Override
	public List<BasePipeline> queryPipeLineByAdminRc(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return baseRunRecordNewDao.queryBasePipelineByAdminRc(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<BasePipelineSection> querySectionByAdminRc(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseRunRecordNewDao.queryBasePipelineSectionByAdminRc(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNewByAdminRc(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return baseRunRecordNewDao.querySpecListNewByAdminRc(param);
		return basePipeLineDao.querySpecListNew(param);
	}

	@Override
	public List<BasePipeline> queryPipeLineByAdminPlMeasure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePotentialMeasureDao.queryBasePipelineByAdminPlMeasure(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<BasePipelineSection> querySectionByAdminPlMeasure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePotentialMeasureDao.queryBasePipelineSectionByAdminPlMeasure(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNewByAdminPlMeasure(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePotentialMeasureDao.querySpecListNewByAdminPlMeasure(param);
		return basePipeLineDao.querySpecListNew(param);
	}

	@Override
	public List<BasePipeline> queryPipeLineByAdminRoutine(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return baseRoutineAttentionDao.queryBasePipelineByAdminRoutine(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<BasePipelineSection> querySectionByAdminRoutine(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return baseRoutineAttentionDao.queryBasePipelineSectionByAdminRoutine(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNewByAdminRoutine(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return baseRoutineAttentionDao.querySpecListNewByAdminRoutine(param);
		return basePipeLineDao.querySpecListNew(param);
	}

	@Override
	public List<BasePipeline> queryPipeLineByAdminPlCurve(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePotentialCurveDao.queryBasePipelineByAdminPlCure(param);
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<BasePipelineSection> querySectionByAdminPlCurve(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return basePotentialCurveDao.queryBasePipelineSectionByAdminPlCure(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNewByAdminPlCurve(Map<String, Object> param) {
		// TODO Auto-generated method stub
		if(param.containsKey("all"))
			return basePotentialCurveDao.querySpecListNewByAdminPlCure(param);
		return basePipeLineDao.querySpecListNew(param);
	}

	@Override
	public void updateChangeCurveStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		basePotentialCurveDao.updateChangeStatus(id, status);
	}
}
