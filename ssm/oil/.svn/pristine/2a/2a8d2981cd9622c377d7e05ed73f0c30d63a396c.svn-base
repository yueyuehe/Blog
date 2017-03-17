package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.PerformanceMeasure;
import cn.hm.oil.domain.PerformanceMeasureDetail;
import cn.hm.oil.domain.PotentialMeasure;
/**
 * @author Administrator
 * 
 * 绝缘接头（法兰）性能测量记录_管理
 * 
 */

public interface BasePerformanceMeasureDao {
	
	public void updatePerformanceMeasure(PerformanceMeasure performanceMeasure);
	
	public void deletePerformanceMeasureDetailByPMid(@Param(value="pm_id") Integer pm_id);
	
	public void deletePerformanceMeasureById(@Param(value="id") Integer id);
	
	public PerformanceMeasure queryPerformanceMeasureById(@Param(value="id") Integer id);
	
	public void insertPerformanceMeasure(PerformanceMeasure pm);
	
	public void insertPerformanceMeasureDetail(PerformanceMeasureDetail pmd);
	
	public List<PerformanceMeasure> queryPerformanceMeasure(Map<String, Object> param);
	
	public void updatePerformanceMeasureVerifyStatus(@Param(value="id") Integer id, @Param(value="verifier") Integer verifier, @Param(value="status") Integer status, @Param(value="verify_desc") String verify_desc);
}
