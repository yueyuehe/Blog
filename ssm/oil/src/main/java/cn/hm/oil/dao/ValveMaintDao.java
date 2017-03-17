package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.BasePipelineSpec;
import cn.hm.oil.domain.ValveMaint;
import cn.hm.oil.domain.ValveMaintDetail;

public interface ValveMaintDao {
	public void saveValveMaint(ValveMaint vm);
	
	public void updateValueMaint(ValveMaint vm);
	
	public void deleteValueMaintByVMId(@Param(value = "vm_id") Integer vm_id);
	
	public void deleteValveMaintById(@Param(value = "id") Integer id);
	
	public void saveValveMaintDetail(ValveMaintDetail vmd);
	
	public ValveMaint queryValveMaintById(@Param(value = "id") Integer id);
	
	public List<ValveMaint> queryValveMaint(Map<String, Object> param);
	
	public void saveVerify(ValveMaint vm);
	
	public void updateValveMaintTime(@Param(value="id") Integer id,@Param(value="create_t") String create_t,@Param(value="verify_t") String verify_t);

	public ValveMaint queryValveMaintByList(@Param("param") Map<String, Object> param);

	public int queryValveMaintByListTotal(@Param("param") Map<String, Object> param);

	public List<BasePipelineSpec> queryValveMaintAuditSpecList(@Param("param") Map<String, Object> param);
}
