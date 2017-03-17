package cn.hm.oil.dao;

import cn.hm.oil.domain.Hazard;


public interface BaseHazardDao {
	public void saveHazard(Hazard hazard);
	
	public Hazard queryHazardByid(Integer id);
	
	public void deleteHazardByid(Integer id);
}
