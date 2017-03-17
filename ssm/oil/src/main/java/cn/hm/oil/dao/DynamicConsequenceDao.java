package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.DynamicConsequence;
import cn.hm.oil.domain.DynamicConsequenceDetail;
import cn.hm.oil.domain.DynamicConsequenceDetailAttachement;

public interface DynamicConsequenceDao {
	public void saveDynamicConse(DynamicConsequence dc);
	
	public void saveDynamicConseDetail(DynamicConsequenceDetail dcd);
	
	public List<DynamicConsequence> queryDynamicConsequence(Map<String, Object> param);
	
	public DynamicConsequence queryDynamicById(@Param(value="id") Integer id);
	
	public void saveDynamicVerify(DynamicConsequence dc);
	
	public void deleteDynamicConseDetailByDcId(@Param(value="dc_id") Integer dc_id);
	
	public void deleteDynamicConseDetailAttachementByDetailId(@Param(value="dcd_id") Integer dcd_id);
	
	public void updateDynamic(DynamicConsequence dc);
	
	public void insertDynamicConsequenceDetailAttachement(DynamicConsequenceDetailAttachement dcda);
	
	public List<DynamicConsequenceDetailAttachement> queryDynamicAttachementByDetailId(
			@Param(value="id") Integer id);
	
	public Integer queryVerifyMonthByDcId(@Param(value="id") Integer id);
	
	public void updateDynamicAttachement(DynamicConsequenceDetailAttachement dcda);
	
	public void updateDynamicVerifyStatus(@Param(value="id") Integer id,@Param(value="status")Integer status);
	
}
