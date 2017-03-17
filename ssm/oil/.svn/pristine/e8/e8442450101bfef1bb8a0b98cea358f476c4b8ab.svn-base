/**
 * 
 */
package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

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
import cn.hm.oil.domain.ValveMaint;
import cn.hm.oil.domain.ValveMaintDetail;
import cn.hm.oil.domain.ValvePatrol;
import cn.hm.oil.domain.ValvePatrolDetail;
import cn.hm.oil.util.PageSupport;

/**
 * @author Administrator
 * 
 */
public interface BaseService {
	
	/**
	 * 根据ID查询管道保护电位测量记录
	 * 
	 * @param id
	 * @return
	 */
	public PotentialMeasure queryPotentialMeasureById(Integer id);
	
	/**
	 * 删除管道保护电位测量记录
	 * 
	 * @param id
	 */
	public void deletePotentialMeasureById(Integer id);
	
	public void deletePotentialCurveById(Integer id);
	
	public void deletePerformanceMeasureById(Integer id);
	
	public void deleteRunRecordcomprehensiveById(Integer id);
	
	public void deleteRunRecordById(Integer id);
	
	public void deleteFacilitiesMaintenanceById(Integer id);
	
	public void deleteValvePatrolById(Integer id);
	
	public void deleteValveMaintById(Integer id);
	
	public List<BasePipelineSpec> querySpecListNew( Map<String, Object> param);
	
	
	/**
	 * 删除管道巡检工作记录
	 * 
	 * @param id
	 */
	public void deletePipelinePatrolById(Integer id);
	
	/**
	 * 管道保护电位测量记录查询
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<PotentialMeasure> queryPotentialMeasure(Map<String, Object> param, PageSupport ps);
 	
	/**
	 * 保存管道保护电位测量记录
	 * 
	 * @param pm
	 * @param pmdList
	 */
	public void savePlMeasure(PotentialMeasure pm, List<PotentialMeasureDetail> pmdList);
	
	/**
	 * 管道巡检工作记录
	 * 
	 * @param pp
	 * @param ppdList
	 */
	public void savePlPatrol(PipelinePatrol pp, List<PipelinePatrolDetail> ppdList);
	
	
	/**
	 * 管道巡检工作记录查询
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<PipelinePatrol> queryPipelinePatrol(Map<String, Object> param, PageSupport ps);
 	
	/**
	 * 查询所有管线
	 * 
	 * @return
	 */
	public List<BasePipeline> queryPipeLine(Map<String, Object> param);
	
	public List<BasePipeline> queryPipeLineByUser(Integer admin);
	
	public List<BaseReceiver> queryLeaderList();
	
	public List<DynamicConsequenceDetailAttachement> queryDynamicAttachementByDetailId(Integer id);

	/**
	 * 查询所有管线下的所有起止段落
	 * 
	 * @param pl_id
	 * @return
	 */
	public List<BasePipelineSection> querySection(Integer pl_id);	
	
	/**
	 * 根据用户查询所有管线下的所有起止段落
	 * @param pl_id
	 * @param admin
	 * @return
	 */
	public List<BasePipelineSection> querySectionByUser(Integer pl_id, Integer admin);
	
	/**
	 * 根据用户查询所有起止段落下的所有规格
	 * @param base_pipeline_spec_id
	 * @param admin
	 * @return
	 */
	public List<BasePipelineSpec> querySpecByUser(Integer pl_section_id, Integer admin);

	/**
	 * 查询所有起止段落下的所有规格
	 * 
	 * @param pl_section_id
	 * @return
	 */
	public List<BasePipelineSpec> querySpec(Integer pl_section_id);
	
	
	/**
	 * 查询管线巡检工作记录
	 * 
	 * @param pp_id
	 * @return
	 */
	public PipelinePatrol queryPipelinePatrolById(Integer pp_id);
	
	/**
	 * 保存集输气管线附属设施维护记录
	 * 
	 * @param fm
	 * @param fmd
	 */
	public void saveFMaint(FacilitiesMaintenance fm, List<FacilitiesMaintenanceDetail> fmdList);
	
	/**
	 * 集输气管线附属设施维护记录查询
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<FacilitiesMaintenance> queryFacilitiesMaintenance(Map<String, Object> param, PageSupport ps);

	/**
	 * 集输气管线附属设施维护记录详细查询
	 * 
	 * @param pp_id
	 * @return
	 */
	public FacilitiesMaintenance queryFacilitiesMaintenanceById(Integer pp_id);
	
	/**
	 * 阴极保护站运行记录保存
	 * 
	 * 
	 */
	public void saveRcRecord(RunRecord rc, List<RunRecordDetail> rcdList);
	
	/**
	 * 阴极保护站运行记录查询
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<RunRecord> queryRunRecord(Map<String, Object> param, PageSupport ps);

	/**
	 * 阴极保护站运行记录详细查询
	 * 
	 * @param id
	 * @return
	 */
	public RunRecord queryRunRecordById(Integer id);
	
	/**
	 * 阴极保护站运行月综合记录保存
	 * 
	 * @param rec
	 * @param recd
	 */
	public void saveRcComp(RunRecordcomprehensive rec, RunRecordcomprehensiveDetail recd);

	/**
	 * 阴极保护站运行月综合记录查询
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<RunRecordcomprehensive> queryRunRecordcomprehensive(Map<String, Object> param, PageSupport ps); 
	
	/**
	 * 阴极保护站运行综合记录详细查询_表单内容
	 * 
	 * @param id
	 * @return
	 */
	public RunRecordcomprehensiveDetail queryRunRecordcomprehensiveDetailById(Integer id);
	
	/**
	 * 阴极保护站运行综合记录详细查询
	 * 
	 * @param id
	 * @return
	 */
	public RunRecordcomprehensive queryRunRecordcomprehensiveById(Integer id);
	
	/**
	 * 绝缘接头（法兰）性能测量记录保存
	 * 
	 * @param pm
	 * @param pmdList
	 */
	public void savePMeasure(PerformanceMeasure pm, List<PerformanceMeasureDetail> pmdList);
	
	/**
	 * 绝缘接头（法兰）性能测量记录查询
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<PerformanceMeasure> queryPerformanceMeasure(Map<String, Object> param, PageSupport ps);
	
	/**
	 * 绝缘接头（法兰）性能测量记录详细查询
	 * 
	 * @param id
	 * @return
	 */
	public PerformanceMeasure queryPerformanceMeasureById(Integer id);
	
	/**
	 * 管道保护电位曲线图保存
	 * 
	 * @param pc
	 * @param pcd
	 */
	public void savePlCurve(PotentialCurve pc, List<PotentialCurveDetail> pcdList);

	/**
	 * 管道保护电路曲线图查询
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<PotentialCurve> queryPotentialCurve(Map<String, Object> param, PageSupport ps);
	
	/**
	 * 管道保护电路曲线图详细查询
	 * 
	 * @param id
	 * @return
	 */
	public PotentialCurve queryPotentialCurveById(Integer id);
	
	public void updatePotentialCurveTime(Integer id,String create_t,String verify_t);
	
	public void updateRunRecordcomprehensiveTime(Integer id,String create_t,String verify_t);
	
	public void updateValvePatrolTime(Integer id,String create_t,String verify_t);
	
	public void updateValveMaintTime(Integer id,String create_t,String verify_t);
	
	/**
	 * 审核管道测量记录
	 * 
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updatePotentialMeasureVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);
	
	/**
	 * 审核绝缘接头记录
	 * 
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updatePerformanceMeasureVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);
	
	/**
	 * 审核月综合运行记录
	 * 
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updateRunRecordcomprehensiveVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);
	
	/**
	 * 审核月运行记录
	 * 
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updateRunRecordVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);

	/**
	 * 审核保护电路曲线图
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updatePotentialCurveVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);

	/**
	 * 审核管道巡检记录
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updatePipelinePatrolVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);

	/**
	 * 审核集输气管
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updateFacilitiesMaintenanceVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);

	/**
	 * 高后果区资料保存
	 * @param hc
	 * @param hcdList
	 */
	public void saveHighConse(HighConsequence hc, List<HighConsequenceDetail> hcdList);

	/**
	 * 高后果区资料查询
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<HighConsequence> queryHighConsequence(Map<String, Object> param, PageSupport ps);

	/**
	 * 通过id查找高后果区资料
	 * @param id
	 * @return
	 */
	public HighConsequence queryHighConsequenceById(Integer id);
	
	/**
	 * 高后果区资料审核信息修改
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updateHighConsequenceVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc, Integer creater);

	/**
	 * 投资项目保存
	 * @param invest
	 * @param indList
	 */
	public void saveInvest(Invest invest, List<InvestDetail> indList);
	
	/**
	 * 查找投资项目列表
	 * @param ps
	 * @return
	 */
	public List<Invest> queryInvest(PageSupport ps);
	
	/**
	 * 查找大修项目列表
	 * @param ps
	 * @return
	 */
	public List<Capital> queryCapital(PageSupport ps);
	
	/**
	 * 地方出资列表
	 * @param ps
	 * @return
	 */
	public List<Locality> queryLocality(PageSupport ps);
	
	/**
	 * 查询投资项目详细
	 * @param id
	 * @return
	 */
	public Invest queryInvestById(Integer id);
	
	/**
	 * 保存大修项目信息
	 * @param capital
	 * @param cadList
	 */
	public void saveCapital(Capital capital, List<CapitalDetail> cadList);
	
	/**
	 * 通过id查找大修项目
	 * @param id
	 * @return
	 */
	public Capital queryCapitalById(Integer id);
	
	public void saveLocality(Locality locality, List<LocalityDetail> lodList);
	
	/**
	 * 通过id查找大修项目
	 * @param id
	 * @return
	 */
	public Locality queryLocalityById(Integer id);
	
	/**
	 * 查询备注的种类
	 * @return
	 */
	public List<PromptType> queryPromptType();
	
	/**
	 * 查询指定类型的提示
	 * @param type_id
	 * @return
	 */
	public Prompt quertPromptByType(Integer type_id);
	
	/**
	 * 更新指定提示
	 * @param prompt
	 * @return
	 */
	public void updatePromptByType(Prompt prompt);
	
	/**
	 * 保存阀室、阀井维护保养工作记录
	 * @param vm
	 * @param vmd
	 */
	public void saveValveMaint(ValveMaint vm, ValveMaintDetail vmd);
	
	/**
	 * 查询阀室、阀井维护保养工作记录
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<ValveMaint> queryValveMaint(Map<String, Object> param, PageSupport ps);

	/**
	 * 通过ID查找阀室、阀井维护保养工作记录
	 * @param id
	 * @return
	 */
	public ValveMaint queryValveMaintById(Integer id);
	
	/**
	 * 保存审核信息
	 * @param vm
	 */
	public void saveVMaintVerify(ValveMaint vm);
	
	/**
	 * 保存阀室、阀井巡检工作记录
	 * @param vp
	 * @param vpd
	 */
	public void saveVPatrol(ValvePatrol vp,ValvePatrolDetail vpd);
	
	/**
	 * 查询阀室、阀井巡检工作记录
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<ValvePatrol> queryValvePatrol(Map<String, Object> param, PageSupport ps);

	/**
	 * 阀室、阀井巡检工作记录详细
	 * @param id
	 * @return
	 */
	public ValvePatrol queryValvePatrolById(Integer id);
	
	/**
	 * 保存阀室、阀井巡检工作记录审核结果
	 * @param vp
	 */
	public void saveValveVerify(ValvePatrol vp);
	
	/**
	 * 保存动态高后果区资料
	 * @param dc
	 * @param dcdList
	 */
	public void saveDynamicConseq(DynamicConsequence dc, List<DynamicConsequenceDetail> dcdList);

	/**
	 * 查询动态高后果区资料
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<DynamicConsequence> queryDynamicConsequence(Map<String, Object> param, PageSupport ps);
	
	/**
	 * 通过id查找动态高后果资料
	 * @param id
	 * @return
	 */
	public DynamicConsequence queryDynamicById(Integer id);
	
	/**
	 * 保存动态高后果区资料审核
	 * @param id
	 * @param status
	 * @param verify_desc
	 */
	public void saveDynamicVerify(DynamicConsequence dc);
	
	
	public void updateHighConsequenceAttachement(DynamicConsequence dc);
	
	public Integer queryVerifyMonthByDcId(Integer id);
	
	public void deleteTips(Integer id);

	/**
	 * 定时提交基础资料去审核
	 */
	public void updateBaseDataAudit();

	/**
	 * 查询阴极保护站运行月综合记录规格列表
	 * @param param
	 * @return
	 */
	public List<BasePipelineSpec> querySpecList(Map<String, Object> param);

	/**
	 * 查询所有规格
	 * @param param
	 * @return
	 */
	public RunRecordcomprehensive queryRunRecordcomprehensiveByParam(Map<String, Object> param);

	public int queryRunRecordcomprehensiveByParamTotal(Map<String, Object> param);

	/**
	 * 查询有审核的规格
	 * @param param
	 * @return
	 */
	public List<BasePipelineSpec> queryRunRecordcomprehensiveAuditSpecList(Map<String, Object> param);

	public ValvePatrol queryValvePatrolByList(Map<String, Object> param);

	public int queryValvePatrolByListTotal(Map<String, Object> param);

	public List<BasePipelineSpec> queryValvePatrolAuditSpecList(Map<String, Object> param);

	public ValveMaint queryValveMaintByList(Map<String, Object> param);

	public int queryValveMaintByListTotal(Map<String, Object> param);

	public List<BasePipelineSpec> queryValveMaintAuditSpecList(Map<String, Object> param);
	
	public List<PotentialCurveDetail> queryPotentialCurveDetailByPcid(int pc_id);
}
