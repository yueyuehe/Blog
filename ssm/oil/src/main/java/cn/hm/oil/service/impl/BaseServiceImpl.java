/**
 * 
 */
package cn.hm.oil.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hm.oil.dao.BaseFacilitiesMaintenanceDao;
import cn.hm.oil.dao.BaseHighConsequenceDao;
import cn.hm.oil.dao.BasePerformanceMeasureDao;
import cn.hm.oil.dao.BasePipeLineDao;
import cn.hm.oil.dao.BasePipelinePatrolDAO;
import cn.hm.oil.dao.BasePotentialCurveDao;
import cn.hm.oil.dao.BasePotentialMeasureDao;
import cn.hm.oil.dao.BaseRunRecordDao;
import cn.hm.oil.dao.BaseRunRecordcomprehensiveDao;
import cn.hm.oil.dao.CapitalDao;
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
import cn.hm.oil.service.BaseService;
import cn.hm.oil.util.AbstractModuleSuport;
import cn.hm.oil.util.AndroidPushMessageSample;
import cn.hm.oil.util.DataUtil;
import cn.hm.oil.util.PageSupport;
import cn.hm.oil.util.SimpleMailSender;

/**
 * @author Administrator
 *
 */
@Service(value = "baseService")
public class BaseServiceImpl extends AbstractModuleSuport implements BaseService {

	@Autowired
	private BasePipeLineDao basePipeLineDao;

	@Autowired
	private BasePotentialMeasureDao basePotentialMeasureDao;

	@Autowired
	private BaseFacilitiesMaintenanceDao baseFacilitiesMaintenanceDao;

	@Autowired
	private BasePipelinePatrolDAO basePipelinePatrolDAO;

	@Autowired
	private BaseRunRecordDao baseRunRecordDao;

	@Autowired
	private BaseRunRecordcomprehensiveDao baseRunRecordcomprehensiveDao;

	@Autowired
	private BasePerformanceMeasureDao basePerformanceMeasureDao;

	@Autowired
	private BasePotentialCurveDao basePotentialCurveDao;

	@Autowired
	private BaseHighConsequenceDao baseHighConsequenceDao;

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
	public List<BasePipelineSpec> queryRunRecordcomprehensiveAuditSpecList(Map<String, Object> param) {
		return baseRunRecordcomprehensiveDao.queryRunRecordcomprehensiveAuditSpecList(param);
	}

	@Override
	public RunRecordcomprehensive queryRunRecordcomprehensiveByParam(Map<String, Object> param) {
		return baseRunRecordcomprehensiveDao.queryRunRecordcomprehensiveByParam(param);
	}

	@Override
	public void updateBaseDataAudit() {
		basePotentialCurveDao.updateBaseDataAudit();
	}

	@Override
	public List<BasePipeline> queryPipeLine(Map<String, Object> param) {
		return basePipeLineDao.queryPipeLine(param);
	}

	@Override
	public List<BasePipelineSection> querySection(Integer pl_id) {
		return basePipeLineDao.querySection(pl_id);
	}

	@Override
	public List<BasePipelineSpec> querySpecList(Map<String, Object> param) {
		return basePipeLineDao.querySpecList(param);
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
	public void savePlPatrol(PipelinePatrol pp, List<PipelinePatrolDetail> ppdList) {
		if (pp.getId() != null && pp.getId().intValue() > 0) {
			basePipelinePatrolDAO.updatePipelinePatro(pp);
			basePipelinePatrolDAO.deletePipelinePatrolDetailById(pp.getId());
		} else {
			basePipelinePatrolDAO.insertPipelinePatrol(pp);
		}
		for (PipelinePatrolDetail ppd : ppdList) {
			ppd.setPp_id(pp.getId());
			basePipelinePatrolDAO.insertPipelinePatrolDetail(ppd);
		}
	}

	@Override
	public List<PipelinePatrol> queryPipelinePatrol(Map<String, Object> param, PageSupport ps) {
		if (ps == null) {
			return basePipelinePatrolDAO.queryPipelinePatrol(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BasePipelinePatrolDAO.queryPipelinePatrol",
					"cn.hm.oil.dao.BasePipelinePatrolDAO.queryPipelinePatrol_count", param, ps);
		}
	}

	@Override
	public List<PotentialMeasure> queryPotentialMeasure(Map<String, Object> param, PageSupport ps) {
		if (ps == null) {
			return basePotentialMeasureDao.queryPotentialMeasure(param);
		} else {
			return this.getListPageSupportByManualOperation(
					"cn.hm.oil.dao.BasePotentialMeasureDao.queryPotentialMeasure",
					"cn.hm.oil.dao.BasePotentialMeasureDao.queryPotentialMeasure_count", param, ps);
		}
	}

	@Override
	public void deletePotentialMeasureById(Integer id) {
		basePotentialMeasureDao.deletePotentialMeasureDetailByPMid(id);
		basePotentialMeasureDao.deletePotentialMeasureById(id);
	}

	@Override
	public void deletePipelinePatrolById(Integer id) {
		PipelinePatrol pp = basePipelinePatrolDAO.queryPipelinePatrolById(id);
		if (!CollectionUtils.isEmpty(pp.getDetailList())) {
			for (PipelinePatrolDetail pd : pp.getDetailList()) {
				if (!StringUtils.isBlank(pd.getAnnex())) {
					DataUtil.deleteByUploadImg(pd.getAnnex());
				}
			}
		}
		basePipelinePatrolDAO.deletePipelinePatrolDetailById(id);
		basePipelinePatrolDAO.deletePipelinePatrolById(id);
	}

	@Override
	public PotentialMeasure queryPotentialMeasureById(Integer id) {
		return basePotentialMeasureDao.queryPotentialMeasureById(id);
	}

	@Override
	public PipelinePatrol queryPipelinePatrolById(Integer id) {
		return basePipelinePatrolDAO.queryPipelinePatrolById(id);
	}

	@Override
	public void saveFMaint(FacilitiesMaintenance fm, List<FacilitiesMaintenanceDetail> fmdList) {
		if (fm.getId() != null && fm.getId().intValue() > 0) {
			baseFacilitiesMaintenanceDao.updateFacilitiesMaintenance(fm);
			baseFacilitiesMaintenanceDao.deleteFacilitiesMaintenanceDetailById(fm.getId());
		} else {
			baseFacilitiesMaintenanceDao.insertFacilitiesMaintenance(fm);
		}
		for (FacilitiesMaintenanceDetail fmd : fmdList) {
			fmd.setFm_id(fm.getId());
			baseFacilitiesMaintenanceDao.insertFacilitiesMaintenanceDetail(fmd);
		}
	}

	@Override
	public List<FacilitiesMaintenance> queryFacilitiesMaintenance(Map<String, Object> param, PageSupport ps) {
		if (ps == null) {
			return baseFacilitiesMaintenanceDao.queryFacilitiesMaintenance(param);
		} else {
			return this.getListPageSupportByManualOperation(
					"cn.hm.oil.dao.BaseFacilitiesMaintenanceDao.queryFacilitiesMaintenance",
					"cn.hm.oil.dao.BaseFacilitiesMaintenanceDao.queryFacilitiesMaintenance_count", param, ps);
		}
	}

	@Override
	public FacilitiesMaintenance queryFacilitiesMaintenanceById(Integer id) {
		return baseFacilitiesMaintenanceDao.queryFacilitiesMaintenanceById(id);
	}

	@Override
	public void saveRcRecord(RunRecord rc, List<RunRecordDetail> rcdList) {
		if (rc.getId() != null && rc.getId().intValue() > 0) {
			baseRunRecordDao.updateRunRecord(rc);
			baseRunRecordDao.deleteRunRecordDetailByRid(rc.getId());
		} else {
			baseRunRecordDao.insertRunRecord(rc);
		}
		for (RunRecordDetail rcd : rcdList) {
			rcd.setR_id(rc.getId());
			baseRunRecordDao.insertRunRecordDetail(rcd);
		}
	}

	@Override
	public List<RunRecord> queryRunRecord(Map<String, Object> param, PageSupport ps) {
		if (ps == null) {
			return baseRunRecordDao.queryRunRecord(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseRunRecordDao.queryRunRecord",
					"cn.hm.oil.dao.BaseRunRecordDao.queryRunRecord_count", param, ps);
		}
	}

	@Override
	public RunRecord queryRunRecordById(Integer id) {
		return baseRunRecordDao.queryRunRecordById(id);
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
		if (ps == null) {
			return baseRunRecordcomprehensiveDao.queryRunRecordcomprehensive(param);
		} else {
			return this.getListPageSupportByManualOperation(
					"cn.hm.oil.dao.BaseRunRecordcomprehensiveDao.queryRunRecordcomprehensive",
					"cn.hm.oil.dao.BaseRunRecordcomprehensiveDao.queryRunRecordcomprehensive_count", param, ps);
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
			basePerformanceMeasureDao.updatePerformanceMeasure(pm);
			basePerformanceMeasureDao.deletePerformanceMeasureDetailByPMid(pm.getId());
		} else {
			basePerformanceMeasureDao.insertPerformanceMeasure(pm);
		}
		for (PerformanceMeasureDetail pmd : pmdList) {
			pmd.setPm_id(pm.getId());
			basePerformanceMeasureDao.insertPerformanceMeasureDetail(pmd);
		}
	}

	@Override
	public List<PerformanceMeasure> queryPerformanceMeasure(Map<String, Object> param, PageSupport ps) {
		if (ps == null) {
			return basePerformanceMeasureDao.queryPerformanceMeasure(param);
		} else {
			return this.getListPageSupportByManualOperation(
					"cn.hm.oil.dao.BasePerformanceMeasureDao.queryPerformanceMeasure",
					"cn.hm.oil.dao.BasePerformanceMeasureDao.queryPerformanceMeasure_count", param, ps);
		}
	}

	@Override
	public PerformanceMeasure queryPerformanceMeasureById(Integer id) {
		return basePerformanceMeasureDao.queryPerformanceMeasureById(id);
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
		for (PotentialCurveDetail pcd : pcdList) {
			pcd.setPc_id(pc.getId());
			basePotentialCurveDao.insertPotentialCurveDetail(pcd);
		}
	}

	@Override
	public List<PotentialCurve> queryPotentialCurve(Map<String, Object> param, PageSupport ps) {
		if (ps == null) {
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
		if (status != null) {
			PotentialMeasure p = basePotentialMeasureDao.queryPotentialMeasureById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的管道保护电位测量记录已审核通过！";
			} else {
				content = "您提交的管道保护电位测量记录未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/pl_measure/show?id=" + id);
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		basePotentialMeasureDao.updatePotentialMeasureVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public List<BasePipeline> queryPipeLineByUser(Integer admin) {
		// TODO Auto-generated method stub
		return basePipeLineDao.queryPipeLineByUser(admin);
	}

	@Override
	public List<BasePipelineSection> querySectionByUser(Integer pl_id, Integer admin) {
		// TODO Auto-generated method stub
		return basePipeLineDao.querySectionByUser(pl_id, admin);
	}

	@Override
	public List<BasePipelineSpec> querySpecByUser(Integer pl_section_id, Integer admin) {
		// TODO Auto-generated method stub
		return basePipeLineDao.querySpecByUser(pl_section_id, admin);
	}

	@Override
	public void updatePerformanceMeasureVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		if (status != null) {
			PerformanceMeasure p = basePerformanceMeasureDao.queryPerformanceMeasureById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的绝缘接头(法兰)性能测量记录已审核通过！";
			} else {
				content = "您提交的绝缘接头(法兰)性能测量记录未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/p_measure/show?id=" + id);
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		basePerformanceMeasureDao.updatePerformanceMeasureVerifyStatus(id, verifier, status, verify_desc);

	}

	@Override
	public void updateRunRecordcomprehensiveVerifyStatus(Integer id, Integer verifier, Integer status,
			String verify_desc) {
		// TODO Auto-generated method stub
		if (status != null) {
			RunRecordcomprehensive p = baseRunRecordcomprehensiveDao.queryRunRecordcomprehensiveById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的阴极保护站运行月综合记录已审核通过！";
			} else {
				content = "您提交的阴极保护站运行月综合记录未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/rc_comp/show?id=" + id);
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		baseRunRecordcomprehensiveDao.updateRunRecordcomprehensiveVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public void updateRunRecordVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		// TODO Auto-generated method stub
		if (status != null) {
			RunRecord p = baseRunRecordDao.queryRunRecordById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的阴极保护站运行记录已审核通过！";
			} else {
				content = "您提交的阴极保护站运行记录未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/rc/show?id=" + id);
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		baseRunRecordDao.updateRunRecordVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public void updatePotentialCurveVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		
		basePotentialCurveDao.updatePotentialCurveVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public void updatePipelinePatrolVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc) {
		if (status != null) {
			PipelinePatrol p = basePipelinePatrolDAO.queryPipelinePatrolById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的管道巡检工作记录已审核通过！";
			} else {
				content = "您提交的管道巡检工作记录未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/pl_patrol/show?id=" + id);
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		basePipelinePatrolDAO.updatePipelinePatrolVerifyStatus(id, verifier, status, verify_desc);
	}

	@Override
	public void updateFacilitiesMaintenanceVerifyStatus(Integer id, Integer verifier, Integer status,
			String verify_desc) {
		if (status != null) {
			FacilitiesMaintenance f = baseFacilitiesMaintenanceDao.queryFacilitiesMaintenanceById(id);
			String content;
			if (status.intValue() == 1) {
				content = "您提交的集输气管线附属设施台帐已审核通过！";
			} else {
				content = "您提交的集输气管线附属设施台帐未审核通过！";
			}
			saveTips(content, f.getCreater(), "/admin/base/f_maint/show?id=" + id);
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		baseFacilitiesMaintenanceDao.updateFacilitiesMaintenanceVerifyStatus(id, verifier, status, verify_desc);
	}

	private void saveTips(String message, Integer user_id, String url) {
		Tips tips = new Tips();
		tips.setContent(message);
		tips.setUser_id(user_id);
		tips.setUrl(url);
		// 此处需要查询用户的通道id
		tipsDao.saveTips(tips);
		List<String> address = new ArrayList<String>();
		String mail = sysUsersDao.queryMailAddress(user_id);
		if (!StringUtils.isBlank(mail)) {
			address.add(mail);
			SimpleMailSender.sendMail(address, "您有新的消息，请及时处理", message);
		}
		SysPush push = sysPushDao.queryPush(tips.getUser_id());
		if (push != null) {
			AndroidPushMessageSample.pushNotice(push.getUserId(), push.getChannelId(), "新消息", tips);
		}
		// AndroidPushMessageSample.pushNotice("995379719876257548",
		// 3870620768511225045L, "新消息", tips);

	}

	@Override
	public void saveHighConse(HighConsequence hc, List<HighConsequenceDetail> hcdList) {
		if (hc.getId() != null && hc.getId().intValue() > 0) {
			baseHighConsequenceDao.updateHighConsequence(hc);
			baseHighConsequenceDao.deleteHighConsequenceDetailByHcId(hc.getId());
		} else {
			baseHighConsequenceDao.insertHighConsequence(hc);
		}
		for (HighConsequenceDetail hcd : hcdList) {
			hcd.setHc_id(hc.getId());
			baseHighConsequenceDao.insertHighConsequenceDetail(hcd);
		}
	}

	@Override
	public List<HighConsequence> queryHighConsequence(Map<String, Object> param, PageSupport ps) {
		if (ps == null) {
			return baseHighConsequenceDao.queryHighConsequence(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.BaseHighConsequenceDao.queryHighConsequence",
					"cn.hm.oil.dao.BaseHighConsequenceDao.queryHighConsequence_count", param, ps);
		}
	}

	@Override
	public List<Invest> queryInvest(PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.InvestDao.queryInvest",
				"cn.hm.oil.dao.InvestDao.queryInvest_count", null, ps);
	}

	@Override
	public List<Capital> queryCapital(PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.CapitalDao.queryCapital",
				"cn.hm.oil.dao.CapitalDao.queryCapital_count", null, ps);
	}

	@Override
	public List<Locality> queryLocality(PageSupport ps) {
		return this.getListPageSupportByManualOperation("cn.hm.oil.dao.LocalityDao.queryLocality",
				"cn.hm.oil.dao.LocalityDao.queryLocality_count", null, ps);
	}

	@Override
	public HighConsequence queryHighConsequenceById(Integer id) {
		return baseHighConsequenceDao.queryHighConsequenceById(id);
	}

	@Override
	public void updateHighConsequenceVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc,
			Integer creater) {
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
		if (invest.getId() != null) {
			investDao.deleteInvestDetail(invest.getId());
			investDao.updateInvest(invest.getId());
			for (InvestDetail ind : indList) {
				ind.setOperate_num(investDao.queryOperate_num(invest.getId()));
				ind.setIv_id(invest.getId());
				investDao.saveInvestDetail(ind);
			}
		} else {
			investDao.saveInvest(invest);
			for (InvestDetail ind : indList) {
				// ind.setOperate_num(invest.getOperate_num() + 1);
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
		if (capital.getId() != null) {
			capitalDao.deleteCapitalDetail(capital.getId());
			capitalDao.updateCapital(capital.getId());
			for (CapitalDetail cad : cadList) {
				cad.setOperate_num(capitalDao.queryOperate_num(capital.getId()));
				cad.setCa_id(capital.getId());
				capitalDao.saveCapitalDetail(cad);
			}
		} else {
			capitalDao.saveCapital(capital);
			for (CapitalDetail cad : cadList) {
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
		if (locality.getId() != null) {
			localityDao.deleteLocalityDetail(locality.getId());
			localityDao.updateLocality(locality.getId());
			for (LocalityDetail lod : lodList) {
				lod.setOperate_num(localityDao.queryOperate_num(locality.getId()));
				lod.setLo_id(locality.getId());
				localityDao.saveLocalityDetail(lod);
			}
		} else {
			localityDao.saveLocality(locality);
			for (LocalityDetail lod : lodList) {
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
		if (promptDao.quertPromptByType(prompt.getType_id()) == null) {
			promptDao.insertPrompt(prompt);
		} else {
			promptDao.updatePrompt(prompt);
		}
	}

	@Override
	public void saveValveMaint(ValveMaint vm, ValveMaintDetail vmd) {
		if (vm.getId() != null) {
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
		if (ps == null) {
			return valveMaintDao.queryValveMaint(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.ValveMaintDao.queryValveMaint",
					"cn.hm.oil.dao.ValveMaintDao.queryValveMaint_count", param, ps);
		}
	}

	@Override
	public ValveMaint queryValveMaintById(Integer id) {
		return valveMaintDao.queryValveMaintById(id);
	}

	@Override
	public void saveVMaintVerify(ValveMaint vm) {
		if (vm.getStatus() != null) {
			ValveMaint p = valveMaintDao.queryValveMaintById(vm.getId());
			String content;
			if (vm.getStatus().intValue() == 1) {
				content = "您提交的阀室、阀井维护保养工作记录已审核通过！";
			} else {
				content = "您提交的阀室、阀井维护保养工作记录未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/v_maint/show?id=" + vm.getId());
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		valveMaintDao.saveVerify(vm);
	}

	@Override
	public void saveVPatrol(ValvePatrol vp, ValvePatrolDetail vpd) {
		if (vp.getId() != null) {
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
		if (ps == null) {
			return valvePatrolDao.queryValvePatrol(param);
		} else {
			return this.getListPageSupportByManualOperation("cn.hm.oil.dao.ValvePatrolDao.queryValvePatrol",
					"cn.hm.oil.dao.ValvePatrolDao.queryValvePatrol_count", param, ps);
		}
	}

	@Override
	public ValvePatrol queryValvePatrolById(Integer id) {
		return valvePatrolDao.queryValvePatrolById(id);
	}

	@Override
	public void saveValveVerify(ValvePatrol vp) {
		if (vp.getStatus() != null) {
			ValvePatrol p = valvePatrolDao.queryValvePatrolById(vp.getId());
			String content;
			if (vp.getStatus().intValue() == 1) {
				content = "您提交的阀室、阀井巡检工作记录已审核通过！";
			} else {
				content = "您提交的阀室、阀井巡检工作记录未审核通过！";
			}
			saveTips(content, p.getCreater(), "/admin/base/v_patrol/show?id=" + vp.getId());
			// eventDao.updateEventStatus(status, message, event_id, type_id);
		}
		valvePatrolDao.saveValveVerify(vp);
	}

	@Override
	public void saveDynamicConseq(DynamicConsequence dc, List<DynamicConsequenceDetail> dcdList) {
		DynamicConsequence d_s = new DynamicConsequence();
		if (dc.getId() != null) {
			d_s = dynamicConsequenceDao.queryDynamicById(dc.getId());
			dynamicConsequenceDao.deleteDynamicConseDetailByDcId(dc.getId());
			dynamicConsequenceDao.updateDynamic(dc);
		} else {
			dynamicConsequenceDao.saveDynamicConse(dc);
		}
		Integer fi = 0;
		for (DynamicConsequenceDetail dcd : dcdList) {
			dcd.setDc_id(dc.getId());
			dynamicConsequenceDao.saveDynamicConseDetail(dcd);
			// List<DynamicConsequenceDetailAttachement> dcdas = new
			// ArrayList<DynamicConsequenceDetailAttachement>();
			if (!StringUtils.isBlank(dcd.getAnnex_file())) {
				Integer i = 1;
				for (String af : dcd.getAnnex_file().split(";")) {
					DynamicConsequenceDetailAttachement dcda = new DynamicConsequenceDetailAttachement();
					dcda.setDc_id(dc.getId());
					dcda.setDcd_id(dcd.getId());
					dcda.setMonth(i);
					if (!StringUtils.isBlank(af)) {
						String path = "";
						try {
							path = DataUtil.moveToFileDir(af);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (!StringUtils.isBlank(path)) {
							dcda.setPath(path);
						}
						if (!CollectionUtils.isEmpty(d_s.getDetailList())) {
							if (d_s.getDetailList().size() > fi) {
								DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
								if (!CollectionUtils.isEmpty(dc_s.getAttachementList())) {
									if (dc_s.getAttachementList().size() > (i - 1)) {
										DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList()
												.get(i - 1);
										if (!StringUtils.isBlank(dcd_s.getPath())) {
											try {
												DataUtil.moveFileToDeleteFileDir(dcd_s.getPath());
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									}
								}
							}
						}
					} else {
						if (!CollectionUtils.isEmpty(d_s.getDetailList())) {
							if (d_s.getDetailList().size() > fi) {
								DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
								if (!CollectionUtils.isEmpty(dc_s.getAttachementList())) {
									if (dc_s.getAttachementList().size() > (i - 1)) {
										DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList()
												.get(i - 1);
										if (!StringUtils.isBlank(dcd_s.getPath())) {
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
				if (i < 13) {
					for (; i < 13; i++) {
						DynamicConsequenceDetailAttachement dcda = new DynamicConsequenceDetailAttachement();
						dcda.setDc_id(dc.getId());
						dcda.setDcd_id(dcd.getId());
						dcda.setMonth(i);
						if (!CollectionUtils.isEmpty(d_s.getDetailList())) {
							if (d_s.getDetailList().size() > fi) {
								DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
								if (!CollectionUtils.isEmpty(dc_s.getAttachementList())) {
									if (dc_s.getAttachementList().size() > (i - 1)) {
										DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList()
												.get(i - 1);
										if (!StringUtils.isBlank(dcd_s.getPath())) {
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
				for (Integer f = 1; f < 13; f++) {
					DynamicConsequenceDetailAttachement dcda = new DynamicConsequenceDetailAttachement();
					dcda.setDc_id(dc.getId());
					dcda.setDcd_id(dcd.getId());
					dcda.setMonth(f);
					if (!CollectionUtils.isEmpty(d_s.getDetailList())) {
						if (d_s.getDetailList().size() > fi) {
							DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
							if (!CollectionUtils.isEmpty(dc_s.getAttachementList())) {
								if (dc_s.getAttachementList().size() > (f - 1)) {
									DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList().get(f - 1);
									if (!StringUtils.isBlank(dcd_s.getPath())) {
										dcda.setPath(dcd_s.getPath());
									}
								}
							}
						}
					}
					dynamicConsequenceDao.insertDynamicConsequenceDetailAttachement(dcda);
				}
			}
			if (!CollectionUtils.isEmpty(d_s.getDetailList())) {
				if (d_s.getDetailList().size() > fi) {
					dynamicConsequenceDao
							.deleteDynamicConseDetailAttachementByDetailId(d_s.getDetailList().get(fi).getId());
				}
			}
			fi++;
		}
		if (!CollectionUtils.isEmpty(d_s.getDetailList())) {
			if (d_s.getDetailList().size() > fi) {
				for (; fi < d_s.getDetailList().size(); fi++) {
					DynamicConsequenceDetail dc_s = d_s.getDetailList().get(fi);
					if (!CollectionUtils.isEmpty(dc_s.getAttachementList())) {
						for (Integer w = 0; w < dc_s.getAttachementList().size(); w++) {
							DynamicConsequenceDetailAttachement dcd_s = dc_s.getAttachementList().get(w);
							if (!StringUtils.isBlank(dcd_s.getPath())) {
								try {
									DataUtil.moveFileToDeleteFileDir(dcd_s.getPath());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
					dynamicConsequenceDao
							.deleteDynamicConseDetailAttachementByDetailId(d_s.getDetailList().get(fi).getId());
				}
			}
		}
	}

	@Override
	public List<DynamicConsequence> queryDynamicConsequence(Map<String, Object> param, PageSupport ps) {
		if (ps == null) {
			return dynamicConsequenceDao.queryDynamicConsequence(param);
		} else {
			return this.getListPageSupportByManualOperation(
					"cn.hm.oil.dao.DynamicConsequenceDao.queryDynamicConsequence",
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
		// eventDao.updateEventStatus(status, message, event_id, type_id);

	}

	@Override
	public void deleteTips(Integer id) {
		tipsDao.deleteTipsByid(id);

	}

	@Override
	public List<DynamicConsequenceDetailAttachement> queryDynamicAttachementByDetailId(Integer id) {
		return dynamicConsequenceDao.queryDynamicAttachementByDetailId(id);
	}

	@Override
	public Integer queryVerifyMonthByDcId(Integer id) {
		return dynamicConsequenceDao.queryVerifyMonthByDcId(id);
	}

	@Override
	public void updateHighConsequenceAttachement(DynamicConsequence dc) {
		for (DynamicConsequenceDetail dcd : dc.getDetailList()) {
			String af = dcd.getAnnex_file();
			Integer i = 1;
			for (String a : af.split(";")) {
				if (i > dc.getVerify_month()) {
					if (!StringUtils.isBlank(a)) {
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
		dynamicConsequenceDao.updateDynamicVerifyStatus(dc.getId(), 0);
	}

	@Override
	public void deletePotentialCurveById(Integer id) {
		basePotentialCurveDao.deletePotentialCurveDetailByPCid(id);
		PotentialCurve pc = basePotentialCurveDao.queryPotentialCurveById(id);
		if (!StringUtils.isBlank(pc.getChartPath())) {
			DataUtil.deleteByUploadImg(pc.getChartPath());
		}
		basePotentialCurveDao.deletePotentialCurveById(id);
	}

	@Override
	public void deletePerformanceMeasureById(Integer id) {
		basePerformanceMeasureDao.deletePerformanceMeasureDetailByPMid(id);
		basePerformanceMeasureDao.deletePerformanceMeasureById(id);
	}

	@Override
	public void deleteRunRecordcomprehensiveById(Integer id) {
		baseRunRecordcomprehensiveDao.deleteRunRecordcomprehensiveDetailByCid(id);
		baseRunRecordcomprehensiveDao.deleteRunRecordcomprehensiveById(id);
	}

	@Override
	public void deleteRunRecordById(Integer id) {
		baseRunRecordDao.deleteRunRecordDetailByRid(id);
		baseRunRecordDao.deleteRunRecordById(id);
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
	public List<BaseReceiver> queryLeaderList() {
		// TODO Auto-generated method stub
		return basePipeLineDao.queryLeaderList();
	}

	@Override
	public void updatePotentialCurveTime(Integer id, String create_t, String verify_t) {
		basePotentialCurveDao.updatePotentialCurveTime(id, create_t, verify_t);
	}

	@Override
	public void updateRunRecordcomprehensiveTime(Integer id, String create_t, String verify_t) {
		baseRunRecordcomprehensiveDao.updateRunRecordcomprehensiveTime(id, create_t, verify_t);
	}

	@Override
	public void updateValvePatrolTime(Integer id, String create_t, String verify_t) {
		valvePatrolDao.updateValvePatrolTime(id, create_t, verify_t);
	}

	@Override
	public void updateValveMaintTime(Integer id, String create_t, String verify_t) {
		valveMaintDao.updateValveMaintTime(id, create_t, verify_t);
	}

	@Override
	public int queryRunRecordcomprehensiveByParamTotal(Map<String, Object> param) {
		return baseRunRecordcomprehensiveDao.queryRunRecordcomprehensiveByParamTotal(param);
	}

	@Override
	public ValvePatrol queryValvePatrolByList(Map<String, Object> param) {
		return valvePatrolDao.queryValvePatrolByList(param);
	}

	@Override
	public int queryValvePatrolByListTotal(Map<String, Object> param) {
		return valvePatrolDao.queryValvePatrolByListTotal(param);
	}

	@Override
	public List<BasePipelineSpec> queryValvePatrolAuditSpecList(Map<String, Object> param) {
		return valvePatrolDao.queryValvePatrolAuditSpecList(param);
	}

	@Override
	public ValveMaint queryValveMaintByList(Map<String, Object> param) {
		return valveMaintDao.queryValveMaintByList(param);
	}

	@Override
	public int queryValveMaintByListTotal(Map<String, Object> param) {
		return valveMaintDao.queryValveMaintByListTotal(param);
	}

	@Override
	public List<BasePipelineSpec> queryValveMaintAuditSpecList(Map<String, Object> param) {
		return valveMaintDao.queryValveMaintAuditSpecList(param);
	}

	@Override
	public List<BasePipelineSpec> querySpecListNew(Map<String, Object> param) {

		return basePipeLineDao.querySpecListNew(param);
	}

	@Override
	public List<PotentialCurveDetail> queryPotentialCurveDetailByPcid(int pc_id) {

		return basePotentialCurveDao.queryPotentialCurveDetailBypcid(pc_id);
	}
}
