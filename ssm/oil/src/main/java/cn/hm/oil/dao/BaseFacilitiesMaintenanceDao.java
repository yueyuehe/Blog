package cn.hm.oil.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.FacilitiesMaintenance;
import cn.hm.oil.domain.FacilitiesMaintenanceDetail;

public interface BaseFacilitiesMaintenanceDao {
	
	public FacilitiesMaintenance queryFacilitiesMaintenanceById(@Param(value="id") Integer id);
	
	public void insertFacilitiesMaintenance(FacilitiesMaintenance facilitiesMaintenance);

	public void insertFacilitiesMaintenanceDetail(FacilitiesMaintenanceDetail facilitiesMaintenanceDetail);
	
	public List<FacilitiesMaintenance> queryFacilitiesMaintenance(Map<String, Object> param);

	public void updateFacilitiesMaintenanceVerifyStatus(@Param(value="id") Integer id,@Param(value="verifier") Integer verifier,@Param(value="status") Integer status,@Param(value="verify_desc") String verify_desc);
	
	public void updateFacilitiesMaintenance(FacilitiesMaintenance facilitiesMaintenance);

	public void deleteFacilitiesMaintenanceDetailById(@Param(value="fm_id") Integer fm_id);
	
	public void deleteFacilitiesMaintenanceById(@Param(value="id") Integer id);
}
