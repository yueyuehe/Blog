package cn.hm.oil.service;

import java.util.List;
import java.util.Map;

import cn.hm.oil.domain.Hazard;
import cn.hm.oil.util.PageSupport;

public interface HazardService {
	public void saveHazard(Hazard hazard);
	
	public List<Hazard> queryHazard(Map<String, Object> param,PageSupport ps); 
	
	public Hazard queryHazardByid(Integer id);
	
	public void deleteHazardByid(Integer id);
}
