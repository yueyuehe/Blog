/**
 * 
 */
package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.PotentialMeasure;
import cn.hm.oil.domain.PotentialMeasureDetail;

/**
 * @author Administrator
 * 
 * 管道保护电位测量_管理
 * 
 */
public interface BasePotentialMeasureDao {
	
	public void updatePotentialMeasure(PotentialMeasure potentialMeasure);
	
	public void deletePotentialMeasureDetailByPMid(@Param(value="pm_id") Integer pm_id);
	
	public PotentialMeasure queryPotentialMeasureById(@Param(value="id") Integer id);
	
	public void deletePotentialMeasureById(@Param(value="id") Integer id);
	
	public void insertPotentialMeasure(PotentialMeasure potentialMeasure);
	
	public void insertPotentialMeasureDetail(PotentialMeasureDetail potentialMeasureDetail);
	
	public List<PotentialMeasure> queryPotentialMeasure(Map<String, Object> param);
	
	public void updatePotentialMeasureVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
}
