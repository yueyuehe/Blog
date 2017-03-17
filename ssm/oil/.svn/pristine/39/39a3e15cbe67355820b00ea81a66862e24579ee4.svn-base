package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.ValvePatrol;
import cn.hm.oil.domain.ValvePatrolDetail;

public interface ValvePatrolDao {
	public void saveValvePatrol(ValvePatrol vp);
	
	public void saveValvePatrolDetail(ValvePatrolDetail vpd);
	
	public ValvePatrol queryValvePatrolById(@Param(value="id") Integer id);
	
	public List<ValvePatrol> queryValvePatrol(Map<String, Object> param);
	
	public void updateValvePatrol(ValvePatrol vp);
	
	public void deleteValvePatrolDetailByVPId(@Param(value="vp_id") Integer vp_id);
	
	public void deleteValvePatrolById(@Param(value="id") Integer id);
	
	public void saveValveVerify(ValvePatrol vp);
	
	public void updateValvePatrolTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);

	public ValvePatrol queryValvePatrolByList(@Param("param") Map<String, Object> param);

	public int queryValvePatrolByListTotal(@Param("param") Map<String, Object> param);

	public List<BasePipelineSpec> queryValvePatrolAuditSpecList(@Param("param") Map<String, Object> param);
}
