package cn.hm.oil.dao;

import org.apache.ibatis.annotations.Param;

import cn.hm.oil.domain.Locality;
import cn.hm.oil.domain.LocalityDetail;

public interface LocalityDao {
	public void saveLocality(Locality locality);
	
	public void saveLocalityDetail(LocalityDetail lod);
	
	public Locality queryLocalityById(@Param(value = "id") Integer id);
	
	public Integer queryOperate_num(@Param(value = "id") Integer id);
	
	public void updateLocality(@Param(value = "id") Integer id);
	
	public void deleteLocalityDetail(@Param(value = "lo_id") Integer lo_id);
}
