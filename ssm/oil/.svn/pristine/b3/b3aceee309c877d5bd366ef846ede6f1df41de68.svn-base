package cn.hm.oil.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.PotentialCurveDetail;
import cn.hm.oil.domain.PotentialCurve_2016;
import cn.hm.oil.domain.SysUsers;

public interface BasePotentialCurveDao_2016 {

	public PotentialCurve_2016 queryPotentialCurveById(@Param(value = "id") Integer id);
	
	public void updatePotentialCurve(PotentialCurve_2016 pc);
	
	public void deletePotentialCurveDetailByPCid(@Param(value = "pc_id") Integer pc_id);
	
	public void insertPotentialCurve(PotentialCurve_2016 pc);
	
	public void insertPotentialCurveDetail(PotentialCurveDetail pcd);
	
	public List<PotentialCurve_2016> queryPotentialCurve(Map<String, Object> param);
	
	public List<BasePipeline> queryPipeLine(Map<String, Object> param);
	
	public List<SysUsers> queryUsers(Map<String, Object> param);
	
	public List<PotentialCurveDetail> queryPotentialCurveDetailByPcid(int pc_id);
	
	public void updatePotentialCurveVerifyStatus(@Param(value = "id") Integer id,
			@Param(value = "verifier") Integer verifier, @Param(value = "status") Integer status,
			@Param(value = "verify_desc") String verify_desc);

	public PotentialCurve_2016 queryPotentialCurveByMeasureId(Integer id);

	public void updateAutoSignRed(Map<String, Object> params);

	/*public void deletePotentialCurveById(@Param(value = "id") Integer id);

	

	

	public void updatePotentialCurveTime(@Param(value = "id") Integer id, @Param(value = "create_t") String create_t,
			@Param(value = "verify_t") String verify_t);

	public void updateBaseDataAudit();
	
	public List<PotentialCurveDetail> queryPotentialCurveDetailBypcid(@Param(value = "pc_id") Integer pc_id);
	
	public List<BasePipeline> queryBasePipelineByAdminPlCure(Map<String, Object> param);
	public List<BasePipelineSection> queryBasePipelineSectionByAdminPlCure(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPlCure(Map<String, Object> param);
	public void updateChangeStatus(@Param(value="id") Integer id, @Param(value="status") Integer status);*/
}
