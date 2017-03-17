package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipeline;
import cn.hm.oil.domain.BasePipelineSection;
import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.PotentialCurve;
import cn.hm.oil.domain.PotentialCurveDetail;

public interface BasePotentialCurveDao {

	public PotentialCurve queryPotentialCurveById(@Param(value = "id") Integer id);

	public void deletePotentialCurveDetailByPCid(@Param(value = "pc_id") Integer pc_id);

	public void deletePotentialCurveById(@Param(value = "id") Integer id);

	public void insertPotentialCurve(PotentialCurve pc);

	public void insertPotentialCurveDetail(PotentialCurveDetail pcd);

	public void updatePotentialCurve(PotentialCurve pc);

	public List<PotentialCurve> queryPotentialCurve(Map<String, Object> param);

	public void updatePotentialCurveVerifyStatus(@Param(value = "id") Integer id,
			@Param(value = "verifier") Integer verifier, @Param(value = "status") Integer status,
			@Param(value = "verify_desc") String verify_desc);

	public void updatePotentialCurveTime(@Param(value = "id") Integer id, @Param(value = "create_t") String create_t,
			@Param(value = "verify_t") String verify_t);

	public void updateBaseDataAudit();
	
	public List<PotentialCurveDetail> queryPotentialCurveDetailBypcid(@Param(value = "pc_id") Integer pc_id);
	
	public List<BasePipeline> queryBasePipelineByAdminPlCure(Map<String, Object> param);
	public List<BasePipelineSection> queryBasePipelineSectionByAdminPlCure(Map<String, Object> param);
	public List<BasePipelineSpec> querySpecListNewByAdminPlCure(Map<String, Object> param);
	public void updateChangeStatus(@Param(value="id") Integer id, @Param(value="status") Integer status);
}
