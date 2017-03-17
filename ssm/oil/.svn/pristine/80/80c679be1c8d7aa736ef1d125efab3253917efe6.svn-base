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
import cn.hm.oil.domain.RoutineAttentionDetail;
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
public interface NewBaseService {

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
	 * 绝缘接头（法兰）性能测量记录修改
	 * 
	 * @param pp
	 * @param ppdList
	 */
	public void modifyPlMeasureDetail(PotentialMeasureDetail pmd);
	
	/**
	 * 管道巡检工作记录
	 * 
	 * @param pp
	 * @param ppdList
	 */
	public void savePlPatrol(PipelinePatrol pp, List<PipelinePatrolDetail> ppdList);
	
	/**
	 * 管道巡检工作记录修改
	 * 
	 * @param pp
	 * @param ppdList
	 */
	public void modifyPlPatrolDetail(PipelinePatrolDetail ppd);

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
	public List<BasePipeline> queryPipeLine();

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
	 * 
	 * @param pl_id
	 * @param admin
	 * @return
	 */
	public List<BasePipelineSection> querySectionByUser(Integer pl_id, Integer admin);

	/**
	 * 根据用户查询所有起止段落下的所有规格
	 * 
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
	
	public void modifyRunRecordDetail(RunRecordDetail rcd);

	public void updatePipelinePatrolTime(Integer id, String create_t, String verify_t);

	public void updatePerformanceMeasureTime(Integer id, String create_t, String verify_t);

	public void updateRunRecordTime(Integer id, String create_t, String verify_t);

	public void updatePotentialMeasureTime(Integer id, String create_t, String verify_t);

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
	 * 绝缘接头（法兰）性能测量记录修改
	 * 
	 * @param pp
	 * @param ppdList
	 */
	public void modifyPMeasureDetail(PerformanceMeasureDetail pmd);

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
	public void updateRunRecordcomprehensiveVerifyStatus(Integer id, Integer verifier, Integer status,
			String verify_desc);

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
	 * 
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updatePotentialCurveVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);

	/**
	 * 审核管道巡检记录
	 * 
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updatePipelinePatrolVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc);

	/**
	 * 审核集输气管
	 * 
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updateFacilitiesMaintenanceVerifyStatus(Integer id, Integer verifier, Integer status,
			String verify_desc);

	/**
	 * 高后果区资料保存
	 * 
	 * @param hc
	 * @param hcdList
	 */
	public void saveHighConse(HighConsequence hc, List<HighConsequenceDetail> hcdList);

	/**
	 * 高后果区资料查询
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<HighConsequence> queryHighConsequence(Map<String, Object> param, PageSupport ps);

	/**
	 * 通过id查找高后果区资料
	 * 
	 * @param id
	 * @return
	 */
	public HighConsequence queryHighConsequenceById(Integer id);

	/**
	 * 高后果区资料审核信息修改
	 * 
	 * @param id
	 * @param verifier
	 * @param status
	 * @param verify_desc
	 */
	public void updateHighConsequenceVerifyStatus(Integer id, Integer verifier, Integer status, String verify_desc,
			Integer creater);

	/**
	 * 投资项目保存
	 * 
	 * @param invest
	 * @param indList
	 */
	public void saveInvest(Invest invest, List<InvestDetail> indList);

	/**
	 * 查找投资项目列表
	 * 
	 * @param ps
	 * @return
	 */
	public List<Invest> queryInvest(PageSupport ps);

	/**
	 * 查找大修项目列表
	 * 
	 * @param ps
	 * @return
	 */
	public List<Capital> queryCapital(PageSupport ps);

	/**
	 * 地方出资列表
	 * 
	 * @param ps
	 * @return
	 */
	public List<Locality> queryLocality(PageSupport ps);

	/**
	 * 查询投资项目详细
	 * 
	 * @param id
	 * @return
	 */
	public Invest queryInvestById(Integer id);

	/**
	 * 保存大修项目信息
	 * 
	 * @param capital
	 * @param cadList
	 */
	public void saveCapital(Capital capital, List<CapitalDetail> cadList);

	/**
	 * 通过id查找大修项目
	 * 
	 * @param id
	 * @return
	 */
	public Capital queryCapitalById(Integer id);

	public void saveLocality(Locality locality, List<LocalityDetail> lodList);

	/**
	 * 通过id查找大修项目
	 * 
	 * @param id
	 * @return
	 */
	public Locality queryLocalityById(Integer id);

	/**
	 * 查询备注的种类
	 * 
	 * @return
	 */
	public List<PromptType> queryPromptType();

	/**
	 * 查询指定类型的提示
	 * 
	 * @param type_id
	 * @return
	 */
	public Prompt quertPromptByType(Integer type_id);

	/**
	 * 更新指定提示
	 * 
	 * @param prompt
	 * @return
	 */
	public void updatePromptByType(Prompt prompt);

	/**
	 * 保存阀室、阀井维护保养工作记录
	 * 
	 * @param vm
	 * @param vmd
	 */
	public void saveValveMaint(ValveMaint vm, ValveMaintDetail vmd);

	/**
	 * 查询阀室、阀井维护保养工作记录
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<ValveMaint> queryValveMaint(Map<String, Object> param, PageSupport ps);

	/**
	 * 通过ID查找阀室、阀井维护保养工作记录
	 * 
	 * @param id
	 * @return
	 */
	public ValveMaint queryValveMaintById(Integer id);

	/**
	 * 保存审核信息
	 * 
	 * @param vm
	 */
	public void saveVMaintVerify(ValveMaint vm);

	/**
	 * 保存阀室、阀井巡检工作记录
	 * 
	 * @param vp
	 * @param vpd
	 */
	public void saveVPatrol(ValvePatrol vp, ValvePatrolDetail vpd);

	/**
	 * 查询阀室、阀井巡检工作记录
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<ValvePatrol> queryValvePatrol(Map<String, Object> param, PageSupport ps);

	/**
	 * 阀室、阀井巡检工作记录详细
	 * 
	 * @param id
	 * @return
	 */
	public ValvePatrol queryValvePatrolById(Integer id);

	/**
	 * 保存阀室、阀井巡检工作记录审核结果
	 * 
	 * @param vp
	 */
	public void saveValveVerify(ValvePatrol vp);

	/**
	 * 保存动态高后果区资料
	 * 
	 * @param dc
	 * @param dcdList
	 */
	public void saveDynamicConseq(DynamicConsequence dc, List<DynamicConsequenceDetail> dcdList);

	/**
	 * 查询动态高后果区资料
	 * 
	 * @param param
	 * @param ps
	 * @return
	 */
	public List<DynamicConsequence> queryDynamicConsequence(Map<String, Object> param, PageSupport ps);

	/**
	 * 通过id查找动态高后果资料
	 * 
	 * @param id
	 * @return
	 */
	public DynamicConsequence queryDynamicById(Integer id);

	/**
	 * 保存动态高后果区资料审核
	 * 
	 * @param id
	 * @param status
	 * @param verify_desc
	 */
	public void saveDynamicVerify(DynamicConsequence dc);

	public void updateHighConsequenceAttachement(DynamicConsequence dc);

	public Integer queryVerifyMonthByDcId(Integer id);

	public void deleteTips(Integer id);

	public PipelinePatrol queryPipelinePatrolByList(Map<String, Object> param);

	public int queryPipelinePatrolByListTotal(Map<String, Object> param);

	public List<BasePipelineSpec> queryPipelinePatrolAuditSpecList(Map<String, Object> param);

	public int queryValvePatrolByListTotal(Map<String, Object> param);

	public PerformanceMeasure queryValvePatrolByList(Map<String, Object> param);

	public List<PerformanceMeasure> queryPerformanceMeasureAuditSpecList(Map<String, Object> param);

	public List<PotentialMeasure> queryPotentialMeasureMerge(Map<String, Object> param);

	public PotentialMeasure queryPotentialMeasureMergeInfo(Map<String, Object> param);

	public List<PotentialMeasureDetail> queryPotentialMeasureDetailMerge(Map<String, Object> param, PageSupport ps);
	public List<PotentialMeasureDetail> queryPotentialMeasureDetailMerge(Map<String, Object> param);

	public List<RunRecord> queryRunRecordMerge(Map<String, Object> param);

	public RunRecord queryRunRecordMergeInfo(Map<String, Object> param);
	
	public List<RunRecordDetail> queryRunRecordDetailMerge(Map<String, Object> param, PageSupport ps);

	public RunRecord RunRecordByList(Map<String, Object> param);

	public int RunRecordByListTotal(Map<String, Object> param);

	public List<BasePipelineSpec> queryRunRecordAuditSpecList(Map<String, Object> param);

	public PotentialMeasure queryPotentialMeasureByParam(Map<String, Object> param);

	public int queryPotentialMeasureByParamTotal(Map<String, Object> param);
	
	//管线巡检工作记录
	public PipelinePatrol queryPipelinePatrolMergeInfo(Map<String, Object> param);
	public List<PipelinePatrol> queryPipelinePatrolMerge(Map<String, Object> param);
	public List<PipelinePatrolDetail> queryPipelinePatrolDetailMerge(Map<String, Object> param, PageSupport ps);
	public List<PipelinePatrolDetail> queryPipelinePatrolDetailMerge(Map<String, Object> param);
	public List<PipelinePatrolDetail> queryPipelinePatrolDetail(Map<String, Object> param, PageSupport ps);
	public List<PipelinePatrolDetail> queryPipelinePatrolDetail(Map<String, Object> param);
	public void	updateCheckPipelinePatrolDetailStatus(PipelinePatrolDetail detail);
	public void updateChangePipelinePatrolDetailStatus(Integer id, Integer status);
	public List<BasePipeline> queryPipeLineByAdminPatrol(Map<String, Object> param);
	public List<BasePipelineSection> querySectionByAdminPatrol(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPatrol( Map<String, Object> param);
	
	//绝缘接头（法兰）性能测量记录
	public List<PerformanceMeasureDetail> queryPerformanceMeasureDetail(Map<String, Object> param, PageSupport ps);
	public List<PerformanceMeasureDetail> queryPerformanceMeasureDetail(Map<String, Object> param);
	public void	updateCheckPerformanceMeasureDetailStatus(PerformanceMeasureDetail detail);
	public void updateChangePerformanceMeasureDetailStatus(Integer id, Integer status);
	public List<BasePipeline> queryPipeLineByAdminPMeasure(Map<String, Object> param);
	public List<BasePipelineSection> querySectionByAdminPMeasure(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPMeasure( Map<String, Object> param);
	
	//阴极保护站运行记录
	public List<RunRecordDetail> queryRunRecordDetail(Map<String, Object> param, PageSupport ps);
	public List<RunRecordDetail> queryRunRecordDetail(Map<String, Object> param);
	public void	updateCheckRunRecordDetailStatus(RunRecordDetail detail);
	public void updateChangeRunRecordDetailStatus(Integer id, Integer status);	
	public List<BasePipeline> queryPipeLineByAdminRc(Map<String, Object> param);
	public List<BasePipelineSection> querySectionByAdminRc(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminRc( Map<String, Object> param);
	
	//管道保护电位测量记录
	public List<PotentialMeasureDetail> queryPotentialMeasureDetail(Map<String, Object> param, PageSupport ps);
	public List<PotentialMeasureDetail> queryPotentialMeasureDetail(Map<String, Object> param);
	public void	updateCheckPotentialMeasureDetailStatus(PotentialMeasureDetail detail);
	public void updateChangePotentialMeasureDetailStatus(Integer id, Integer status);
	public List<BasePipeline> queryPipeLineByAdminPlMeasure(Map<String, Object> param);
	public List<BasePipelineSection> querySectionByAdminPlMeasure(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPlMeasure( Map<String, Object> param);
	
	//管线日常维护工作记录
	public List<RoutineAttentionDetail> queryRoutineAttentionDetail(Map<String, Object> param, PageSupport ps);
	public List<RoutineAttentionDetail> queryRoutineAttentionDetail(Map<String, Object> param);
	public void	updateCheckRoutineAttentionDetailStatus(RoutineAttentionDetail detail);
	public void updateChangeRoutineAttentionDetailStatus(Integer id, Integer status);
	public void modifyRoutineAttentionDetail(RoutineAttentionDetail rad);
	public List<BasePipeline> queryPipeLineByAdminRoutine(Map<String, Object> param);
	public List<BasePipelineSection> querySectionByAdminRoutine(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminRoutine( Map<String, Object> param);
	
	//阴极保护站运行月综合记录
	public void updateChangeRunRecordcomprehensiveStatus(Integer id, Integer status);
	public void updateRunRecordcomprehensiveTime(Integer id,String create_t,String verify_t);
	public List<BasePipelineSpec> queryRunRecordcomprehensiveAuditSpecList(Map<String, Object> param);
	
	public void	updateCheckRunRecordcomprehensiveDetailStatus(RunRecordcomprehensiveDetail detail);
	public void updateRunRecordcomprehensiveDetail(RunRecordcomprehensiveDetail recd);
	public List<BasePipeline> queryPipeLineByAdminRcComp(Map<String, Object> param);
	public List<BasePipelineSection> querySectionByAdminRcComp(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminRcComp( Map<String, Object> param);
	
	public List<BasePipeline> queryPipeLineByAdminPlCurve(Map<String, Object> param);
	public List<BasePipelineSection> querySectionByAdminPlCurve(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPlCurve( Map<String, Object> param);
	
	public void updateChangeCurveStatus(Integer id, Integer status);
	
	public void updateAutoSubmitEveryWeek();
}
